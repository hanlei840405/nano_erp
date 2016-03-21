package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Note;
import com.nano.service.base.NoteService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.NoteVO;
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
@RequestMapping("base/note")
public class NoteController {
    private static final Logger logger = LoggerFactory
            .getLogger(NoteController.class);
    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<NoteVO> save(NoteVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存备注信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<NoteVO> responseVO = new ResponseVO<>();
        Note note;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
            	note = noteService.findOne(vo.getId());
                note.setModifier(session.getAttribute("userId").toString());
            } else {
            	note = new Note();
                note.setCreator(session.getAttribute("userId").toString());
            }
            note.setContent(vo.getContent());
            if (!StringUtils.isEmpty(vo.getId())) {
            	noteService.update(note);
            } else {
            	noteService.save(note);
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
    ResponseVO<NoteVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除备注信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<NoteVO> responseVO = new ResponseVO<>();
        try {
        	noteService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryNotes", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<NoteVO> queryNotees(@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询备注信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        PageInfo<Note> pageInfo = noteService.find( page - 1, rows);
        PageVO<NoteVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Note note : pageInfo.getList()) {
            NoteVO noteVO = new NoteVO();
            noteVO.setId(note.getId());
            noteVO.setContent(note.getContent());
            pageVO.getRows().add(noteVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问备注信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/note/init";
    }
}
