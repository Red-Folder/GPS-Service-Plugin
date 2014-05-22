package com.red_folder.phonegap.plugin.gpsservice.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

import com.red_folder.phonegap.plugin.gpsservice.models.ServerModel;

import android.util.Log;

public class HTTPGet {
	
	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	public static String TAG = HTTPGet.class.getSimpleName();
	private final static int CONNECTION_TIMEOUT = 30 * 1000;		// 30 Seconds
	
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
	public static ServerModel execute(String destination, String queryString) {
		Log.d(TAG, "start of Execute");
		ServerModel result = new ServerModel();
		
		try {
			Log.d(TAG, "URL = " + destination);
			Log.d(TAG, "QueryString = " + queryString);
			
			String fullUrl = destination + "?" + queryString;
			
			URL url = new URL(fullUrl);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			if (fullUrl.toLowerCase().startsWith("https:"))
			{
				// HTTPS specific code
				// TODO if required
			}

			// Set a timeout
			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			connection.connect();
			
			int responseCode = connection.getResponseCode();
			
			String responseBody = getBody(connection);

			((HttpURLConnection)connection).disconnect();
			connection = null;

			result.processResponse(responseCode, responseBody);
			
		} catch (SocketTimeoutException e) {
			Log.d(TAG, "Connection timed out");
			result.setFailure("Connection timed out");
		} catch (ClientProtocolException e) {
			Log.d(TAG, "Failed execute HTTP request - " + e.getMessage());
			result.setFailure("Failed execute HTTP request - " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Failed execute HTTP request - " + e.getMessage());
			result.setFailure("Failed execute HTTP request - " + e.getMessage());
		}

		return result;

	}

	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */
	private static String getBody(HttpURLConnection connection) {
		String result = "";

		try {
			int contentLength = connection.getContentLength();
			if (contentLength > 0) {
				InputStream input = null;
	
				input = connection.getInputStream();
				byte[] buffer = new byte[connection.getContentLength()];
				input.read(buffer);

				result = new String(buffer);
				input.close();
				input = null;
			}
		} catch (Exception ex) {
				
		}
		
		return result;
	}
}
