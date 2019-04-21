package com.freshjuice.fl.dao.base;

import java.util.List;

import com.freshjuice.fl.dto.base.PriorityResource;

public interface IResourceDao {
	/**
	 * 根据path 查询 k_priority_resource表的authf字段
	 * @param path
	 * @return
	 */
	String getFAuthOfPath(String path);
	/**
	 * 根据pricipal从
	 * k_user k_role k_user_role k_priority_resource k_role_priority
	 * 查询用户的权限名称
	 * @param pricipal
	 * @return
	 */
	List<String> getPermissionsOfUserByUn(String pricipal);
	/**
	 * 根据path 查询 k_priority_resource表的resource信息
	 * @param pathWithinApplication
	 * @return
	 */
	PriorityResource getResourceOfPath(String pathWithinApplication);
	
}
