package com.freshjuice.fl.service.base;

public interface IUserService {
	/**
	 * 根据username获取pswd
	 * @param principal
	 * @return
	 */
	String getPswdOfUserByUn(String principal);

}
