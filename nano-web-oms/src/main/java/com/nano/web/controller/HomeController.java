package com.nano.web.controller;

import java.util.Locale;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nano.domain.system.Department;
import com.nano.domain.system.User;
import com.nano.service.system.DepartmentService;
import com.nano.service.system.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private DepartmentService departmentService;
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		Subject subject = SecurityUtils.getSubject();
		User user = userService.get(subject.getPrincipal().toString());
		logger.info(String.format("欢迎%s【%s】进入NANO-OMS系统.", user.getName(),user.getCode()));
		Session session = subject.getSession();
		session.setAttribute("userId", user.getId());
		session.setAttribute("username", user.getName());
		Department department = departmentService.get(user.getDepartmentId());
		session.setAttribute("departmentName", department.getName());
		return "index";
	}

}
