package gt.research.losf.download.component;

import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.journal.IBlockInfo;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadRunnable implements Runnable {
    private IBlockInfo mBlock;

    public DownloadRunnable(IBlockInfo blockInfo) {
        mBlock = blockInfo;
    }

    @Override
    public void run() {
        ControlStateCenter state = ControlStateCenter.getInstance();
        if (state.needPause(mBlock.getBlockId())) {
            state.paused(mBlock.getBlockId());
            return;
        }
        if (state.needCancel(mBlock.getBlockId())) {

        }
    }
}
