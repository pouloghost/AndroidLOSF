package gt.research.losf.download.control;

import android.content.Context;
import android.content.Intent;

import gt.research.losf.download.component.DownloadManagerService;
import gt.research.losf.download.task.FileInfo;

/**
 * Created by ayi.zty on 2016/5/26.
 */
public class DownloadManager {
    public static void start(Context context, FileInfo fileInfo) {
        ControlStateCenter.getInstance().start(fileInfo);
        Intent intent = new Intent(DownloadManagerService.ACTION_NEW);
        intent.setPackage("gt.research.losf");
        intent.setClass(context, DownloadManagerService.class);
    }
}
