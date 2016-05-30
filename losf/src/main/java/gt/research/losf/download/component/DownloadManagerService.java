package gt.research.losf.download.component;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gt.research.losf.LosfApplication;
import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.download.task.FileInfo;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.db.DBJournal;
import gt.research.losf.journal.file.FileJournal;
import gt.research.losf.util.LogUtils;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadManagerService extends Service {
    public static final String ACTION_NEW = "gt.research.losf.download.NEW";
    public static final String ACTION_PAUSE = "gt.research.losf.download.PAUSE";
    public static final String ACTION_CANCEL = "gt.research.losf.download.CANCEL";

    public static final String KEY_FILE_NAME = "fileName";

    private ExecutorService mExecutorPool;

    @Override
    public void onCreate() {
        mExecutorPool = new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        //// TODO: 2016/5/30 resume downloads in db
    }

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

    private void doAddTask(Bundle extra) {
        mExecutorPool.execute(new InitRunnable());
        Toast.makeText(DownloadManagerService.this, "add", Toast.LENGTH_SHORT).show();
    }

    private void doPauseTask(Bundle extra) {

    }

    private void doCancelTask(Bundle extra) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InitRunnable implements Runnable {
        @Override
        public void run() {
            IJournal journal;
            if (IJournal.USE_DB) {
                journal = new DBJournal(LosfApplication.getInstance());
            } else {
                try {
                    journal = new FileJournal("journal");
                } catch (IOException e) {
                    LogUtils.exception(this, e);
                }
            }
            ControlStateCenter state = ControlStateCenter.getInstance();
            Set<FileInfo> fileSet = state.getPendingStarts();
            for (FileInfo file : fileSet) {
                Collection<IBlockInfo> blocks = file.getBlockInfo();
                for (IBlockInfo blockInfo : blocks) {
                    journal.addBlock(blockInfo);
                    mExecutorPool.execute(new DownloadRunnable(blockInfo));
                }
                state.start(file);
            }
        }
    }
}
