package gt.research.losf.journal.db;

import android.text.TextUtils;

import gt.research.losf.journal.IBlockInfo;

/**
 * Created by ayi.zty on 2016/5/18.
 */
public class DBBlockInfo implements IBlockInfo {
    private Block mData = new Block();

    public DBBlockInfo() {

    }

    public DBBlockInfo(String uri, int blockId, int offset) {
        mData.setState(STATE_NEW + "");
        mData.setUri(uri);
        mData.setId(blockId);
        mData.setOffset(offset);
    }

    public DBBlockInfo(Block data) {
        mData = data;
    }

    public Block getData() {
        return mData;
    }

    @Override
    public char getState() {
        return mData.getState().charAt(0);
    }

    @Override
    public String getUri() {
        return mData.getUri();
    }

    @Override
    public int getBlockId() {
        return mData.getId();
    }

    @Override
    public int getOffset() {
        return mData.getOffset();
    }

    @Override
    public boolean isLegal() {
        char state = getState();
        boolean result = STATE_PROGRESS == state ||
                STATE_DELETE == state || STATE_NEW == state;
        result &= !TextUtils.isEmpty(getUri());
        result &= getBlockId() > 0;
        result &= getOffset() > 0;
        return result;
    }
}
