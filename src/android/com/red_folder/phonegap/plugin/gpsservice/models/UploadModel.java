package com.red_folder.phonegap.plugin.gpsservice.models;

import java.util.Map;

import com.red_folder.phonegap.plugin.gpsservice.exceptions.NotYetImplementedException;
import com.red_folder.phonegap.plugin.gpsservice.interfaces.IConfig;
import com.red_folder.phonegap.plugin.gpsservice.logic.TransportStrategy;
import com.red_folder.phonegap.plugin.gpsservice.logic.UploadHelper;

public class UploadModel implements UploadHelper.IUploadModel, TransportStrategy.ITransportData {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = UploadModel.class.getSimpleName();

	/*
	 ************************************************************************************************
	 * Keys 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Internal Data 
	 ************************************************************************************************
	 */
	private IConfig mConfig = null;
	private LocationModel mModel = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */
	public UploadModel(IConfig config, LocationModel model) {
		this.mConfig = config;
		this.mModel = model;
	}


	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	public LocationModel getLocation() {
		return this.mModel;
	}

	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Implemented method 
	 ************************************************************************************************
	 */

	@Override
	public ServerModel Upload() throws NotYetImplementedException {
		return TransportStrategy.save(this.mConfig.getMethod(), this.mConfig.getURL(), this.mConfig.getMap(), this);
	}


	@Override
	public String getQueryString(Map<String, String> map) {
		
		return this.mModel.getQueryString(map);
	}

}
