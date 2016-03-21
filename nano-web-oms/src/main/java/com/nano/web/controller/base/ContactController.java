package com.nano.web.controller.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Contact;
import com.nano.domain.system.User;
import com.nano.service.base.ContactService;
import com.nano.service.system.DepartmentService;
import com.nano.service.system.UserService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.ContactVO;
import com.nano.web.vo.system.UserVO;

@Controller
@RequestMapping("base/contact")
public class ContactController {
    private static final Logger logger = LoggerFactory
            .getLogger(ContactController.class);
    @Autowired
    private ContactService contactService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<ContactVO> save(ContactVO vo,HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存商品负责人信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<ContactVO> responseVO = new ResponseVO<>();
        Contact contact;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
                contact = contactService.findOne(vo.getId());
                contact.setModifier(session.getAttribute("userId").toString());
            } else {
                contact = new Contact();
                contact.setCreator(session.getAttribute("userId").toString());
            }
            BeanUtils.copyProperties(vo, contact, "id");
            List<String> inserts = mapper.readValue(
                    request.getParameter("inserts"),
                    new TypeReference<List<String>>() {
                    });
            contact.setUserIds(inserts);
            if (!StringUtils.isEmpty(vo.getId())) {
                contactService.update(contact);
            } else {
                contactService.save(contact);
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
    ResponseVO<ContactVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除商品负责人信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<ContactVO> responseVO = new ResponseVO<>();
        try {
            contactService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryContacts", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<ContactVO> queryContacts(ContactVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询商品负责人信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Contact queryParmas = new Contact();
        queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo.getName());
        queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo.getCode());
        PageInfo<Contact> pageInfo = contactService.find(queryParmas, page - 1, rows);
        PageVO<ContactVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Contact contact : pageInfo.getList()) {
            ContactVO contactVO = new ContactVO();
            contactVO.setId(contact.getId());
            contactVO.setName(contact.getName());
            contactVO.setCode(contact.getCode());
            for(String userId : contact.getUserIds()){
            	User user = userService.get(userId);
            	contactVO.getUserIds().add(userId);
            	contactVO.getUserNames().add(user.getName());
            }
            pageVO.getRows().add(contactVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/queryAllContacts", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    List<ContactVO> queryAllContacts(ContactVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行查询商品负责人信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Contact queryParmas = new Contact();
        List<Contact> contacts = contactService.find(queryParmas);
        List<ContactVO> vos = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactVO contactVO = new ContactVO();
            contactVO.setId(contact.getId());
            contactVO.setName(contact.getName());
            contactVO.setCode(contact.getCode());
            for(String userId : contact.getUserIds()){
                User user = userService.get(userId);
                contactVO.getUserIds().add(userId);
                contactVO.getUserNames().add(user.getName());
            }
            vos.add(contactVO);
        }
        return vos;
    }

    @RequestMapping(value = "/queryContactUsers", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    List<UserVO> queryContactUsers(String contactId) {
        Contact queryParmas = new Contact();
        queryParmas.setId(contactId);
        Contact contact = contactService.findOne(contactId);
        
        List<UserVO> vos = new ArrayList<>();
        for (String userId : contact.getUserIds()) {
        	UserVO userVO = new UserVO();
        	User user = userService.get(userId);
        	userVO.setId(user.getId());
        	userVO.setName(user.getName());
        	userVO.setDepartmentName(departmentService.get(user.getDepartmentId()).getName());
        	vos.add(userVO);
        }
        return vos;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        return "base/contact/init";
    }
}
