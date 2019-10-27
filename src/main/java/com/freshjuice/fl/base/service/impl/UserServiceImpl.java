package com.freshjuice.fl.base.service.impl;

import com.freshjuice.fl.base.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.freshjuice.fl.base.mapper.IUserDao;
import com.freshjuice.fl.base.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private RedisTemplate<String, Object> redisTempleteComm;

	@Override
	public String getPswdOfUserByUn(String principal) {
		return userDao.getPswdOfUserByUn(principal);
	}

	@Override
	public User getUserByUn(String username) {
		User user = (User) redisTempleteComm.opsForValue().get("user:" + username);
		if (user != null) return user;
		User userDb = userDao.getUserByUn(username);
		redisTempleteComm.opsForValue().set("user::" + username, userDb);
		return userDb;
	}

	@Override
	public User getUserByPhone(String phone) {
		User user = (User) redisTempleteComm.opsForValue().get("user:" + phone);
		if(user != null) return user;
		User userDb = userDao.getUserByPhone(phone);
		redisTempleteComm.opsForValue().set("user::" + phone, userDb);
		return userDb;
	}

	@Override
	public User getUserById(String userId) {
		User user = (User) redisTempleteComm.opsForValue().get("user:" + userId);
		if(user != null) return user;
		User userDb = userDao.getUserById(userId);
		redisTempleteComm.opsForValue().set("user:" + userId, userDb);
		return userDb;
	}

	/**
	 * cache方案
	 * 读： 查询cache,命中返回,不命中查询db,写入cache，查询cache，写入cache失败均没关系
	 * 写： 更新db，删除cache(删除成功或者失败都没关系)，开启异步删除消息(保证成功执行一次删除)
	 * @param user
	 */
	@Transactional
	@Override
	public void updateUser(User user) {
		User userPrev = getUserById(user.getUserId());
		userDao.updateUser(user);
		redisTempleteComm.delete("user:" + userPrev.getUserId());
		redisTempleteComm.delete("user:" + userPrev.getUserName());
		redisTempleteComm.delete("user:" + userPrev.getPhone());
		//向消息队列写一个异步删除消息，消息队列消费必须确保该删除成功执行一次

	}



	/*
	 * spring-data-redis 抽象目前遇到的问题：
	 * 1 getUserById 根据id缓存User,getUserByName 根据name缓存User （产生大量冗余？）
	 *   这样将存在两个相同value的key,则当User中某些字段更新时,要同时删除这两个key （如何同时删除这两个key？）
	 *   如果只保存一个value为User,那么key又如何设置呢？
	 * 2 针对"缓存从数据库中查询的数据"，这和mybatis二级缓存没区别
	 * 3 spring-data-redis抽象只提供了string格式api支持
	 *
	 * 按照spring-data-redis抽象的逻辑：生成key，将result缓存
	 * 应对service中茫茫多种类方法时，显得不太能统一
	 *
	 * 目前的结论是：对需要缓存的数据使用redisTemplate
	 */
	/*@Cacheable(value="user", key="#root.args[0]")
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
	}*/

}
