package gt.research.losf.download.control;

import java.util.HashSet;
import java.util.Set;

import gt.research.losf.download.task.FileInfo;

/**
 * Created by GT on 2016/5/25.
 */
public class ControlStateCenter {
    private volatile static ControlStateCenter sInstance;

    public static ControlStateCenter getInstance() {
        if (null == sInstance) {
            synchronized (ControlStateCenter.class) {
                if (null == sInstance) {
                    sInstance = new ControlStateCenter();
                }
            }
        }
        return sInstance;
    }

    private ControlStateCenter() {
    }

    private int mNetwork;
    private final Set<FileInfo> mPendingStartFiles = new HashSet<>(10);
    private final Set<Integer> mPendingPauseBlocks = new HashSet<>(100);
    private final Set<Integer> mPendingCancelBlocks = new HashSet<>(100);

    public synchronized int getNetwork() {
        return mNetwork;
    }

    public void start(FileInfo info) {
        synchronized (mPendingStartFiles) {
            mPendingStartFiles.add(info);
        }
    }

    public Set<FileInfo> getPendingStarts() {
        synchronized (mPendingStartFiles) {
            return new HashSet<>(mPendingStartFiles);
        }
    }

    public void started(Set<FileInfo> started) {
        synchronized (mPendingStartFiles) {
            mPendingCancelBlocks.removeAll(started);
        }
    }

    public void pause(int... ids) {
        synchronized (mPendingPauseBlocks) {
            for (int id : ids) {
                mPendingPauseBlocks.add(id);
            }
        }
    }

    public boolean needPause(int id) {
        synchronized (mPendingPauseBlocks) {
            return mPendingPauseBlocks.contains(id);
        }
    }

    public void paused(int... ids) {
        synchronized (mPendingPauseBlocks) {
            for (int id : ids) {
                mPendingPauseBlocks.remove(id);
            }
        }
    }

    public void cancel(int... ids) {
        synchronized (mPendingCancelBlocks) {
            for (int id : ids) {
                mPendingCancelBlocks.add(id);
            }
        }
    }

    public boolean needCancel(int id) {
        synchronized (mPendingCancelBlocks) {
            return mPendingCancelBlocks.contains(id);
        }
    }

    public void canceled(int... ids) {
        synchronized (mPendingCancelBlocks) {
            for (int id : ids) {
                mPendingCancelBlocks.remove(id);
            }
        }
    }
}
