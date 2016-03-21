package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nano.domain.system.Group;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.service.system.GroupService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;
import com.nano.util.UUID;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.GroupVO;

@Controller
@RequestMapping("system/group")
public class GroupController {
	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		return "system/group/init";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<GroupVO> save(GroupVO vo,
			HttpServletRequest request) {
		ResponseVO<GroupVO> responseVO = new ResponseVO<GroupVO>();
		Group group;
		try {
			List<String> roleIds = mapper.readValue(
					request.getParameter("roles"),
					new TypeReference<List<String>>() {
					});
			List<String> userIds = mapper.readValue(
					request.getParameter("users"),
					new TypeReference<List<String>>() {
					});
			if (!StringUtils.isEmpty(vo.getId())) {
				group = groupService.get(vo.getId());
			} else {
				group = new Group();
			}
			User creator = userService.get(SecurityUtils.getSubject()
					.getPrincipal().toString());
			group.setCreator(creator.getId());
			if(!StringUtils.isEmpty(vo.getParentId())){
				Group parent = groupService.get(vo.getParentId());
				group.setCode(parent.getCode() + "" + UUID.generateShortUuid());
				group.setParentId(vo.getParentId());
			}else{
				group.setCode(UUID.generateShortUuid());
			}
			group.setName(vo.getName());
			List<Role> roles = new ArrayList<>();
			for (String id : roleIds) {
				Role role = roleService.get(id);
				roles.add(role);
			}
			group.setRoles(roles);
			List<User> users = new ArrayList<>();
			for (String id : userIds) {
				User user = userService.get(id);
				users.add(user);
			}
			group.setUsers(users);
			if (!StringUtils.isEmpty(vo.getId())) {
				groupService.update(group);
			} else {
				groupService.save(group);
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

	@RequestMapping(value = "/queryGroups", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<GroupVO> queryGroups() {
		List<GroupVO> vos = initGroupTreeGrid(groupService.queryRoots());
		return vos;
	}

	private List<GroupVO> initGroupTreeGrid(List<Group> groups) {
		List<GroupVO> vos = new ArrayList<>();
		for (Group group : groups) {
			GroupVO vo = new GroupVO();
			vo.setId(group.getId());
			vo.setText(group.getName());
			vo.setName(group.getName());
			vo.setCode(group.getCode());
			vo.setParentId(group.getParentId());
			List<Group> items = groupService.queryGroupsByParent(group.getId());
			vo.setChildren(initGroupTreeGrid(items));
			vos.add(vo);
		}
		
		return vos;
	}
}
