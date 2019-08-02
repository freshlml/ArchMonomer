package com.freshjuice.fl.dao.base;

import com.freshjuice.fl.dto.base.User;

public interface IUserDao {
	/**
	 * 在k_user中根据userName 查询 userPswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

	/**
	 * 在k_user 中根据userName 查询user
	 * @param username
	 * @return
	 */
    User getUserByUn(String username);

	/**
	 * 在k_user 中根据phone 查询user
	 * @param phone
	 * @return
	 */
    User getUserByPhone(String phone);
}
