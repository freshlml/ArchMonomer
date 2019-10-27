package com.freshjuice.fl.base.service;

import java.util.List;

import com.freshjuice.fl.base.entity.PriorityResource;

public interface IResourceService {
	/**
	 * 根据path查询该资源是否需要认证
	 * @param path
	 * @return
	 */
	String getFAuthOfPath(String path);
	/**
	 * 根据pricipal查询该用户的所有权限
	 * @param pricipal
	 * @return
	 */
	List<String> getPermissionsOfUserByUn(String pricipal);
	/**
	 * 根据path查询资源详情
	 * @param pathWithinApplication
	 * @return
	 */
	PriorityResource getFResourceOfPath(String pathWithinApplication);

}
