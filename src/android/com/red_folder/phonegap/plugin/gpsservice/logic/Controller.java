package com.red_folder.phonegap.plugin.gpsservice.logic;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.red_folder.phonegap.plugin.gpsservice.interfaces.IConfig;
import com.red_folder.phonegap.plugin.gpsservice.models.LocationModel;
import com.red_folder.phonegap.plugin.gpsservice.models.ServerModel;
import com.red_folder.phonegap.plugin.gpsservice.models.UploadModel;

public class Controller {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = Controller.class.getSimpleName();

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
	private IControllerListener mListener;
	private Context mContext;
	
	private IConfig mConfig = null;
	
	private LocationHelper mLocationHelper;

	private Thread mLocationThread;
	private ExecutorService mUploadExecutor;
	
	private Object mUploadingLocationLock = new Object();
	private UploadModel mUploadingLocation = null;
	private UploadModel mUploadedLocation = null;
	private ServerModel mUploadedLocationResult = null;
	
	private Object mLock = new Object();
	private LocationModel mLastLocationReceived = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */
	public Controller(IControllerListener listener, Context context, IConfig config) {
		this.mListener = listener;
		
		this.mContext = context;
		
		this.mConfig = config;
	}
	
	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	private LocationModel getLastLocation() {
		LocationModel result = null;
		
		synchronized (this.mLock) {
			// Copy to the last saved results
			// We don't clear the last location because we will send the last known
			result = this.mLastLocationReceived;
			
		}
		
		return result;
	}
	
	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */
	public JSONObject getResult() {
		UploadModel uploadedLocation;
		ServerModel uploadedLocationResult;

		synchronized (this.mUploadingLocationLock) {
			uploadedLocation = this.mUploadedLocation;
			uploadedLocationResult = this.mUploadedLocationResult;
		}

		return ResultFactory.getResult(uploadedLocation, uploadedLocationResult);
	}
	
	public void start() {
		Log.d(TAG, "startingLocationHelper");
		startLocationHelper();

		this.mUploadExecutor = Executors.newFixedThreadPool(5);
	}
	
	public void stop() {
		this.stopLocationHelper();
		
		Log.d(TAG, "Stopping the Upload executor");
		this.mUploadExecutor.shutdown();
		try {
			Log.d(TAG, "Waiting for Upload executor termination");
			this.mUploadExecutor.awaitTermination(60, TimeUnit.SECONDS);
			Log.d(TAG, "Upload executor terminated successfully");
		} catch (InterruptedException e) {
			Log.d(TAG, "Upload executor termination failed");
		}
		Log.d(TAG, "Stopped the Upload executor");
	}

	private void processResponse(ServerModel newResponse) {
		// If we need to take an action based on the server response, then it should go here
	}

	
	public void onTimer() {
		// No point uploading if we don't have a location
		Log.d(TAG, "Starting onTimer");
		if (this.getLastLocation() == null) {
			Log.d(TAG, "Still waiting on a location");
		} else {
			Log.d(TAG, "Starting to upload location");
			
			UploadModel model = new UploadModel(this.mConfig, this.getLastLocation());
			synchronized (mUploadingLocationLock) {
				this.mUploadingLocation = model;
			}

			UploadHelper task = new UploadHelper(model, new UploadHelper.IListener() {

				@Override
				public void onUploaded(ServerModel result) {
					synchronized (mUploadingLocationLock) {
						mUploadedLocationResult = result;
						mUploadedLocation = mUploadingLocation;
						mUploadingLocation = null;
					}

					updateLatestResult();

					processResponse(result);
					
				}
			});

			this.mUploadExecutor.execute(task);
		}
		Log.d(TAG, "End of onTimer");
	}


	public void addLocation(LocationModel model) {
		synchronized (this.mLock) {
			this.mLastLocationReceived = model;
		}
	}
	
	public void clearLastLocation() {
		synchronized (this.mLock) {
			if (this.mLastLocationReceived != null)
				this.mLastLocationReceived.clear();
		}
	}
	

	/*
	 ************************************************************************************************
	 * Private Methods 
	 ************************************************************************************************
	 */
	private void startLocationHelper() {
		Log.d(TAG, "startingLocationHelper");

		// If the LocationHelpder has yet to be created, then create it
		if (mLocationHelper == null) {
			Log.d(TAG, "Created new LocationHelper");
			mLocationHelper = new LocationHelper(this.mContext, new LocationHelper.ILocationListener() {
				@Override
				public void onLocationChanged(	double longitude,
												double latitude, 
												float accuracy, 
												double altitude,
												float altitudeAccuracy, 
												float heading, 
												float speed,
												long timestamp) {
					
					Log.d(TAG, "Location Changed");
		
					LocationModel model = new LocationModel(longitude,
															latitude, 
															accuracy, 
															altitude,
															altitudeAccuracy, 
															heading, 
															speed,
															timestamp);
					
					addLocation(model);
				}
			});
			
			
			// Reset the lastlocation
			clearLastLocation();
			
			mLocationThread = new Thread(mLocationHelper);
			mLocationThread.start();
		}
	}

	private void updateLatestResult() {
		if (this.mListener != null)
			this.mListener.onLatestResult(this.getResult());
	}
	
	private void stopLocationHelper() {
		if (mLocationHelper != null) {
			// Stop the LocationHelper
			Log.d(TAG, "Closing the LocationHelper");
			try {
				Log.d(TAG, "Calling StopRun");
				mLocationHelper.StopRun();
				
				Log.d(TAG, "Setting the Thread to null");
				mLocationThread = null;

				Log.d(TAG, "Setting the LocationHelper to null");
				mLocationHelper = null;
			} catch (Exception ex) {
				Log.d(TAG, "Problem occurred while closing the LocationHelper");
			}
		}
	}
	
	public interface IControllerListener {
		public void onNewTimerInterval(int milliseconds);
		public void onLatestResult(JSONObject result);
	}

}
