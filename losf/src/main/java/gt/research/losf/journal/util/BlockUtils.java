package gt.research.losf.journal.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import gt.research.losf.journal.IFileInfo;
import gt.research.losf.util.TypeUtils;

/**
 * Created by GT on 2016/3/28.
 */
public class BlockUtils {
    private static byte[] sReadBuffer = new byte[2];

    public static boolean isCurrentInfoVailable(RandomAccessFile file) throws IOException {
        long pointer = file.getFilePointer();
        file.read(sReadBuffer, 0, 2);
        char state = TypeUtils.bytesToChars(sReadBuffer)[0];
        file.seek(pointer);
        return state != IFileInfo.STATE_NEW && state != IFileInfo.STATE_DOWNLOADING;
    }
}
