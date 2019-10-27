package com.freshjuice.fl.base.entity;

public class PriorityResource {
	private String priorityId;    //id
	private String priorityCode;  //权限code
	private String priorityName;  //权限名称
	private String authf;     //是否需要认证
	private String authif;    //是否需要授权
	private String showf;     //是否显示 (废弃)
	private String resourceCode;  //资源code
	private String resourceName;  //资源名称
	private String resourceUrl;   //资源url
	private String resourcePCode; //资源父code
	
	public String getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(String priorityId) {
		this.priorityId = priorityId;
	}
	public String getPriorityCode() {
		return priorityCode;
	}
	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public String getAuthf() {
		return authf;
	}
	public void setAuthf(String authf) {
		this.authf = authf;
	}
	public String getShowf() {
		return showf;
	}
	public void setShowf(String showf) {
		this.showf = showf;
	}
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getResourcePCode() {
		return resourcePCode;
	}
	public void setResourcePCode(String resourcePCode) {
		this.resourcePCode = resourcePCode;
	}
	public String getAuthif() {
		return authif;
	}
	public void setAuthif(String authif) {
		this.authif = authif;
	}
}
