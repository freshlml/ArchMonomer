package com.freshjuice.fl.dao.base.impl;

import org.springframework.stereotype.Repository;

import com.freshjuice.fl.dao.FlBaseDao;
import com.freshjuice.fl.dao.base.IUserDao;

@Repository("userDao")
public class UserDaoImpl extends FlBaseDao implements IUserDao {

	@Override
	public String getPswdOfUserByUn(String principal) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IUserDao.getPswdOfUserByUn", principal);
	}

}
