package com.freshjuice.fl.base.mapper.impl;

import com.freshjuice.fl.base.entity.User;
import org.springframework.stereotype.Repository;

import com.freshjuice.fl.dao.FlBaseDao;
import com.freshjuice.fl.base.mapper.IUserDao;

@Repository("userDao")
public class UserDaoImpl extends FlBaseDao implements IUserDao {

	@Override
	public String getPswdOfUserByUn(String principal) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IUserDao.getPswdOfUserByUn", principal);
	}

	@Override
	public User getUserByUn(String username) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IUserDao.getUserByUn", username);
	}

	@Override
	public User getUserByPhone(String phone) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IUserDao.getUserByPhone", phone);
	}

	@Override
	public User getUserById(String id) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IUserDao.getUserById", id);
	}

	@Override
	public void delUserById(String id) {
		this.getSqlSession().delete("com.freshjuice.fl.base.mapper.IUserDao.delUserById", id);
	}

	@Override
	public void updateUser(User user) {
		this.getSqlSession().update("com.freshjuice.fl.base.mapper.IUserDao.updateUser", user);
	}

}
