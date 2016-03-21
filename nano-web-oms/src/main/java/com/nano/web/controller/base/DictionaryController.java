package com.nano.web.controller.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Dictionary;
import com.nano.domain.base.DictionaryItem;
import com.nano.service.base.DictionaryItemService;
import com.nano.service.base.DictionaryService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.DictionaryItemVO;
import com.nano.web.vo.base.DictionaryVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.BeanUtils;
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

@Controller
@RequestMapping("base/dictionary")
public class DictionaryController {
    private static final Logger logger = LoggerFactory
            .getLogger(DictionaryController.class);
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private DictionaryItemService dictionaryItemService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<DictionaryVO> save(DictionaryVO vo,HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存字典信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<DictionaryVO> responseVO = new ResponseVO<>();
        Dictionary dictionary;
        try {
            if(StringUtils.isEmpty(vo.getId())){
                dictionary = new Dictionary();
                dictionary.setCreator(session.getAttribute("userId").toString());
                BeanUtils.copyProperties(vo, dictionary, "id");
            }else{
                dictionary = dictionaryService.findOne(vo.getId());
            }

            List<DictionaryItemVO> saveDictionaryItemVOs = mapper.readValue(
                    request.getParameter("saveFunctions"),
                    new TypeReference<List<DictionaryItemVO>>() {
                    });
            List<DictionaryItemVO> deleteDictionaryItemVOs = mapper.readValue(
                    request.getParameter("deleteFunctions"),
                    new TypeReference<List<DictionaryItemVO>>() {
                    });
            List<DictionaryItem> inserts = new ArrayList<>();
            List<DictionaryItem> updates = new ArrayList<>();
            for (DictionaryItemVO dictionaryItemVO : saveDictionaryItemVOs) {
                DictionaryItem dictionaryItem;
                if (dictionaryItemVO.getId() != null) {
                    dictionaryItem = dictionaryItemService.findOne(dictionaryItemVO.getId());
                    dictionaryItem.setModifier(session.getAttribute("userId").toString());
                    updates.add(dictionaryItem);
                } else {
                    dictionaryItem = new DictionaryItem();
                    dictionaryItem.setCreator(session.getAttribute("userId").toString());
                    inserts.add(dictionaryItem);
                }
                BeanUtils.copyProperties(dictionaryItemVO, dictionaryItem, "id", "creator",
                        "modifier");
                dictionaryItem.setDictionaryId(dictionary.getId());
            }
            List<String> deletes = new ArrayList<>();
            for (DictionaryItemVO dictionaryItemVO : deleteDictionaryItemVOs) {
                deletes.add(dictionaryItemVO.getId());
            }

            if(StringUtils.isEmpty(vo.getId())){
                dictionaryService.save(dictionary,inserts);
            }else {
                dictionaryItemService.update(inserts,updates,deletes);
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
    ResponseVO<DictionaryVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除字典信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<DictionaryVO> responseVO = new ResponseVO<>();
        try {
            dictionaryService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryDictionaries", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<DictionaryVO> queryDictionaries(DictionaryVO vo,@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询字典信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(vo,dictionary,"id");
        PageInfo<Dictionary> pageInfo = dictionaryService.find(dictionary,page - 1,rows);
        PageVO<DictionaryVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for(Dictionary d : pageInfo.getList()){
            DictionaryVO dictionaryVO = new DictionaryVO();
            dictionaryVO.setCode(d.getCode());
            dictionaryVO.setName(d.getName());
            dictionaryVO.setId(d.getId());
            pageVO.getRows().add(dictionaryVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/queryDictionaryItems", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    List<DictionaryItemVO> queryDictionaryItems(String dictionaryId) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行查询字典信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        List<DictionaryItemVO> vos = new ArrayList<>();
        if (StringUtils.isEmpty(dictionaryId)) {
            return vos;
        }
        DictionaryItem queryParam = new DictionaryItem();
        queryParam.setDictionaryId(dictionaryId);
        List<DictionaryItem> dictionaryItems = dictionaryItemService.find(queryParam);
        for(DictionaryItem dictionaryItem : dictionaryItems){
            DictionaryItemVO vo = new DictionaryItemVO();
            vo.setId(dictionaryItem.getId());
            vo.setDictionaryId(dictionaryItem.getDictionaryId());
            vo.setCode(dictionaryItem.getCode());
            vo.setName(dictionaryItem.getName());
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问字典信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/dictionary/init";
    }

}
