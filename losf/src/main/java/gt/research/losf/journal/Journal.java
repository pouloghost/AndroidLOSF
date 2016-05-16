package gt.research.losf.journal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import gt.research.losf.journal.util.BlockUtils;
import gt.research.losf.util.LogUtils;

/**
 * Created by ayi.zty on 2016/3/16.
 */
public class Journal implements IJournal {
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = -1;

    public static final int RESULT_INDEX_EXCEPTION = -1;
    public static final int RESULT_INDEX_FULL = -2;

    private static final int sCount = 100;//100 lines of journal
    private static final int sLength = BlockInfo.LENGTH * sCount;
    private RandomAccessFile mFile;

    // the position after this insertion, may be occupied.
    private int mLastIndex = -1;
    // block info count
    private int mSize = 0;

    public Journal(String name) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.exception(this, e);
            }
        }
        mFile = new RandomAccessFile(file, "rwd");
        mFile.setLength(sLength);

    }

    @Override
    public int addBlock(BlockInfo info) {
        moveToNextEmptyIndex();
        if (mLastIndex > 0) {
            try {
                info.writeToFile(mFile);
                ++mSize;
                return RESULT_SUCCESS;
            } catch (IOException e) {
                LogUtils.exception(this, e);
            }
        }
        return RESULT_FAIL;
    }

    @Override
    public int addBlock(int id, String uri, int offset) {
        return addBlock(new BlockInfo(uri, id, offset));
    }

    @Override
    public int deleteBlock(final int id) {
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                BlockInfo blockInfo = new BlockInfo(file);
                return id == blockInfo.;
            }
        }, false);
        return 0;
    }

    @Override
    public int deleteBlock(String uri) {
        return 0;
    }

    private void moveToNextEmptyIndex() {
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws IOException {
                return !BlockUtils.isCurrentInfoVailable(file);
            }
        }, false);
    }

    private void iterateOverBlocks(BlockIterateCallback callback, boolean restoreFileSeeker) {
        if (null == callback) {
            return;
        }
        int startIndex = mLastIndex;
        try {
            do {
                mLastIndex = ++mLastIndex % sCount;
                long offset = mLastIndex * BlockInfo.LENGTH;
                mFile.seek(offset);
            } while (callback.onBlock(mFile, mLastIndex) && startIndex != mLastIndex);
        } catch (Exception e) {
            LogUtils.exception(this, e);
            mLastIndex = RESULT_INDEX_EXCEPTION;
        }
        if (startIndex == mLastIndex) {
            mLastIndex = RESULT_INDEX_FULL;
        }
        if (restoreFileSeeker) {
            mLastIndex = startIndex;
            try {
                mFile.seek(mLastIndex * BlockInfo.LENGTH);
            } catch (IOException e) {
                LogUtils.exception(this, e);
            }
        }
    }

    private interface BlockIterateCallback {
        boolean onBlock(RandomAccessFile file, int index) throws Exception;
    }

}
