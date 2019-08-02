package com.freshjuice.fl.dao.base.impl;

import com.freshjuice.fl.dto.base.User;
import org.springframework.stereotype.Repository;

import com.freshjuice.fl.dao.FlBaseDao;
import com.freshjuice.fl.dao.base.IUserDao;

@Repository("userDao")
public class UserDaoImpl extends FlBaseDao implements IUserDao {

	@Override
	public String getPswdOfUserByUn(String principal) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IUserDao.getPswdOfUserByUn", principal);
	}

	@Override
	public User getUserByUn(String username) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IUserDao.getUserByUn", username);
	}

	@Override
	public User getUserByPhone(String phone) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IUserDao.getUserByPhone", phone);
	}

}
