package gt.research.losf.journal.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import gt.research.losf.journal.BlockInfo;

/**
 * Created by ayi.zty on 2016/3/28.
 */
public class BlockUtils {
    public static boolean isCurrentInfoVailable(RandomAccessFile file) throws IOException {
        char state = file.readChar();
        return state != BlockInfo.STATE_NEW && state != BlockInfo.STATE_PROGRESS;
    }
}
