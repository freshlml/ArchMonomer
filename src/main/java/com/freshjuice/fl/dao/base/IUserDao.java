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

	/**
	 * 在k_user 中根据id 查询user
	 * @param id
	 * @return
	 */
	User getUserById(String id);

	/**
	 * 在k_user 中根据id 删除user real del
	 * @param id
	 */
	void delUserById(String id);
}
