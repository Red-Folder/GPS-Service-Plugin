package com.red_folder.phonegap.plugin.gpsservice.models;

import android.util.Log;

public class ServerModel {
	
	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = ServerModel.class.getSimpleName();


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
	private Boolean mSuccess = false;
	private String mFailureMessage = "";

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
	public Boolean getSuccess() {
		return this.mSuccess;
	}
	
	public String getFailureMessage() {
		return this.mFailureMessage;
	}

	public HTTPMethod getHTTPMethod() {
		return HTTPMethod.GET;
	}
	
	
	public void setFailure(String message) {
		this.mFailureMessage = message;
		
		// As we have failed, then we don't have success
		this.mSuccess = false;
	}


	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */
	public Boolean processResponse(int responseCode, String rawResponse) {
		Boolean result = false;
		
		Log.d(TAG, "responseCode = " + responseCode);
		Log.d(TAG, "rawResponse = " + rawResponse);
		
		if (responseCode != 200)
			this.mSuccess = false;
			
		result = true;
		
		return result;
	}
	
	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof ServerModel))
			return false;
		
		ServerModel that = (ServerModel)obj;
		
		if (!(this.mSuccess.equals(that.getSuccess()))) 
			return false;
		
		return true;
	}

	
	/*
	 ************************************************************************************************
	 * Internal classes
	 ************************************************************************************************
	 */
	public enum HTTPMethod {
		GET,
		POST
	}
	
}
