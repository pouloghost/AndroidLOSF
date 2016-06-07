package gt.research.losf.download.control;

import java.util.HashSet;
import java.util.Set;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IFileInfo;

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
    private final Set<IFileInfo> mPendingStartFiles = new HashSet<>(10);
    private final Set<Integer> mPendingStartBlocks = new HashSet<>(100);
    private final Set<Integer> mPendingPauseBlocks = new HashSet<>(100);
    private final Set<Integer> mPendingCancelBlocks = new HashSet<>(100);
    private final Set<IBlockInfo> mNetworkWaitList = new HashSet<>(100);

    public synchronized int getNetwork() {
        return mNetwork;
    }

    public void startFile(IFileInfo info) {
        synchronized (mPendingStartFiles) {
            mPendingStartFiles.add(info);
        }
    }

    public Set<IFileInfo> getPendingStartFiles() {
        synchronized (mPendingStartFiles) {
            return new HashSet<>(mPendingStartFiles);
        }
    }

    public void fileStarted(Set<IFileInfo> started) {
        synchronized (mPendingStartFiles) {
            mPendingCancelBlocks.removeAll(started);
        }
    }

    public Set<Integer> getPendingStartBlocks() {
        synchronized (mPendingStartBlocks) {
            return new HashSet<>(mPendingStartBlocks);
        }
    }

    public void startBlocks(int... ids) {
        synchronized (mPendingStartBlocks) {
            for (int id : ids) {
                mPendingStartBlocks.add(id);
            }
        }
    }

    public void blockStarted(Set<Integer> ids) {
        synchronized (mPendingStartBlocks) {
            mPendingStartBlocks.removeAll(ids);
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

    public void waitForNetwork(IBlockInfo info) {
        synchronized (mNetworkWaitList) {
            mNetworkWaitList.add(info);
        }
    }

    public void networkMeets(IBlockInfo info) {
        synchronized (mNetworkWaitList) {
            mNetworkWaitList.remove(info);
        }
    }
}
