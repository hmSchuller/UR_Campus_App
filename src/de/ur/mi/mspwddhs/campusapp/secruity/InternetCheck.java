package de.ur.mi.mspwddhs.campusapp.secruity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;

public class InternetCheck {
	

	public static boolean isInternetAvailiable(Context context, OptionsActivity activity){
		
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		activity.internetCheckToast();
		return false;
					
}
}
