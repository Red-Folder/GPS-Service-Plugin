package com.red_folder.phonegap.plugin.gpsservice.interfaces;

import java.util.Map;

import com.red_folder.phonegap.plugin.gpsservice.logic.HTTPMethod;

public interface IConfig {
	public HTTPMethod getMethod();
	
	public String getURL();
	
	public Map<String, String> getMap();
	
	public Boolean getLogOnly();
}
