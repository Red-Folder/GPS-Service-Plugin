package com.red_folder.phonegap.plugin.gpsservice;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


import android.content.Intent;
import android.util.Log;



import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;
import com.red_folder.phonegap.plugin.gpsservice.exceptions.NotYetImplementedException;
import com.red_folder.phonegap.plugin.gpsservice.interfaces.IConfig;
import com.red_folder.phonegap.plugin.gpsservice.logic.Controller;
import com.red_folder.phonegap.plugin.gpsservice.logic.HTTPMethod;
import com.red_folder.phonegap.plugin.gpsservice.logic.ResultFactory;
import com.red_folder.phonegap.plugin.gpsservice.models.LocationModel;
import com.red_folder.phonegap.plugin.gpsservice.models.ServiceConfig;

public class GPSService  extends BackgroundService implements Controller.IControllerListener {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = GPSService.class.getSimpleName();
	
	
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
	Controller mController = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */

	
	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	public HTTPMethod getMethod() {
		return HTTPMethod.GET;
	}
	
	public void setMethod(HTTPMethod method) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public String getURL() {
		return "http://www.test.com";
	}
	
	public void setURL(String url) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public Map<String, String> getMap() {
		Map<String, String> result = new HashMap<String, String>();
		
		result.put(LocationModel.MAP_KEY_HEADING, "heading");
		result.put(LocationModel.MAP_KEY_ALTITUDE, "altitude");
		result.put(LocationModel.MAP_KEY_LATITUDE, "latitude");
		result.put(LocationModel.MAP_KEY_ACCURACY, "accuracy");
		result.put(LocationModel.MAP_KEY_LONGITUDE, "longitude");
		result.put(LocationModel.MAP_KEY_SPEED, "speed");
		result.put(LocationModel.MAP_KEY_ALTITUDE_ACCURACY, "altitudeAccuracy");
		result.put(LocationModel.MAP_KEY_TIMESTAMP, "timestamp");
		
		return result;
	}

	public void setMap(Map<String, String> map) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public IConfig getServiceConfig() {
		return new ServiceConfig(this.getMethod(), this.getURL(), this.getMap());
	}

	/*
	 ************************************************************************************************
	 * Service functions
	 * Overriden from the BackgroundService 
	 ************************************************************************************************
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service started");       

		super.onStart(intent, startId);
	}
	
	private void initialiseController() {
		this.mController = new Controller(this, this, this.getServiceConfig());
		
		this.mController.start();
	}
	
	@Override
	protected JSONObject initialiseLatestResult() {
		return ResultFactory.getResult();
	}
	
	@Override
	protected JSONObject doWork() {
		Log.d(TAG, "Started DoWork");
		
		if (this.mController == null)
			initialiseController();
		
		Log.d(TAG, "Calling Controller");
		this.mController.onTimer();
		
		return this.mController.getResult();
	}

	@Override
	protected void onTimerEnabled() {
		initialiseController();
	}
	
	@Override
	protected void onTimerDisabled() {
		if (this.mController != null) {
			this.mController.stop();
			this.mController = null;
		}
	}

	@Override
	protected JSONObject getConfig() {
		return null;
	}

	@Override
	protected void setConfig(JSONObject arg0) {
		
	}

	@Override
	public void onNewTimerInterval(int milliseconds) {
		// Called by the controller when the server has requested we change the timer interval
		// There is overhead in changing, so only call if different
		if (this.getMilliseconds() != milliseconds) {
			this.setMilliseconds(milliseconds);
			this.restartTimer();
		}
	}

	@Override
	public void onLatestResult(JSONObject result) {
		this.setLatestResult(result);
		
	}

	
}
