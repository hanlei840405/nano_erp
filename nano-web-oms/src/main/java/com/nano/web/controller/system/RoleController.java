package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.nano.domain.system.Privilege;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.service.system.PrivilegeService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.RoleVO;

@Controller
@RequestMapping("system/role")
public class RoleController {
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String init() {
		return "system/role/init";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	ResponseVO<RoleVO> save(RoleVO roleVO, HttpServletRequest request) {
		ResponseVO<RoleVO> responseVO = new ResponseVO<RoleVO>();
		Role role;
		try{
			List<String> privileges = mapper.readValue(request.getParameter("privileges"), new TypeReference<List<String>>() {});

			if (!StringUtils.isEmpty(roleVO.getId())) {
				role = roleService.get(roleVO.getId());
			}else{
				role = new Role();
			}
			BeanUtils.copyProperties(roleVO, role,"id","creator","modifier");
			List<Privilege> privilegeList = new ArrayList<>();
			for(String id : privileges){
				Privilege privilege = privilegeService.get(id);
				privilegeList.add(privilege);
			}
			User creator = userService.get(SecurityUtils.getSubject()
					.getPrincipal().toString());
			role.setCreator(creator.getId());
			role.setPrivileges(privilegeList);
			if (!StringUtils.isEmpty(roleVO.getId())) {
				roleService.update(role);
			}else{
				roleService.save(role);
			}
			responseVO.setYesOrNo(true);
			return responseVO;
		}catch(Exception e){
			logger.error(Marker.ANY_MARKER,e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public @ResponseBody ResponseVO<RoleVO> delete(@RequestParam("ids[]") List<String> ids) {
		ResponseVO<RoleVO> responseVO = new ResponseVO<RoleVO>();
		try{
			roleService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		}catch (Exception e){
			logger.error(Marker.ANY_MARKER,e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequiresPermissions(value = "queryRoles")
	@RequestMapping(value = "/queryRoles", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	PageVO<RoleVO> queryRoles(String name,@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		PageInfo<Role> roles = roleService.queryRoles(name,page -1,rows);
		PageVO<RoleVO> vos = new PageVO<>();
		vos.setTotal(roles.getTotal());
		for(Role role :roles.getList() ){
			RoleVO vo = new RoleVO();
			BeanUtils.copyProperties(role, vo);
			vos.getRows().add(vo);
		}
		return vos;
	}

	@RequestMapping(value = "/queryRolesForPlugin", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	List<RoleVO> queryRolesForPlugin(String userId,String groupId) {
		Iterable<Role> roles = roleService.queryAllRoles();
		List<Role> roleList = roleService.queryRoles(userId, groupId);
		List<RoleVO> vos = new ArrayList<>();
		for(Role role :roles ){
			RoleVO vo = new RoleVO();
			BeanUtils.copyProperties(role, vo);
			if(roleList.contains(role)){
				vo.getAttributes().put("checked",true);
			}else{
				vo.getAttributes().put("checked",false);
			}
			vos.add(vo);
		}
		return vos;
	}
}
