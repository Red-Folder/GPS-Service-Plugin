package com.red_folder.phonegap.plugin.gpsservice.logic;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/*
 * Below ULR used for reference@
 * http://developer.android.com/guide/topics/location/strategies.html
 */

public class LocationHelper extends Thread {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private static final String TAG = LocationHelper.class.getSimpleName();

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
	private LocationManager mLocationManager;
	private LocationListener mListener;
	
	private ILocationListener mClientListener;
	
	private Looper mLooper;
	
	/*
	 ************************************************************************************************
	 * Constructors 
	 ************************************************************************************************
	 */
	public LocationHelper(Context context, ILocationListener clientListener) {
		// Acquire a reference to the system Location Manager
		Log.d(TAG, "Constructor called");

		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mClientListener = clientListener;
	}

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
	public void run() {
		Log.d(TAG, "run()");

		// Define a listener that responds to location updates
		mListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged called");

				// Called when a new location is found by the network location provider.
				makeUseOfNewLocation(location);
			}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		};

		// Prepare the Looper
		Log.d(TAG, "Preparing Looper");
		Looper.prepare();
		mLooper = Looper.myLooper();
		
		// Register the listener with the Location Manager to receive location updates
		Log.d(TAG, "Registering for GPS provider");
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
		
		// Get the most recent location
		makeUseOfNewLocation(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		
		Looper.loop();
		
		// If we have come out of the loop, then we need to free everything
		Log.d(TAG, "Looper quit, time to destroy");
		Log.d(TAG, "Removing listener for GPS provider");
		mLocationManager.removeUpdates(mListener);

		Log.d(TAG, "Setting location to null");
		mListener = null;
		
		Log.d(TAG, "Setting location manager to null");
		mLocationManager = null;

		Log.d(TAG, "Run finished");
	}
	
	public void StopRun() {
		if (mLooper != null)
			mLooper.quit();
	}

	/*
	 ************************************************************************************************
	 * Private Methods 
	 ************************************************************************************************
	 */
	private void makeUseOfNewLocation(Location location) {
		Log.d(TAG, "makeUseOfNewLocation called");

		if (location != null)
		{
			if (mClientListener != null) {
				Log.d(TAG, "Pass details to listener");

				if (mClientListener == null) Log.d(TAG, "mClientListener is null");
				mClientListener.onLocationChanged(	location.getLongitude(), 
													location.getLatitude(),
													location.getAccuracy(),
													location.getAltitude(),
													location.getAccuracy(),
													location.getBearing(),
													location.getSpeed(),
													location.getTime());
			}
		}
	}
	
	/*
	 ************************************************************************************************
	 * Internal Classes/ Interfaces 
	 ************************************************************************************************
	 */
	interface ILocationListener {
		void onLocationChanged(	double longitude,
								double latitude,
								float accuracy,
								double altitude,
								float altitudeAccuracy,
								float heading,
								float speed,
								long timestamp);
	}
}
