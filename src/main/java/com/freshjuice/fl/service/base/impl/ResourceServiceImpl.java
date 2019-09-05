package com.freshjuice.fl.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.freshjuice.fl.dao.base.IResourceDao;
import com.freshjuice.fl.dto.base.PriorityResource;
import com.freshjuice.fl.service.base.IResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements IResourceService {
	@Autowired
	private IResourceDao resourceDao;

	@Autowired
	private RedisTemplate<String, Object> redisTemplateComm;
	
	@Override
	public String getFAuthOfPath(String path) {
		return resourceDao.getFAuthOfPath(path);
	}

	@Override
	public List<String> getPermissionsOfUserByUn(String principal) {
		List<String> prios = (List<String>) redisTemplateComm.opsForValue().get("resourceCodes::" + principal);
		if(prios != null && prios.size() > 0) return prios;
		List<String> priosDb = resourceDao.getPermissionsOfUserByUn(principal);
		redisTemplateComm.opsForValue().set("resourceCodes::" + principal, priosDb);
		return priosDb;
	}

	@Override
	public PriorityResource getFResourceOfPath(String pathWithinApplication) {
		return resourceDao.getResourceOfPath(pathWithinApplication);
	}

}
