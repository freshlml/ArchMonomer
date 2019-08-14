package com.freshjuice.fl.service.base.impl;

import com.freshjuice.fl.dto.base.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

	@Override
	public User getUserByUn(String username) {
		return userDao.getUserByUn(username);
	}

	@Override
	public User getUserByPhone(String phone) {
		return userDao.getUserByPhone(phone);
	}

	/*
	* spring-data-redis 抽象目前遇到的问题：
	* 1 getUserById 根据id缓存User,getUserByName 根据name缓存User？ 这样将存在两个相同value的key,则当User中某些字段更新时，要同时删除这两个key
	*   如果只保存一个value为User,那么key又如何设置呢？
	*
	* 2 getUserAndRoleById 根据id缓存UserAndRole,getUserById，根据id缓存User
	*
	* 按照spring-data-redis抽象的逻辑：生成key，将result缓存
	* 应对service中茫茫多种类方法时，显得不太能统一
	*/

	@Cacheable(value="user", key="#root.args[0]")
	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}
	public void addUser(User user) {

	}
	public void updateUser(User user) {

	}
	@CacheEvict(value="user")
	@Override
	public void removeUserById(String id) {
		userDao.delUserById(id);
	}

}
