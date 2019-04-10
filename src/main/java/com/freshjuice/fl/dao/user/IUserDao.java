package com.freshjuice.fl.dao.user;

public interface IUserDao {
	/**
	 * 在k_user中根据userName 查询 userPswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

}
