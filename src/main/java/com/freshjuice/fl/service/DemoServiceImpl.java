package com.freshjuice.fl.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshjuice.fl.dao.DemoDao;

@Service("demoService")
public class DemoServiceImpl implements DemoService {
	@Autowired
	private DemoDao demoDao;

	@Override
	public Map<String, String> getById(String id) {
		return demoDao.getById(id);
	}

	@Transactional
	@Override
	public void addDemo(Map<String, String> addDemo) {
		 demoDao.addDemo(addDemo);
		 int i = 1/0;
	}
	
}
