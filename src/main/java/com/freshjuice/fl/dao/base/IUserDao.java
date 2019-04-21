package com.freshjuice.fl.dao.base;

public interface IUserDao {
	/**
	 * 在k_user中根据userName 查询 userPswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

}
