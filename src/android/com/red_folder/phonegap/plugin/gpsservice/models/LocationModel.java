package com.red_folder.phonegap.plugin.gpsservice.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.red_folder.phonegap.plugin.gpsservice.logic.TransportStrategy;

public class LocationModel implements TransportStrategy.ITransportData {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private static String TAG = LocationModel.class.getSimpleName();

	/*
	 ************************************************************************************************
	 * Keys 
	 ************************************************************************************************
	 */
	public final static String MAP_KEY_HEADING = "heading";
	public final static String MAP_KEY_ALTITUDE = "altitude";
	public final static String MAP_KEY_ACCURACY = "accuracy";
	public final static String MAP_KEY_LONGITUDE = "longitude";
	public final static String MAP_KEY_LATITUDE = "latitude";
	public final static String MAP_KEY_SPEED = "speed";
	public final static String MAP_KEY_ALTITUDE_ACCURACY = "altitudeAccuracy";
	public final static String MAP_KEY_TIMESTAMP = "timestamp";

	
	/*
	 ************************************************************************************************
	 * Internal Data 
	 ************************************************************************************************
	 */
	
	// Location data and the lock to ensure only single access
	private final Object mLocationDataLock = new Object();
	
	private	double mLongitude = 0;
	private double mLatitude = 0;
	private float mAccuracy = 0;
	private double mAltitude = 0;
	private float mAltitudeAccuracy = 0;
	private float mHeading = 0;
	private float mSpeed = 0;
	private long mTimestamp = 0;
	
	private Date mUpdated = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */
	public LocationModel() {
	
	}
	
	public LocationModel(double pLongitude,
						double pLatitude,
						float pAccuracy,
						double pAltitude,
						float pAltitudeAccuracy,
						float pHeading,
						float pSpeed,
						long pTimestamp) {
		this.setLocation(pLongitude, pLatitude, pAccuracy, pAltitude, pAltitudeAccuracy, pHeading, pSpeed, pTimestamp);
	}
	
	
	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	public double getLongitude() {
		double result;
		synchronized (this.mLocationDataLock) {
			result = this.mLongitude;
		}

		return result;
	}

	public double getLatitude() {
		double result;
		synchronized (this.mLocationDataLock) {
			result = this.mLatitude;
		}
		return result;
	}
	
	public float getAccuracy() {
		float result;
		synchronized (this.mLocationDataLock) {
			result = this.mAccuracy;
		}
		return result;
	}

	public double getAltitude() {
		double result;
		synchronized (this.mLocationDataLock) {
			result = this.mAltitude;
		}
		return result;
	}
	
	public float getAltitudeAccuracy() {
		float result;
		synchronized (this.mLocationDataLock) {
			result = this.mAltitudeAccuracy;
		}
		return result;
	}

	public float getHeading() {
		float result;
		synchronized (this.mLocationDataLock) {
			result = this.mHeading;
		}
		return result;
	}

	public float getSpeed() {
		float result;
		synchronized (this.mLocationDataLock) {
			result = this.mSpeed;
		}
		return result;
	}

	public long getTimestamp() {
		long result;
		synchronized (this.mLocationDataLock) {
			result = this.mTimestamp;
		}
		return result;
	}

	/*
	 ************************************************************************************************
	 * Public 
	 ************************************************************************************************
	 */
	public void setLocation(double pLongitude,
							double pLatitude,
							float pAccuracy,
							double pAltitude,
							float pAltitudeAccuracy,
							float pHeading,
							float pSpeed,
							long pTimestamp) {
		
		synchronized (this.mLocationDataLock) {
			this.mLongitude = pLongitude;
			this.mLatitude = pLatitude;
			this.mAccuracy = pAccuracy;
			this.mAltitude = pAltitude;
			this.mAltitudeAccuracy = pAltitudeAccuracy;
			this.mHeading = pHeading;
			this.mSpeed = pSpeed;
			this.mTimestamp = pTimestamp;
			
			this.mUpdated = Calendar.getInstance().getTime();

			this.mLocationDataLock.notifyAll();
		}
	}

	public boolean isUndefined() {
		boolean result = false;

		synchronized (mLocationDataLock) {
			if (this.mUpdated == null)
				result = true;
		}
		
		return result;
	}

	public void clear() {
		synchronized (this.mLocationDataLock) {

			this.mLongitude = 0;
			this.mLatitude = 0;
			this.mAccuracy = 0;
			this.mAltitude = 0;
			this.mAltitudeAccuracy = 0;
			this.mHeading = 0;
			this.mSpeed = 0;
			this.mTimestamp = 0;
			
			this.mUpdated = null;
		}
	}
	
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
	public String getQueryString(Map<String, String> map) {
		String query = new String();

		if (map.containsKey(MAP_KEY_LONGITUDE)) {
			query += map.get(MAP_KEY_LONGITUDE) + "=" + String.valueOf(this.getLongitude());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_LATITUDE)) {
			query += map.get(MAP_KEY_LATITUDE) + "=" + String.valueOf(this.getLatitude());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_ACCURACY)) {
			query += map.get(MAP_KEY_ACCURACY) + "=" + String.valueOf(this.getAccuracy());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_ALTITUDE)) {
			query += map.get(MAP_KEY_ALTITUDE) + "=" + String.valueOf(this.getAltitude());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_ALTITUDE_ACCURACY)) {
			query += map.get(MAP_KEY_ALTITUDE_ACCURACY) + "=" + String.valueOf(this.getAltitudeAccuracy());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_HEADING)) {
			query += map.get(MAP_KEY_HEADING) + "=" + String.valueOf(this.getHeading());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_SPEED)) {
			query += map.get(MAP_KEY_SPEED) + "=" + String.valueOf(this.getSpeed());
			query += "&";
		}

		if (map.containsKey(MAP_KEY_TIMESTAMP)) {
			query += map.get(MAP_KEY_TIMESTAMP) + "=" + String.valueOf(this.getTimestamp());
		}
			
		return query;
	}

}
