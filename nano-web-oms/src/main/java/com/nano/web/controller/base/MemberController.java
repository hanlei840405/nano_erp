package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Member;
import com.nano.service.base.MemberService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.MemberVO;
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

import java.util.List;

@Controller
@RequestMapping("base/member")
public class MemberController {
    private static final Logger logger = LoggerFactory
            .getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<MemberVO> save(MemberVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存会员信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<MemberVO> responseVO = new ResponseVO<>();
        Member member;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
            	member = memberService.findOne(vo.getId());
                member.setModifier(session.getAttribute("userId").toString());
            } else {
            	member = new Member();
                member.setCreator(session.getAttribute("userId").toString());
            }
            BeanUtils.copyProperties(vo, member, "id","outlay","vantages","creator","modifier");
            if (!StringUtils.isEmpty(vo.getId())) {
            	memberService.update(member);
            } else {
            	memberService.save(member);
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
    ResponseVO<MemberVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除会员信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<MemberVO> responseVO = new ResponseVO<>();
        try {
        	memberService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryMembers", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<MemberVO> queryMemberes(MemberVO vo,@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询会员信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Member queryParam = new Member();
        BeanUtils.copyProperties(vo, queryParam);
    	PageInfo<Member> pageInfo = memberService.find(queryParam, page - 1, rows);
        PageVO<MemberVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Member member : pageInfo.getList()) {
            MemberVO memberVO = new MemberVO();
            memberVO.setId(member.getId());
            BeanUtils.copyProperties(member, memberVO);
            pageVO.getRows().add(memberVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问会员信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/member/init";
    }
}
