package com.freshjuice.fl.service.user;

public interface IUserService {
	/**
	 * 根据username获取pswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

}
