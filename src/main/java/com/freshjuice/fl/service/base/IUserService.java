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

}
