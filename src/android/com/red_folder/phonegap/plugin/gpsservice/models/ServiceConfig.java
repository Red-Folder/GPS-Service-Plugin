package com.red_folder.phonegap.plugin.gpsservice.models;

import java.util.Map;

import com.red_folder.phonegap.plugin.gpsservice.interfaces.IConfig;
import com.red_folder.phonegap.plugin.gpsservice.logic.HTTPMethod;

public class ServiceConfig implements IConfig{

	private HTTPMethod mMethod;
	private String mURL;
	private Map<String, String> mMap;
	
	public ServiceConfig(HTTPMethod method, String url, Map<String, String> map) {
		this.mMethod = method;
		this.mURL = url;
		this.mMap = map;
	}
	
	@Override
	public HTTPMethod getMethod() {
		return this.mMethod;
	}

	@Override
	public String getURL() {
		return this.mURL;
	}

	@Override
	public Map<String, String> getMap() {
		return this.mMap;
	}
	
}
