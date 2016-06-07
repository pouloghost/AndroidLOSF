package gt.research.losf.download.component;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.JournalMaker;
import gt.research.losf.util.LogUtils;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadRunnable implements Runnable {
    private IBlockInfo mBlock;
    private IJournal mJournal;

    private byte[] mTmp;
    private MessageDigest mDigest;

    public DownloadRunnable(IBlockInfo blockInfo) {
        mJournal = JournalMaker.get();
        mBlock = blockInfo;
        if (!TextUtils.isEmpty(blockInfo.getMd5())) {
            try {
                mDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                LogUtils.exception(this, e);
            }
        }
        if (null == mDigest) {
            mDigest = new MockMessageDigest("");
        }
    }

    /**
     * download stage:
     * download data
     * verify md5
     */
    @Override
    public void run() {
        ControlStateCenter state = ControlStateCenter.getInstance();
        if (state.needCancel(mBlock.getId())) {
            mJournal.deleteBlock(mBlock.getId());
            state.canceled(mBlock.getId());
            return;
        }
        try {
            mTmp = ByteArrayPool.getInstance().get();
            while (mBlock.getRead() < mBlock.getLength()) {
                if (state.needPause(mBlock.getId())) {
                    state.paused(mBlock.getId());
                    return;
                }
                //check network type
                if (mBlock.getNetwork() > state.getNetwork()) {
                    state.waitForNetwork(mBlock);
                    return;
                }
            }
        } finally {
            ByteArrayPool.getInstance().offer(mTmp);
        }
    }

    private static class MockMessageDigest extends MessageDigest {
        private static byte[] sEmptyBytes = new byte[0];

        /**
         * Constructs a new instance of {@code MessageDigest} with the name of
         * the algorithm to use.
         *
         * @param algorithm the name of algorithm to use
         */
        protected MockMessageDigest(String algorithm) {
            super(algorithm);
        }

        @Override
        protected void engineUpdate(byte input) {

        }

        @Override
        protected void engineUpdate(byte[] input, int offset, int len) {

        }

        @Override
        protected byte[] engineDigest() {
            return sEmptyBytes;
        }

        @Override
        protected void engineReset() {

        }
    }
}
