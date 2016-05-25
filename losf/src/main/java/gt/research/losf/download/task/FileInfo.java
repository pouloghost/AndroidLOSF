package gt.research.losf.download.task;

import android.util.SparseArray;

import gt.research.losf.journal.IBlockInfo;

/**
 * Created by GT on 2016/5/25.
 */
public class FileInfo {
    private SparseArray<IBlockInfo> mBlocks;
    private String mUrl;
    private int mNetwork;
    private char mState;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getNetwork() {
        return mNetwork;
    }

    public void setNetwork(int network) {
        mNetwork = network;
    }

    public char getState() {
        return mState;
    }

    public void setState(char state) {
        mState = state;
    }

    public void ensureBlockSize(int size) {
        mBlocks = new SparseArray<>(size);
    }

    public void addBlock(IBlockInfo info) {
        mBlocks.put(info.getBlockId(), info);
    }

    public interface ICreator {
        FileInfo create();
    }
}
