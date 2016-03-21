package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.List;

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
import com.nano.domain.system.Element;
import com.nano.domain.system.Function;
import com.nano.domain.system.Menu;
import com.nano.domain.system.Privilege;
import com.nano.service.system.ElementService;
import com.nano.service.system.FunctionService;
import com.nano.service.system.MenuService;
import com.nano.service.system.PrivilegeService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.PrivilegeVO;

@Controller
@RequestMapping("system/privilege")
public class PrivilegeController {
	private static final Logger logger = LoggerFactory
			.getLogger(PrivilegeController.class);

	@Autowired
	private PrivilegeService privilegeService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private FunctionService functionService;

	@Autowired
	private ElementService elementService;

	private String categoryDefault = "MENU";

	private String categoryMenu = "MENU";

	private String categoryFunction = "FUNCTION";

	private String categoryElement = "ELEMENT";

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String init() {
		return "system/privilege/init";
	}

	@RequestMapping(value = "/privilegeAccording", method = RequestMethod.GET)
	public String privilegeAccording() {
		return "system/privilege/component/privilege_according";
	}

	@RequestMapping(value = "/queryPrivileges", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody PageVO<PrivilegeVO> queryPrivileges(String mceCode,
			String roleId, @RequestParam(required = true) int page,
			@RequestParam(required = true) int rows) {
		String category = null;
		String menuCode = null;
		String functionCode = null;
		String elementCode = null;
		if (!StringUtils.isEmpty(mceCode)) {
			if (mceCode.startsWith("@MENU")) {
				category = categoryMenu;
				menuCode = mceCode.replace("@MENU", "");
			} else if (mceCode.startsWith("@FUNCTION")) {
				category = categoryFunction;
				functionCode = mceCode.replace("@FUNCTION", "");
			} else if (mceCode.startsWith("@ELEMENT")) {
				category = categoryElement;
				elementCode = mceCode.replace("@ELEMENT", "");
			}
		} else {
			category = categoryDefault;
		}
		PageInfo<Privilege> privileges = privilegeService.queryPrivileges(
				menuCode, functionCode, elementCode, category, page - 1, rows); // 分页查找权限
		PageVO<PrivilegeVO> vos = new PageVO<>();
		vos.setTotal(privileges.getTotal());
		for (Privilege privilege : privileges.getList()) {
			PrivilegeVO vo = new PrivilegeVO();
			BeanUtils.copyProperties(privilege, vo);
			String[] relatedIds;
			if(!privilege.getMenus().isEmpty()){
				relatedIds = new String[privilege.getMenus().size()];
				for(int i = 0;i < privilege.getMenus().size();i++){
					Menu menu = privilege.getMenus().get(i);
					relatedIds[i] = menu.getId();
				}
			}else if(!privilege.getFunctions().isEmpty()){
				relatedIds = new String[privilege.getFunctions().size()];
				for(int i = 0;i < privilege.getFunctions().size();i++){
					Function function = privilege.getFunctions().get(i);
					relatedIds[i] = function.getId();
				}
			}else if(!privilege.getElements().isEmpty()){
				relatedIds = new String[privilege.getElements().size()];
				for(int i = 0;i < privilege.getElements().size();i++){
					// TODO 添加资源权限信息
				}
			}else{
				relatedIds = new String[0];
			}
			vo.setRelatedIds(relatedIds);
			vos.getRows().add(vo);
		}
		return vos;
	}

	@RequestMapping(value = "/queryPrivilegesForPlugin", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<PrivilegeVO> queryPrivilegesForPlugin(
			String roleId) {
		Iterable<Privilege> privileges = privilegeService.queryAllPrivileges(); // 分页查找权限
		List<Privilege> privilegeList = privilegeService
				.queryPrivileges(roleId); // 角色具有的权限
		List<PrivilegeVO> vos = new ArrayList<>();
		for (Privilege privilege : privileges) {
			PrivilegeVO vo = new PrivilegeVO();
			BeanUtils.copyProperties(privilege, vo);
			if (privilegeList.contains(privilege)) {
				vo.getAttributes().put("checked", true);
			} else {
				vo.getAttributes().put("checked", false);
			}
			vos.add(vo);
		}

		return vos;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<PrivilegeVO> save(PrivilegeVO privilegeVO) {
		ResponseVO<PrivilegeVO> responseVO = new ResponseVO<PrivilegeVO>();
		Privilege privilege;
		try {
			if (!StringUtils.isEmpty(privilegeVO.getId())) {
				privilege = privilegeService.get(privilegeVO.getId());
			} else {
				privilege = new Privilege();
			}
			BeanUtils.copyProperties(privilegeVO, privilege, "id", "creator",
					"modifier");
			if (categoryMenu.equals(privilegeVO.getCategory())) {
				for (String relateId : privilegeVO.getRelatedIds()) {
					Menu menu = menuService.get(relateId);
					privilege.getMenus().add(menu);
				}
			}
			if (categoryFunction.equals(privilegeVO.getCategory())) {
				for (String relateId : privilegeVO.getRelatedIds()) {
					Function function = functionService.get(relateId);
					privilege.getFunctions().add(function);
				}
			}
			if (categoryElement.equals(privilegeVO.getCategory())) {
				for (String relateId : privilegeVO.getRelatedIds()) {
					Element element = elementService.get(relateId);
					privilege.getElements().add(element);
				}
			}
			if (!StringUtils.isEmpty(privilegeVO.getId())) {
				privilegeService.update(privilege);
			} else {
				privilegeService.save(privilege);
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
	public @ResponseBody ResponseVO<PrivilegeVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		ResponseVO<PrivilegeVO> responseVO = new ResponseVO<PrivilegeVO>();
		try {
			privilegeService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}
}
