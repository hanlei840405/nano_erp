package com.nano.web.controller.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Express;
import com.nano.domain.base.Platform;
import com.nano.domain.base.PlatformExpress;
import com.nano.service.base.ExpressService;
import com.nano.service.base.PlatformExpressService;
import com.nano.service.base.PlatformService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.PlatformVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("base/platform")
public class PlatformController {
    private static final Logger logger = LoggerFactory
            .getLogger(PlatformController.class);
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private PlatformExpressService platformExpressService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<PlatformVO> save(PlatformVO vo,HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存平台信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<PlatformVO> responseVO = new ResponseVO<>();
        Platform platform;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
                platform = platformService.findOne(vo.getId());
                platform.setModifier(session.getAttribute("userId").toString());
            } else {
                platform = new Platform();
                platform.setCreator(session.getAttribute("userId").toString());
            }
            platform.setCode(vo.getCode());
            platform.setName(vo.getName());
            platform.setStatus(vo.getStatus());


            List<Map<String,String>> inserts = mapper.readValue(
                    request.getParameter("inserts"),
                    new TypeReference<List<Map<String,String>>>() {
                    });

            List<PlatformExpress> platformExpresses = new ArrayList<>();
            for(Map<String,String> entity : inserts){
                PlatformExpress platformExpress = new PlatformExpress();
                platformExpress.setPlatformId(platform.getId());
                platformExpress.setExpressId(entity.get("expressId"));
                platformExpress.setCode(entity.get("code"));
                platformExpress.setAlias(entity.get("alias"));
                platformExpresses.add(platformExpress);
            }

            if (!StringUtils.isEmpty(vo.getId())) {
                platformService.update(platform,platformExpresses);
            } else {
                platformService.save(platform,platformExpresses);
            }
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(false);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseVO<PlatformVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除平台信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<PlatformVO> responseVO = new ResponseVO<>();
        try {
            platformService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryPlatforms", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<PlatformVO> queryPlatforms(PlatformVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询平台信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Platform queryParmas = new Platform();
        queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo.getName());
        queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo.getCode());
        queryParmas.setStatus(StringUtils.isEmpty(vo.getStatus()) ? null : vo.getStatus());
        PageInfo<Platform> pageInfo = platformService.find(queryParmas, page - 1, rows);
        PageVO<PlatformVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Platform platform : pageInfo.getList()) {
            PlatformVO platformVO = new PlatformVO();
            platformVO.setId(platform.getId());
            platformVO.setName(platform.getName());
            platformVO.setCode(platform.getCode());
            platformVO.setStatus(platform.getStatus());
            PlatformExpress platformExpress = new PlatformExpress();
            platformExpress.setPlatformId(platform.getId());
            List<PlatformExpress> platformExpresses = platformExpressService.find(platformExpress);
            for(PlatformExpress pe : platformExpresses){
                Express express = expressService.findOne(pe.getExpressId());
                platformVO.getExpressIds().add(express.getId());
                platformVO.getExpressNames().add(express.getName());
            }
            pageVO.getRows().add(platformVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/queryPlatformsForPlugin", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    List<PlatformVO> queryPlatformsForPlugin(PlatformVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行查询平台信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Platform queryParmas = new Platform();
        queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo.getName());
        queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo.getCode());
        queryParmas.setStatus(StringUtils.isEmpty(vo.getStatus()) ? null : vo.getStatus());
        List<Platform> platforms = platformService.find(queryParmas);
        List<PlatformVO> vos = new ArrayList<PlatformVO>();
        for (Platform platform : platforms) {
            PlatformVO platformVO = new PlatformVO();
            platformVO.setId(platform.getId());
            platformVO.setName(platform.getName());
            platformVO.setCode(platform.getCode());
            vos.add(platformVO);
        }
        return vos;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问平台信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/platform/init";
    }
}
