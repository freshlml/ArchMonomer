package com.freshjuice.fl.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

public class FlBaseDao extends SqlSessionDaoSupport {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> find_myBatis(String sqlId, Object paramObject, int offset, int size) {
		@SuppressWarnings("rawtypes")
		List rtnList = null;
		if ((offset >= 0) && (size > 0)) {
			RowBounds rowBounds = new RowBounds(offset, size);
			rtnList = getSqlSession().selectList(sqlId, paramObject, rowBounds);
		} else {
			rtnList = getSqlSession().selectList(sqlId, paramObject);
		}
		return rtnList;
	}
	
	public <T> int save_myBatis(String sqlId, T entity) {
		return getSqlSession().insert(sqlId, entity);
	}
	
	public <T> int save_jdbctemplate(T entity) {
		return 0;
	}
	
	
}
