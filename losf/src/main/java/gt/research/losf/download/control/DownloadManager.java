package gt.research.losf.download.control;

import android.content.Context;
import android.content.Intent;

import gt.research.losf.download.component.DownloadManagerService;
import gt.research.losf.journal.IFileInfo;

import static gt.research.losf.download.component.DownloadManagerService.ACTION_NEW;
import static gt.research.losf.download.component.DownloadManagerService.KEY_FILE_NAME;

/**
 * Created by GT on 2016/5/26.
 */
public class DownloadManager {
    public static void start(Context context, IFileInfo fileInfo) {
        ControlStateCenter.getInstance().startFile(fileInfo);
        startService(context, fileInfo.getFile());
    }

    private static void startService(Context context, String file) {
        Intent intent = new Intent(ACTION_NEW);
        intent.setPackage("gt.research.losf");
        intent.setClass(context, DownloadManagerService.class);
        intent.putExtra(KEY_FILE_NAME, file);
        context.startService(intent);
    }

    public static void start(Context context, int... ids) {
        ControlStateCenter.getInstance().startBlocks(ids);
        startService(context, "");
    }

    public static void pause(Context context, int... ids) {
        ControlStateCenter.getInstance().pause(ids);
    }

    public static void cancel(Context context, int... ids) {
        ControlStateCenter.getInstance().cancel(ids);
    }
}
