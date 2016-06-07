package gt.research.losf.download.component;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IFileInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.JournalMaker;

import static gt.research.losf.journal.IFileInfo.MD5_SUCCESS;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadManagerService extends Service {
    public static final String ACTION_NEW = "gt.research.losf.download.NEW";
    public static final String ACTION_PAUSE = "gt.research.losf.download.PAUSE";
    public static final String ACTION_CANCEL = "gt.research.losf.download.CANCEL";

    public static final String KEY_FILE_NAME = "fileName";

    private ExecutorService mDownloadExecutor;
    private ExecutorService mInitExecutor;

    private IJournal mJournal;

    @Override
    public void onCreate() {
        mJournal = JournalMaker.get();

        mDownloadExecutor = new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        mInitExecutor = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        resumeDownloads();
    }

    private void resumeDownloads() {
        List<IBlockInfo> blocks = mJournal.getAllBlocks();
        for (IBlockInfo block : blocks) {
            if (block.getRead() != block.getLength() ||
                    (!TextUtils.isEmpty(block.getMd5()) && !MD5_SUCCESS.equals(block.getMd5()))) {
                mDownloadExecutor.execute(new DownloadRunnable(block));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Bundle extra = intent.getExtras();
        if (ACTION_NEW.equals(action)) {
            doAddBlock(extra);
            doAddFile(extra);
        } else if (ACTION_PAUSE.equals(action)) {
            doPauseFile(extra);
        } else if (ACTION_CANCEL.equals(action)) {
            doCancelFile(extra);
        }
        return START_NOT_STICKY;
    }

    private void doAddBlock(Bundle extra) {
        mInitExecutor.execute(new InitBlockRunnable());
        Toast.makeText(DownloadManagerService.this, "add block", Toast.LENGTH_SHORT).show();
    }

    private void doAddFile(Bundle extra) {
        mInitExecutor.execute(new InitFileRunnable());
        Toast.makeText(DownloadManagerService.this, "add file", Toast.LENGTH_SHORT).show();
    }

    private void doPauseFile(Bundle extra) {

    }

    private void doCancelFile(Bundle extra) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InitFileRunnable implements Runnable {
        @Override
        public void run() {
            ControlStateCenter state = ControlStateCenter.getInstance();
            Set<IFileInfo> fileSet = state.getPendingStartFiles();
            for (IFileInfo file : fileSet) {
                List<IBlockInfo> blocks = file.getBlocks();
                for (IBlockInfo blockInfo : blocks) {
                    mJournal.addBlock(blockInfo);
                    mDownloadExecutor.execute(new DownloadRunnable(blockInfo));
                }
            }
            state.fileStarted(fileSet);
        }
    }

    private class InitBlockRunnable implements Runnable {
        @Override
        public void run() {
            ControlStateCenter state = ControlStateCenter.getInstance();
            Set<Integer> ids = state.getPendingStartBlocks();
            for (Integer id : ids) {
                IBlockInfo blockInfo = mJournal.getBlock(id);
                if (null != blockInfo) {
                    mDownloadExecutor.execute(new DownloadRunnable(blockInfo));
                }
            }
            state.blockStarted(ids);
        }
    }
}
