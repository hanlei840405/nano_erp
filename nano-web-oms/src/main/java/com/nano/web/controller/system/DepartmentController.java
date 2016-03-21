package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import com.nano.util.UUID;
import org.apache.shiro.SecurityUtils;
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

import com.nano.domain.system.Department;
import com.nano.domain.system.User;
import com.nano.service.system.DepartmentService;
import com.nano.service.system.UserService;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.DepartmentVO;

@Controller
@RequestMapping("system/department")
public class DepartmentController {
	private static final Logger logger = LoggerFactory
			.getLogger(DepartmentController.class);
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<DepartmentVO> save(DepartmentVO vo) {
		ResponseVO<DepartmentVO> responseVO = new ResponseVO<DepartmentVO>();
		Department department;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				department = departmentService.get(vo.getId());
			} else {
				department = new Department();
			}
			User creator = userService.get(SecurityUtils.getSubject()
					.getPrincipal().toString());
			department.setCreator(creator.getId());
			department.setName(vo.getName());
			department.setPosition(vo.getPosition());
			if(!StringUtils.isEmpty(vo.getParentId())){
				Department parent = departmentService.get(vo.getId());
				department.setParentId(parent.getId());
				department.setCode(parent.getCode() + "-" + UUID.generateShortUuid());
			}else{
				department.setCode(UUID.generateShortUuid());
			}
			if (!StringUtils.isEmpty(vo.getId())) {
				departmentService.update(department);
			} else {
				departmentService.save(department);
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
	public @ResponseBody ResponseVO<DepartmentVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		ResponseVO<DepartmentVO> responseVO = new ResponseVO<DepartmentVO>();
		try {
			departmentService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryDepartments", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<DepartmentVO> queryDepartments() {
		List<Department> rootDepartments = departmentService
				.queryRootDepartment();
		List<DepartmentVO> vos = initDepartmentTreeGrid(rootDepartments);
		return vos;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		return "system/department/init";
	}

	@RequestMapping(value = "/departmentTree", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String departmentTree() {
		return "system/department/component/department_tree";
	}

	private List<DepartmentVO> initDepartmentTreeGrid(
			List<Department> departments) {
		List<DepartmentVO> vos = new ArrayList<DepartmentVO>();
		for (Department department : departments) {
			DepartmentVO vo = new DepartmentVO();
			vo.setId(department.getId());
			vo.setText(department.getName());
			vo.setName(department.getName());
			vo.setPosition(department.getPosition());
			vo.setParentId(department.getParentId());
			List<Department> items = departmentService
					.queryDepartments(department.getId());
			vo.setChildren(initDepartmentTreeGrid(items));
			vos.add(vo);
		}
		return vos;
	}
}
