package gt.research.losf.journal.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

import gt.research.losf.common.Reference;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.util.BlockUtils;
import gt.research.losf.util.LogUtils;

/**
 * Created by GT on 2016/3/16.
 */
public class FileJournal implements IJournal {
    private static final int sCount = 10;//100 lines of journal
    private static final int sLength = FileBlockInfo.LENGTH * sCount;
    private RandomAccessFile mFile;

    // the position after this insertion, may be occupied.
    private int mLastIndex = -1;
    // block info count
    private int mSize = 0;

    public FileJournal(String name) throws IOException {
        this(new File(name));
    }

    public FileJournal(File file) throws IOException {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.exception(this, e);
            }
        }
        mFile = new RandomAccessFile(file, "rwd");
        mFile.setLength(sLength);
        readStateFromFile();
    }

    private void readStateFromFile() {
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                if (!BlockUtils.isCurrentInfoVailable(file)) {
                    ++mSize;
                }
                return false;
            }
        }, false);
    }

    @Override
    public int addBlock(FileBlockInfo info) {
        moveToNextEmptyIndex();
        if (mLastIndex >= 0) {
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
        return addBlock(new FileBlockInfo(uri, id, offset));
    }

    @Override
    public int deleteBlock(final int id) {
        int lastSize = mSize;
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                if (BlockUtils.isCurrentInfoVailable(file)) {
                    return false;
                }
                FileBlockInfo blockInfo = new FileBlockInfo(file);
                if (id == blockInfo.getBlockId()) {
                    blockInfo.setStateDeleteAndFile(file);
                    --mSize;
                    return true;
                }
                return false;
            }
        }, false);
        return lastSize == mSize ? RESULT_FAIL : RESULT_SUCCESS;
    }

    @Override
    public int deleteBlock(final String uri) {
        int lastSize = mSize;
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                if (BlockUtils.isCurrentInfoVailable(file)) {
                    return false;
                }
                FileBlockInfo blockInfo = new FileBlockInfo(file);
                if (uri.equals(blockInfo.getUri())) {
                    blockInfo.setStateDeleteAndFile(file);
                    --mSize;
                }
                return false;
            }
        }, false);
        return lastSize == mSize ? RESULT_FAIL : RESULT_SUCCESS;
    }

    @Override
    public boolean isFull() {
        return mSize == sCount;
    }

    @Override
    public int getSize() {
        return mSize;
    }

    @Override
    public IBlockInfo getBlock(final int id) {
        final Reference<FileBlockInfo> ref = new Reference<>();
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                if (BlockUtils.isCurrentInfoVailable(file)) {
                    return false;
                }
                FileBlockInfo blockInfo = new FileBlockInfo(file);
                if (id == blockInfo.getBlockId()) {
                    ref.ref = blockInfo;
                    return true;
                }
                return false;
            }
        }, true);
        return ref.ref;
    }

    @Override
    public List<IBlockInfo> getBlocks(final String uri) {
        final LinkedList<IBlockInfo> list = new LinkedList<>();
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws Exception {
                if (BlockUtils.isCurrentInfoVailable(file)) {
                    return false;
                }
                FileBlockInfo blockInfo = new FileBlockInfo(file);
                if (uri.equals(blockInfo.getUri())) {
                    list.add(blockInfo);
                }
                return false;
            }
        }, true);
        return list.isEmpty() ? null : list;
    }

    private void moveToNextEmptyIndex() {
        iterateOverBlocks(new BlockIterateCallback() {
            @Override
            public boolean onBlock(RandomAccessFile file, int index) throws IOException {
                return BlockUtils.isCurrentInfoVailable(file);
            }
        }, false);
    }

    private void iterateOverBlocks(BlockIterateCallback callback, boolean restoreFileSeeker) {
        if (null == callback) {
            return;
        }
        int startIndex = mLastIndex;
        try {
            int count = 0;
            do {
                mLastIndex = ++mLastIndex % sCount;
                long offset = mLastIndex * FileBlockInfo.LENGTH;
                mFile.seek(offset);
                ++count;
            } while (!callback.onBlock(mFile, mLastIndex) && count < sCount);
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
                long pointer = mLastIndex * FileBlockInfo.LENGTH;
                if (pointer < 0) {
                    pointer = 0;
                }
                mFile.seek(pointer);
            } catch (IOException e) {
                LogUtils.exception(this, e);
            }
        }
    }

    private interface BlockIterateCallback {
        boolean onBlock(RandomAccessFile file, int index) throws Exception;
    }

}
