package gt.research.losf.download.component;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadManagerService extends Service {
    public static final String ACTION_NEW = "gt.research.losf.download.NEW";
    public static final String ACTION_PAUSE = "gt.research.losf.download.PAUSE";
    public static final String ACTION_CANCEL = "gt.research.losf.download.CANCEL";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Bundle extra = intent.getExtras();
        if (ACTION_NEW.equals(action)) {
            doAddTask(extra);
        } else if (ACTION_PAUSE.equals(action)) {
            doPauseTask(extra);
        } else if (ACTION_CANCEL.equals(action)) {
            doCancelTask(extra);
        }
        return START_NOT_STICKY;
    }

    private void doCancelTask(Bundle extra) {

    }

    private void doPauseTask(Bundle extra) {

    }

    private void doAddTask(Bundle extra) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
