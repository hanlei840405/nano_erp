package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
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

import com.github.pagehelper.PageInfo;
import com.nano.constant.Constant;
import com.nano.domain.system.Department;
import com.nano.domain.system.Group;
import com.nano.domain.system.Privilege;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.service.system.DepartmentService;
import com.nano.service.system.GroupService;
import com.nano.service.system.PrivilegeService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;
import com.nano.web.security.PasswordHelper;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.UserVO;

@Controller
@RequestMapping("system/user")
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);
	private PasswordHelper passwordHelper = new PasswordHelper();
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(value = "/userAdd", method = RequestMethod.GET)
	public String userAdd() {
		User user = new User();
		user.setCode("admin");
		user.setPassword("admin");
		user.setLocked(false);
		passwordHelper.encryptPassword(user);
		// user = EndecryptUtils.md5Password(user.getCode(),
		// user.getPassword());
		Role role1 = new Role();
		role1.setCode("admin");
		role1.setName("超级管理员");
		Group group1 = new Group();
		group1.setCode("aaa");
		group1.setName("AAA");
		group1.getRoles().add(role1);
		group1.getUsers().add(user);

		Privilege privilege1 = new Privilege();
		privilege1.setCode("queryUsers");
		privilege1.setName("查看用户");
		role1.getPrivileges().add(privilege1);
		user.getRoles().add(role1);

		Privilege privilege2 = new Privilege();
		privilege2.setCode("queryRoles");
		privilege2.setName("查看角色");
		role1.getPrivileges().add(privilege2);

		Privilege privilege3 = new Privilege();
		privilege3.setCode("querySystems");
		privilege3.setName("查看系统管理");
		role1.getPrivileges().add(privilege3);

		Privilege privilege4 = new Privilege();
		privilege4.setCode("queryDepartments");
		privilege4.setName("查看部门信息");
		role1.getPrivileges().add(privilege4);

		Privilege privilege5 = new Privilege();
		privilege5.setCode("queryDepartments");
		privilege5.setName("查看用户信息");
		role1.getPrivileges().add(privilege5);
		
		
		privilegeService.save(privilege1);
		privilegeService.save(privilege2);
		privilegeService.save(privilege3);
		privilegeService.save(privilege4);
		privilegeService.save(privilege5);
		
		roleService.save(role1);
		userService.save(user);
		groupService.save(group1);
		
		return "login";
	}
	@RequestMapping(value = "/queryUsers", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody
	PageVO<UserVO> queryUsers(String code,String departmentId, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		PageInfo<User> users = userService.queryUsers(code,departmentId, page - 1, rows);
		PageVO<UserVO> vos = new PageVO<>();
		vos.setTotal(users.getTotal());
		for(User user :users.getList() ){
			UserVO vo = new UserVO();
			BeanUtils.copyProperties(user, vo);
			vo.setLocked(String.valueOf(user.getLocked()));
			if(!StringUtils.isEmpty(user.getDepartmentId())){	
				Department department = departmentService.get(user.getDepartmentId());
				vo.setDepartmentId(department.getId());
				vo.setDepartmentName(department.getName());
			}
			vos.getRows().add(vo);
		}
		return vos;
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	ResponseVO<UserVO> save(UserVO userVO) {
		ResponseVO<UserVO> responseVO = new ResponseVO<UserVO>();
		User user;
		try{
			if (!StringUtils.isEmpty(userVO.getId())) {
				user = userService.get(userVO.getId());
			}else{
				user = new User();
			}
			BeanUtils.copyProperties(userVO, user,"id","creator","modifier");
			for(String id : userVO.getRoleIds()){
				Role role = roleService.get(id);
				user.getRoles().add(role);
			}
			user.setLocked(new Boolean(userVO.getLocked()));
			user.setDepartmentId(userVO.getDepartmentId());
			if(StringUtils.isEmpty(user.getPassword())){
				user.setPassword("123456");
				passwordHelper.encryptPassword(user);
			}
			User creator = userService.get(SecurityUtils.getSubject()
					.getPrincipal().toString());
			user.setCreator(creator.getId());
			if (!StringUtils.isEmpty(userVO.getId())) {
				userService.update(user);
			}else{
				userService.save(user);
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
	public @ResponseBody ResponseVO<UserVO> delete(@RequestParam("ids[]") List<String> ids) {
		ResponseVO<UserVO> responseVO = new ResponseVO<UserVO>();
		try{
			userService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		}catch (Exception e){
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		return "system/user/init";
	}

	@RequestMapping(value = "/queryUsersForPlugin", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody
	List<Map<String,Object>> queryUsersForPlugin(String groupId,String id) {
		List<Map<String,Object>> vos = new ArrayList<>();
		if(id != null){
			return vos;
		}
		List<User> users = userService.queryUsersByGroup(groupId);
		List<Department> roots = departmentService.queryRootDepartment();
		User user = new User(); // 查询使用
		for(Department root : roots){
			Map<String, Object> result = new HashMap<>();
			result.put("id", root.getId());
			result.put("code", root.getCode());
			result.put("text", root.getName());
			result.put("position", root.getPosition());
			result.put("state", Constant.CLOSED);
			result.put("parentId", null);
			List<Map<String, Object>> children = initDepartmentAndUserData(root, users);
			user.setDepartmentId(root.getId());
			List<User> usersOfDepartment = userService.queryUsers(user);
			for(User u : usersOfDepartment){
				Map<String, Object> userResult = new HashMap<>();
				if(users.contains(u)){
					userResult.put("checked", true);
					users.remove(u);
				}
				userResult.put("id", u.getId());
				userResult.put("code", u.getCode());
				userResult.put("text", u.getName());
				userResult.put("departmentName",root.getName());
				userResult.put("iconCls","icon-man");
				children.add(userResult);
			}
			result.put("children", children);
			vos.add(result);
		}
		return vos;
	}

	private List<Map<String, Object>> initDepartmentAndUserData(Department parent,List<User> users) {
		List<Department> departments = departmentService.queryDepartments(parent.getId());
		List<Map<String, Object>> results = new ArrayList<>();
		if (!departments.isEmpty()) {
			User user = new User(); // 查询使用
			for (Department department : departments) {
				Map<String, Object> departmentResult = new HashMap<>();
				departmentResult.put("id", department.getId());
				departmentResult.put("code", department.getCode());
				departmentResult.put("text", department.getName());
				departmentResult.put("position", department.getPosition());
				departmentResult.put("state", Constant.CLOSED);
				departmentResult.put("parentId",parent.getId());
				List<Map<String, Object>> children = initDepartmentAndUserData(department,users);
				user.setDepartmentId(department.getId());
				List<User> usersOfDepartment = userService.queryUsers(user);
				for(User u : usersOfDepartment){
					Map<String, Object> userResult = new HashMap<>();
					if(users.contains(u)){
						userResult.put("checked", true);
						users.remove(u);
					}
					userResult.put("id", u.getId());
					userResult.put("code", u.getCode());
					userResult.put("text", u.getName());
					userResult.put("iconCls","icon-man");
					children.add(userResult);
				}
				departmentResult.put("children", children);
				results.add(departmentResult);
			}
		}
		return results;
	}
}
