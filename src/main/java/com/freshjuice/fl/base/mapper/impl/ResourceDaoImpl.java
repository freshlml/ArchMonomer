package com.freshjuice.fl.base.mapper.impl;

import java.util.List;

import com.freshjuice.fl.base.mapper.IResourceDao;
import org.springframework.stereotype.Repository;

import com.freshjuice.fl.dao.FlBaseDao;
import com.freshjuice.fl.base.entity.PriorityResource;

@Repository("resourceDao")
public class ResourceDaoImpl extends FlBaseDao implements IResourceDao {

	@Override
	public String getFAuthOfPath(String path) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IResourceDao.getFAuthOfPath", path);
	}

	@Override
	public List<String> getPermissionsOfUserByUn(String pricipal) {
		return this.getSqlSession().selectList("com.freshjuice.fl.base.mapper.IResourceDao.getPermissionsOfUserByUn", pricipal);
	}

	@Override
	public PriorityResource getResourceOfPath(String pathWithinApplication) {
		return this.getSqlSession().selectOne("com.freshjuice.fl.base.mapper.IResourceDao.getResourceOfPath", pathWithinApplication);
	}

}
