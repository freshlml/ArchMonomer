package com.freshjuice.fl.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("demoDao")
public class DemoDaoImpl extends FlBaseDao implements DemoDao {

	@Override
	public Map<String, String> getById(String id) {
		return getSqlSession().selectOne("com.freshjuice.fl.dao.DemoDao.getById", id);
	}

	@Override
	public void addDemo(Map<String, String> addDemo) {
		getSqlSession().insert("com.freshjuice.fl.dao.DemoDao.addDemo", addDemo);
	}
	
}
