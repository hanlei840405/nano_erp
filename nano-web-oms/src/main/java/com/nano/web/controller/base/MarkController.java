package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Mark;
import com.nano.service.base.MarkService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.MarkVO;
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

import java.util.List;

@Controller
@RequestMapping("base/mark")
public class MarkController {
    private static final Logger logger = LoggerFactory
            .getLogger(MarkController.class);
    @Autowired
    private MarkService markService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<MarkVO> save(MarkVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存标记信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<MarkVO> responseVO = new ResponseVO<>();
        Mark mark;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
            	mark = markService.findOne(vo.getId());
                mark.setModifier(session.getAttribute("userId").toString());
            } else {
            	mark = new Mark();
                mark.setCreator(session.getAttribute("userId").toString());
            }
            mark.setContent(vo.getContent());
            if (!StringUtils.isEmpty(vo.getId())) {
            	markService.update(mark);
            } else {
            	markService.save(mark);
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
    ResponseVO<MarkVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除标记信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<MarkVO> responseVO = new ResponseVO<>();
        try {
        	markService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryMarks", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<MarkVO> queryMarkes(@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询标记信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        PageInfo<Mark> pageInfo = markService.find( page - 1, rows);
        PageVO<MarkVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Mark mark : pageInfo.getList()) {
            MarkVO markVO = new MarkVO();
            markVO.setId(mark.getId());
            markVO.setContent(mark.getContent());
            pageVO.getRows().add(markVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问标记信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/mark/init";
    }
}
