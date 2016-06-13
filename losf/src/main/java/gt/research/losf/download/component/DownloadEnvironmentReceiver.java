package gt.research.losf.download.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.util.NetworkUtil;

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
        ControlStateCenter.getInstance().setNetwork(NetworkUtil.getNetworkLevel(context));
    }


}
