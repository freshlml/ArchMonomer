package com.freshjuice.fl.service.base;

import com.freshjuice.fl.dto.base.User;

public interface IUserService {
	/**
	 * 根据username获取pswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

	/**
	 * 根据username查询User
	 * @param username
	 * @return
	 */
    User getUserByUn(String username);

	/**
	 * 根据phone查询user
	 * @param phone
	 * @return
	 */
    User getUserByPhone(String phone);

	/**
	 * 根据id 查询User
	 * @param id
	 * @return
	 */
	public User getUserById(String id);

	/**
	 * 根据id删除user real del
	 * @param id
	 */
	public void removeUserById(String id);

}
