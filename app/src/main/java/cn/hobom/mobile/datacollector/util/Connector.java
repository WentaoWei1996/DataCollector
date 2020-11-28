package cn.hobom.mobile.datacollector.util;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class Connector {

	public static final int NO_NETWORK_START = 0;
	public static final int NO_NETWORK_USING = 1;

	/**
	 * Checks if is connected.
	 *
	 * @param ctx
	 *            the ctx
	 * @return true, if is connected
	 */
	public static boolean isConnected(final Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}

	/**
	 * Checks if is wifi.
	 *
	 * @param ctx
	 *            the ctx
	 * @return true, if is wifi
	 */
	public static boolean isWifi(final Context ctx) {
		WifiManager wm = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wi = wm.getConnectionInfo();
		if (wi != null
				&& (WifiInfo.getDetailedStateOf(wi.getSupplicantState()) == DetailedState.OBTAINING_IPADDR || WifiInfo
				.getDetailedStateOf(wi.getSupplicantState()) == DetailedState.CONNECTED)) {
			return true;
		}
		return false;
	}

	public static boolean wifiOpened(final Context context) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wm.isWifiEnabled();
	}

	/**
	 * Checks if is umts.
	 *
	 * @param ctx
	 *            the ctx
	 * @return true, if is umts
	 */
	public static boolean isUmts(final Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getNetworkType() >= TelephonyManager.NETWORK_TYPE_UMTS;
	}

	/**
	 * Checks if is edge.
	 *
	 * @param ctx
	 *            the ctx
	 * @return true, if is edge
	 */
	public static boolean isEdge(final Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE;
	}

	/**
	 *
	 *
	 * @param context
	 * @return true if the gps is available
	 */
	public static boolean isGPSEnabled(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isSDCARDAvaible(final Context context) {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean isGPSInService(final Context context) {

		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
			return false;

		GpsStatus status = locationManager.getGpsStatus(null);

		Iterable<GpsSatellite> sats = status.getSatellites();
		if (sats.iterator().hasNext()) {
			return true;
		} else
			return false;
	}

}
