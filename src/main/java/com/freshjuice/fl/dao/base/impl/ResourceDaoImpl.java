package com.freshjuice.fl.dao.base.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.freshjuice.fl.dao.FlBaseDao;
import com.freshjuice.fl.dao.base.IResourceDao;
import com.freshjuice.fl.dto.base.PriorityResource;

@Repository("resourceDao")
public class ResourceDaoImpl extends FlBaseDao implements IResourceDao {

	@Override
	public String getFAuthOfPath(String path) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IResourceDao.getFAuthOfPath", path);
	}

	@Override
	public List<String> getPermissionsOfUserByUn(String pricipal) {
		return this.getSqlSession().selectList("com.freshjuice.fl.dao.base.IResourceDao.getPermissionsOfUserByUn", pricipal);
	}

	@Override
	public PriorityResource getResourceOfPath(String pathWithinApplication) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.dao.base.IResourceDao.getResourceOfPath", pathWithinApplication);
	}

}
