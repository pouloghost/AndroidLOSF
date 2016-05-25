package gt.research.losf.journal.db;

import android.text.TextUtils;

import gt.research.losf.journal.IBlockInfo;

/**
 * Created by GT on 2016/5/18.
 */
public class DBBlockInfo extends Block implements IBlockInfo {
    public DBBlockInfo() {
        super();
    }

    public DBBlockInfo(String uri, int blockId, int offset, int network, int end, String md5) {
        super(blockId, uri, String.valueOf(STATE_NEW), offset, network, end, md5);
    }

    public DBBlockInfo(Block block) {
        this(block.getUri(), block.getId(), block.getOffset(),
                block.getNetwork(), block.getEnd(), block.getMd5());
        setState(block.getState());
    }

    @Override
    public char getBlockState() {
        return super.getState().charAt(0);
    }

    @Override
    public int getBlockId() {
        return super.getId();
    }

    @Override
    public int getNetworkLevel() {
        return super.getNetwork();
    }

    @Override
    public int getEndOffset() {
        return super.getEnd();
    }

    @Override
    public boolean isLegal() {
        char state = getBlockState();
        boolean result = STATE_PROGRESS == state ||
                STATE_DELETE == state || STATE_NEW == state;
        result &= !TextUtils.isEmpty(getUri());
        result &= getBlockId() > 0;
        result &= getOffset() > 0;
        result &= getNetworkLevel() > 0;
        result &= getEnd() >= getOffset();
        return result;
    }


}
