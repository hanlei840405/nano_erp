package com.nano.web.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.nano.domain.system.Group;
import com.nano.domain.system.Privilege;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.service.system.GroupService;
import com.nano.service.system.PrivilegeService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;

public class OmsRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private PrivilegeService privilegeService;

	@Autowired
	private RoleService roleService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = userService.get(username);
		Set<Role> roles = new HashSet<>();
		roles.addAll(user.getRoles());
		List<Group> groups = new ArrayList<>(0);
		for (Group group : user.getGroups()) {
			groups.addAll(groupService.queryGroups(group.getCode()));
		}

		for (Group group : user.getGroups()) {
			roles.addAll(roleService.queryRoles(null, group.getId()));
		}
		for (Role role : roles) {
			List<Privilege> privileges = privilegeService.queryPrivileges(role.getId());
			for (Privilege privilege : privileges) {
				authorizationInfo.addStringPermission(privilege.getCode());
			}
			// authorizationInfo.addRole(role.getCode());
		}
		// authorizationInfo.setStringPermissions(userService
		// .findPermissions(username));

		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		String username = (String) token.getPrincipal();

		User user = userService.get(username);

		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}

		if (Boolean.TRUE.equals(user.getLocked())) {
			throw new LockedAccountException(); // 帐号锁定
		}
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				token.getPrincipal(),
				user.getPassword(), // 密码
				new OmsSimpleByteSource(user.getCredentialsSalt().getBytes()),// salt=username+salt
				getName() // realm name
		);
		return authenticationInfo;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -1748602382963711884L;
		private Long id;
		private String code;

		public ShiroUser(Long id, String code) {
			this.id = id;
			this.code = code;
		}

		public Long getId() {
			return id;
		}

		public String getCode() {
			return code;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return this.code;
		}
	}
}
