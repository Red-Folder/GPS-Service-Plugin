package com.red_folder.phonegap.plugin.gpsservice.logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.red_folder.phonegap.plugin.gpsservice.models.LocationModel;
import com.red_folder.phonegap.plugin.gpsservice.models.ServerModel;
import com.red_folder.phonegap.plugin.gpsservice.models.UploadModel;

public class ResultFactory {
	
	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */


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

	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */
	public static JSONObject getResult() {
		return getResult(null, null);
	}

	public static JSONObject getResult(	UploadModel lastLocationUploaded,
										ServerModel lastLocationUploadedResult) {
		
		JSONObject locationResult = new JSONObject();
		if (lastLocationUploaded == null || lastLocationUploadedResult == null) {
			try { locationResult.put("Uploaded", false); } catch (JSONException e) {}
			try { locationResult.put("Message", "Location not yet established"); } catch (JSONException e) {}
		} else {
			
			LocationModel location = lastLocationUploaded.getLocation();

			if (lastLocationUploadedResult.getSuccess()) {
				try { locationResult.put("Uploaded", true); } catch (JSONException e) {}
				try { locationResult.put("Message", "Location Uploaded"); } catch (JSONException e) {}
			} else {
				try { locationResult.put("Uploaded", false); } catch (JSONException e) {}
				try { locationResult.put("Message", "Unable to upload location"); } catch (JSONException e) {}
			}
		
			try { locationResult.put("Longitude", location.getLongitude()); } catch (JSONException e) {}
			try { locationResult.put("Latitude", location.getLatitude()); } catch (JSONException e) {}
			try { locationResult.put("Accuracy", location.getAccuracy()); } catch (JSONException e) {}
			try { locationResult.put("Altitude", location.getAltitude()); } catch (JSONException e) {}
			try { locationResult.put("AltitudeAccuracy", location.getAltitudeAccuracy()); } catch (JSONException e) {}
			try { locationResult.put("Heading", location.getHeading()); } catch (JSONException e) {}
			try { locationResult.put("Speed", location.getSpeed()); } catch (JSONException e) {}
			try { locationResult.put("Timestamp", location.getTimestamp()); } catch (JSONException e) {}

			if (lastLocationUploadedResult != null) {
				JSONObject serverResult = new JSONObject();

				try { serverResult.put("Success", lastLocationUploadedResult.getSuccess()); } catch (JSONException e) {}
				try { serverResult.put("FailureMessage", lastLocationUploadedResult.getFailureMessage()); } catch (JSONException e) {}

				try { locationResult.put("ServerResponse", serverResult); } catch (JSONException e) {}
			}

		}
		
		return locationResult;
	}


	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */

}
