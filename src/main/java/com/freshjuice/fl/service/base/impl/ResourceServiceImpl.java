package com.freshjuice.fl.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshjuice.fl.dao.base.IResourceDao;
import com.freshjuice.fl.dto.base.PriorityResource;
import com.freshjuice.fl.service.base.IResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements IResourceService {
	@Autowired
	private IResourceDao resourceDao;
	
	@Override
	public String getFAuthOfPath(String path) {
		return resourceDao.getFAuthOfPath(path);
	}

	@Override
	public List<String> getPermissionsOfUserByUn(String pricipal) {
		return resourceDao.getPermissionsOfUserByUn(pricipal);
	}

	@Override
	public PriorityResource getFResourceOfPath(String pathWithinApplication) {
		return resourceDao.getResourceOfPath(pathWithinApplication);
	}

}
