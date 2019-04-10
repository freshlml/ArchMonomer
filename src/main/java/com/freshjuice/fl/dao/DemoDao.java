package com.freshjuice.fl.dao;

import java.util.Map;

public interface DemoDao {
	Map<String, String> getById(String id);
	void addDemo(Map<String, String> addDemo);
}
