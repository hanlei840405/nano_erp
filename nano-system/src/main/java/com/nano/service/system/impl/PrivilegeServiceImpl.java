package com.nano.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Element;
import com.nano.domain.system.Function;
import com.nano.domain.system.Menu;
import com.nano.domain.system.Privilege;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.ElementPrivilegeMapper;
import com.nano.persistence.system.FunctionMapper;
import com.nano.persistence.system.FunctionPrivilegeMapper;
import com.nano.persistence.system.MenuMapper;
import com.nano.persistence.system.MenuPrivilegeMapper;
import com.nano.persistence.system.PrivilegeMapper;
import com.nano.persistence.system.RolePrivilegeMapper;
import com.nano.service.system.PrivilegeService;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	private PrivilegeMapper privilegeMapper;

	@Autowired
	private FunctionPrivilegeMapper functionPrivilegeMapper;

	@Autowired
	private ElementPrivilegeMapper elementPrivilegeMapper;

	@Autowired
	private MenuPrivilegeMapper menuPrivilegeMapper;

	@Autowired
	private RolePrivilegeMapper rolePrivilegeMapper;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private FunctionMapper functionMapper;

	@Transactional
	public Privilege save(Privilege privilege) {
		privilegeMapper.saveOne(privilege);
		List<Map<String, String>> functionPrivileges = new ArrayList<Map<String, String>>();
		for (Function function : privilege.getFunctions()) {
			Map<String, String> functionPrivilege = new HashMap<String, String>();
			functionPrivilege.put("functionId", function.getId());
			functionPrivilege.put("privilegeId", privilege.getId());
			functionPrivileges.add(functionPrivilege);
		}
		if (!functionPrivileges.isEmpty()) {
			functionPrivilegeMapper.saveMany(functionPrivileges);
		}
		List<Map<String, String>> elementPrivileges = new ArrayList<Map<String, String>>();
		for (Element element : privilege.getElements()) {
			Map<String, String> elementPrivilege = new HashMap<String, String>();
			elementPrivilege.put("elementId", element.getId());
			elementPrivilege.put("privilegeId", privilege.getId());
			elementPrivileges.add(elementPrivilege);
		}
		if (!elementPrivileges.isEmpty()) {
			elementPrivilegeMapper.saveMany(elementPrivileges);
		}
		List<Map<String, String>> menuPrivileges = new ArrayList<Map<String, String>>();
		for (Menu menu : privilege.getMenus()) {
			Map<String, String> menuPrivilege = new HashMap<String, String>();
			menuPrivilege.put("menuId", menu.getId());
			menuPrivilege.put("privilegeId", privilege.getId());
			menuPrivileges.add(menuPrivilege);
		}
		if (!menuPrivileges.isEmpty()) {
			menuPrivilegeMapper.saveMany(menuPrivileges);
		}
		return privilege;
	}

	@Transactional
	public List<Privilege> save(List<Privilege> privileges) {
		if(!privileges.isEmpty()){
			privilegeMapper.saveMany(privileges);
		}
		return privileges;
	}

	@Transactional
	public void delete(String id) {
		Privilege privilege = get(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("privilegeId", id);
		functionPrivilegeMapper.deleteMany(params);
		elementPrivilegeMapper.deleteMany(params);
		menuPrivilegeMapper.deleteMany(params);
		rolePrivilegeMapper.deleteMany(params);
		long resultCnt = privilegeMapper.deleteOne(privilege);
		if (resultCnt == 0) {
			throw new NanoDBDeleteRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					privilege.getId() + " : " + privilege.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		Map<String, String> params = new HashMap<String, String>();
		for (String id : ids) {
			params.put("privilegeId", id);
			functionPrivilegeMapper.deleteMany(params);
			elementPrivilegeMapper.deleteMany(params);
			menuPrivilegeMapper.deleteMany(params);
			rolePrivilegeMapper.deleteMany(params);
		}
		privilegeMapper.deleteMany(ids);
	}

	public Privilege get(String id) {
		return privilegeMapper.findOne(id);
	}

	@Transactional
	public void update(Privilege privilege) {
		long updateCnt = privilegeMapper.updateOne(privilege);
		if (updateCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					privilege.getId() + " : " + privilege.getName()));
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("privilegeId", privilege.getId());
		functionPrivilegeMapper.deleteMany(params);
		elementPrivilegeMapper.deleteMany(params);
		menuPrivilegeMapper.deleteMany(params);
		if (!privilege.getFunctions().isEmpty()) {
			List<Map<String, String>> functionPrivileges = new ArrayList<Map<String, String>>();
			for (Function function : privilege.getFunctions()) {
				Map<String, String> functionPrivilege = new HashMap<String, String>();
				functionPrivilege.put("functionId", function.getId());
				functionPrivilege.put("privilegeId", privilege.getId());
				functionPrivileges.add(functionPrivilege);
			}
			functionPrivilegeMapper.saveMany(functionPrivileges);
		}
		if (!privilege.getElements().isEmpty()) {
			List<Map<String, String>> elementPrivileges = new ArrayList<Map<String, String>>();
			for (Element element : privilege.getElements()) {
				Map<String, String> elementPrivilege = new HashMap<String, String>();
				elementPrivilege.put("elementId", element.getId());
				elementPrivilege.put("privilegeId", privilege.getId());
				elementPrivileges.add(elementPrivilege);
			}
			elementPrivilegeMapper.saveMany(elementPrivileges);
		}

		if (!privilege.getMenus().isEmpty()) {
			List<Map<String, String>> menuPrivileges = new ArrayList<Map<String, String>>();
			for (Menu menu : privilege.getMenus()) {
				Map<String, String> menuPrivilege = new HashMap<String, String>();
				menuPrivilege.put("menuId", menu.getId());
				menuPrivilege.put("privilegeId", privilege.getId());
				menuPrivileges.add(menuPrivilege);
			}
			menuPrivilegeMapper.saveMany(menuPrivileges);
		}

	}

	@Transactional
	public void update(List<Privilege> privileges) {
		privilegeMapper.updateMany(privileges);
	}

	public List<Privilege> queryAllPrivileges() {
		return privilegeMapper.findMany(new HashMap<String, String>());
	}

	public PageInfo<Privilege> queryPrivileges(String menuId,
			String functionId, String elementId, String category, int pageNo,
			int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("menuId", menuId);
		params.put("functionId", functionId);
		params.put("elementId", elementId);
		params.put("category", category);
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		List<Privilege> privileges = privilegeMapper
				.findMany(params, rowBounds);
		for(Privilege privilge : privileges){
			List<Menu> menus = menuMapper.findManyByPrivilege(privilge.getId());
			privilge.setMenus(menus);
			List<Function> function = functionMapper.findManyByPrivilege(privilge.getId());
			privilge.setFunctions(function);
		}
		PageInfo<Privilege> pageInfo = new PageInfo<Privilege>(privileges);
		return pageInfo;
	}

	public List<Privilege> queryPrivileges(String roleId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("roleId", roleId);
		return privilegeMapper.findMany(params);
	}

}
