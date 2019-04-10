package com.freshjuice.fl.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshjuice.fl.dao.user.IUserDao;
import com.freshjuice.fl.service.user.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Override
	public String getPswdOfUserByUn(String principal) {
		return userDao.getPswdOfUserByUn(principal);
	}

}
