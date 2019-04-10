package com.freshjuice.fl.service;

import java.util.Map;

public interface DemoService {
	Map<String, String> getById(String id);
	void addDemo(Map<String, String> addDemo);
}
