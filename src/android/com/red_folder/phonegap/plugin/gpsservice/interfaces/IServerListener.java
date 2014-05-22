package com.red_folder.phonegap.plugin.gpsservice.interfaces;

import com.red_folder.phonegap.plugin.gpsservice.models.ServerModel;

public interface IServerListener {
	
	public void onResponseReceived(ServerModel oldResponse, ServerModel newResponse);

}
