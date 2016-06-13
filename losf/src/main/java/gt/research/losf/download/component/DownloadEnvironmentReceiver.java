package gt.research.losf.download.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import gt.research.losf.download.control.ControlStateCenter;

import static android.net.ConnectivityManager.TYPE_ETHERNET;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import static gt.research.losf.FileConfig.NET_2G;
import static gt.research.losf.FileConfig.NET_3G;
import static gt.research.losf.FileConfig.NET_4G;
import static gt.research.losf.FileConfig.NET_WIFI;

/**
 * Created by GT on 2016/6/13.
 */
public class DownloadEnvironmentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            handleConnectivityChange(context);
        }
    }

    private void handleConnectivityChange(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        int network = -1;
        if (null != info && info.isConnected()) {
            switch (info.getType()) {
                case TYPE_WIFI:
                case TYPE_ETHERNET:
                    network = NET_WIFI;
                    break;
                case TYPE_MOBILE:
                    network = extractMobileType(info);
                    break;
            }
        }
        ControlStateCenter.getInstance().setNetwork(network);
    }

    private int extractMobileType(NetworkInfo info) {
        switch (info.getSubtype()) {
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_GPRS:
                return NET_2G;
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_HSPAP:
            case NETWORK_TYPE_HSUPA:
                return NET_3G;
            case NETWORK_TYPE_LTE:
                return NET_4G;
        }
        return -1;
    }
}
