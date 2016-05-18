package gt.research.losf.journal.file;

import android.text.TextUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.util.TypeUtils;

/**
 * Created by GT on 2016/3/16.
 */
public class FileBlockInfo implements IBlockInfo {
    public static final int STATE_LENGTH = 1;
    public static final int URI_LENGTH = 256;
    public static final int BLOCK_LENGTH = 10;
    public static final int OFFSET_LENGTH = 10;

    public static final int LENGTH = STATE_LENGTH + URI_LENGTH + BLOCK_LENGTH + OFFSET_LENGTH;

    private char mState;
    private String mUri;
    private int mBlockId;
    private int mOffset;//offset of this block in file (next reading start)

    public FileBlockInfo() {

    }

    public FileBlockInfo(String uri, int blockId, int offset) {
        mState = STATE_NEW;
        mUri = uri;
        mBlockId = blockId;
        mOffset = offset;
    }

    public FileBlockInfo(char[] raw) {
        fromChars(raw);
    }

    public FileBlockInfo(RandomAccessFile file) throws Exception {
        byte[] bytes = new byte[LENGTH];
        int read = file.read(bytes, 0, LENGTH);
        if (LENGTH != read) {
            throw new Exception("Illegal Length " + read);
        }
        fromChars(TypeUtils.bytesToChars(bytes));
    }

    @Override
    public char getState() {
        return mState;
    }

    @Override
    public String getUri() {
        return mUri;
    }

    @Override
    public int getBlockId() {
        return mBlockId;
    }

    @Override
    public int getOffset() {
        return mOffset;
    }

    @Override
    public boolean isLegal() {
        boolean result = STATE_PROGRESS == mState ||
                STATE_DELETE == mState || STATE_NEW == mState;
        result &= !TextUtils.isEmpty(mUri);
        result &= mBlockId > 0;
        result &= mOffset > 0;
        return result;
    }

    public void fromChars(char[] raw) {
        int offset = STATE_LENGTH;
        mState = raw[0];
        mUri = readValue(raw, offset, totalLength(STATE_LENGTH, URI_LENGTH));
        offset += URI_LENGTH;
        mBlockId = Integer.valueOf(readValue(raw, offset,
                totalLength(STATE_LENGTH, URI_LENGTH, BLOCK_LENGTH)));
        offset += BLOCK_LENGTH;
        mOffset = Integer.valueOf(readValue(raw, offset, totalLength(LENGTH)));
    }

    public char[] toRaw() {
        int offset = STATE_LENGTH;
        char[] raw = new char[LENGTH];
        raw[0] = mState;
        offset = fillSpace(raw, mUri, offset, totalLength(STATE_LENGTH, URI_LENGTH));
        offset = fillSpace(raw, String.valueOf(mBlockId), offset,
                totalLength(STATE_LENGTH, URI_LENGTH, BLOCK_LENGTH));
        fillSpace(raw, String.valueOf(mOffset), offset, totalLength(LENGTH));
        return raw;
    }

    public void writeToFile(RandomAccessFile file) throws IOException {
        byte[] bytes = TypeUtils.charsToBytes(toRaw());
        file.write(bytes);
    }

    public void setStateAndFile(RandomAccessFile file, char state) throws IOException {
        mState = state;
        file.writeChar(mState);
    }

    private int fillSpace(char[] raw, String value, int offset, int end) {
        System.arraycopy(value.toCharArray(), 0, raw, offset, value.length());
        offset += value.length();
        if (offset >= end) {
            return end;
        }
        Arrays.fill(raw, offset, Math.min(end + 1, raw.length), ' ');
        return end;
    }

    private String readValue(char[] raw, int offset, int end) {
        int i = end - 1;
        for (; i >= offset; --i) {
            if (' ' != raw[i]) {
                break;
            }
        }
        return String.valueOf(raw, offset, i - offset + 1);
    }

    private int totalLength(int... lengths) {
        int sum = 0;
        for (int length : lengths) {
            sum += length;
        }
        return sum;
    }
}