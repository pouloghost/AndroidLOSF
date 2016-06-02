package gt.research.losf.download.component;

import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.JournalMaker;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadRunnable implements Runnable {
    private IBlockInfo mBlock;
    private IJournal mJournal;

    public DownloadRunnable(IBlockInfo blockInfo) {
        mJournal = JournalMaker.get();
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
            mJournal.deleteBlock(mBlock.getBlockId());
            return;
        }
        
    }
}
