package com.freshjuice.fl.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshjuice.fl.dao.base.IUserDao;
import com.freshjuice.fl.service.base.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Override
	public String getPswdOfUserByUn(String principal) {
		return userDao.getPswdOfUserByUn(principal);
	}

}