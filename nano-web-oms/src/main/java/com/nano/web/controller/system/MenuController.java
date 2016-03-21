package com.nano.web.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
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
import com.nano.constant.Constant;
import com.nano.domain.system.Function;
import com.nano.domain.system.Group;
import com.nano.domain.system.Menu;
import com.nano.domain.system.Privilege;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.service.system.FunctionService;
import com.nano.service.system.MenuService;
import com.nano.service.system.PrivilegeService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;
import com.nano.util.UUID;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.FunctionVO;
import com.nano.web.vo.system.MenuVO;

@Controller
@RequestMapping("system/menu")
public class MenuController {
	private static final Logger logger = LoggerFactory
			.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleService roleService;

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<MenuVO> save(MenuVO menuVO,
			HttpServletRequest request) throws IOException {
		ResponseVO<MenuVO> responseVO = new ResponseVO<MenuVO>();
		User user = userService.get(SecurityUtils.getSubject().getPrincipal()
				.toString());
		Menu menu;
		try {
			if (!StringUtils.isEmpty(menuVO.getId())) {
				menu = menuService.get(menuVO.getId());
				menu.setModifier(user.getId());
			} else {
				menu = new Menu();
				menu.setCreator(user.getId());
			}
			BeanUtils.copyProperties(menuVO, menu, "id", "parentId", "code",
					"creator", "modifier");
			if (StringUtils.isEmpty(menuVO.getParentId())) {
				menu.setCode(UUID.generateShortUuid());
			} else {
				Menu parent = menuService.get(menuVO.getParentId());
				menu.setParentId(menuVO.getParentId());
				menu.setCode(parent.getCode() + "-" + UUID.generateShortUuid());
			}

			List<FunctionVO> saveFunctionVOs = mapper.readValue(
					request.getParameter("saveFunctions"),
					new TypeReference<List<FunctionVO>>() {
					});
			List<FunctionVO> deleteFunctionVOs = mapper.readValue(
					request.getParameter("deleteFunctions"),
					new TypeReference<List<FunctionVO>>() {
					});
			List<Function> inserts = new ArrayList<>();
			List<Function> updates = new ArrayList<>();
			for (FunctionVO vo : saveFunctionVOs) {
				Function function;
				if (!StringUtils.isEmpty(vo.getId())) {
					function = functionService.get(vo.getId());
					function.setModifier(user.getId());
					updates.add(function);
				} else {
					function = new Function();
					function.setCreator(user.getId());
					inserts.add(function);
				}
				BeanUtils.copyProperties(vo, function, "id", "creator",
						"modifier");
				function.setMenuId(menu.getId());
			}
			List<String> deleteFunctions = new ArrayList<>();
			for (FunctionVO vo : deleteFunctionVOs) {
				deleteFunctions.add(vo.getId());
			}
			if (!StringUtils.isEmpty(menuVO.getId())) {
				menuService.update(menu, inserts, updates, deleteFunctions);
			} else {
				menuService.save(menu, inserts, deleteFunctions);
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

	@RequestMapping(value = "/delete", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody ResponseVO<MenuVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		ResponseVO<MenuVO> responseVO = new ResponseVO<MenuVO>();
		try {
			menuService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryMenuByPrivilege", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<MenuVO> queryMenuByPrivilege(String code) {
		if (StringUtils.isEmpty(code)) {
			Subject currentUser = SecurityUtils.getSubject();// 获取当前用户
			code = currentUser.getPrincipal().toString();
		}
		User user = userService.get(code);
		Set<Role> roles = new HashSet<Role>();
		roles.addAll(user.getRoles());
		for (Group group : user.getGroups()) {
			roles.addAll(roleService.queryRoles(null, group.getId()));
		}
		List<Menu> menus = new ArrayList<>();
		for (Role role : roles) {
			List<Privilege> privileges = privilegeService.queryPrivileges(role
					.getId());
			for (Privilege privilege : privileges) {
				menus.addAll(menuService.queryMenuByPrivilege(privilege.getId()));
			}
		}
		List<MenuVO> menuVOs = new ArrayList<>();
		initNavigation(menuVOs, menus);
		return menuVOs;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		return "system/menu/init";
	}

	@RequestMapping(value = "/queryAllMenusAndFunctions", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<MenuVO> queryAllMenusAndFunctions(
			@RequestParam boolean initFunctions) {
		List<Menu> menus = menuService.queryRootMenus();
		List<MenuVO> vos = initMenuAndFunctionTreeData(menus, initFunctions,
				new ArrayList<Function>());
		return vos;
	}

	private List<MenuVO> initMenuAndFunctionTreeData(List<Menu> menus,
			boolean initFunctions, List<Function> functions) {
		List<MenuVO> vos = new ArrayList<>();
		for (Menu menu : menus) {
			MenuVO vo = new MenuVO();
			vo.setId(menu.getId());
			vo.setName(menu.getName());
			vo.setCode(menu.getCode());
			vo.setParentId(menu.getParentId());
			vo.setPosition(menu.getPosition());
			vo.setText(menu.getName());
			vo.setUrl(menu.getUrl());
			List<Menu> menuItems = menuService.queryMenuByParent(menu.getId());
			List<Function> functionItems = new ArrayList<Function>();
			if (initFunctions) {
				functionItems = functionService.queryFunctionsByMenu(menu
						.getId());
			}
			vo.setChildren(initMenuAndFunctionTreeData(menuItems,
					initFunctions, functionItems));
			if (vo.getChildren().isEmpty()) {
				vo.setState(Constant.OPEN);
			} else {
				vo.setState(Constant.CLOSED);
			}
			vos.add(vo);
		}
		for (Function function : functions) {
			MenuVO vo = new MenuVO();
			vo.setId(function.getId());
			vo.setName(function.getName());
			vo.setCode(function.getCode());
			vo.setParentId(function.getMenuId());
			vo.setState(Constant.OPEN);
			vo.setText(function.getName());
			vo.setUrl(function.getUrl());
			vos.add(vo);
		}
		return vos;
	}

	private void initNavigation(List<MenuVO> menuVOs, List<Menu> menus) {
		if (menuVOs.isEmpty()) {
			int size = menus.size();
			for (int i = size - 1; i >= 0; i--) {
				Menu menu = menus.get(i);
				if (StringUtils.isEmpty(menu.getParentId())) {
					MenuVO vo = new MenuVO();
					vo.setId(menu.getId());
					vo.setName(menu.getName());
					vo.setCode(menu.getCode());
					vo.setText(menu.getName());
					vo.setState(Constant.OPEN);
					vo.setUrl(menu.getUrl());
					vo.setPosition(menu.getPosition());
					vo.setParentId(menu.getParentId());
					if (!menuVOs.contains(vo)) {
						menuVOs.add(vo);
					}
					menus.remove(i);
				}
			}
		}
		for (int i = 0; i < menuVOs.size(); i++) {
			MenuVO menuVO = menuVOs.get(i);
			int size = menus.size();
			for (int j = size - 1; j >= 0; j--) {
				Menu menu = menus.get(j);
				if (menuVO.getId().equals(menu.getParentId())) {
					MenuVO vo = new MenuVO();
					vo.setId(menu.getId());
					vo.setName(menu.getName());
					vo.setCode(menu.getCode());
					vo.setText(menu.getName());
					vo.setState(Constant.OPEN);
					List<Function> functions = functionService
							.queryFunctionsByMenu(menu.getId());
					for (Function function : functions) {
						if ("init".equals(function.getCode())) {
							vo.setUrl(function.getUrl());
							break;
						}
					}
					vo.setPosition(menu.getPosition());
					vo.setParentId(menu.getParentId());
					if (!menuVO.getChildren().contains(vo)) {
						menuVO.getChildren().add(vo);
					}
					menus.remove(j);
					continue;
				}
				initNavigation(menuVO.getChildren(), menus);
			}
		}

	}
}
