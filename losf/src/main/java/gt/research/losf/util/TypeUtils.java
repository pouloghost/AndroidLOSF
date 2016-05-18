package gt.research.losf.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by GT on 2016/3/16.
 */
public class TypeUtils {
    private static Charset sUtf8Charset = Charset.forName("UTF-8");

    public static char[] bytesToChars(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return sUtf8Charset.decode(byteBuffer).array();
    }

    public static byte[] charsToBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.allocate(chars.length);
        charBuffer.put(chars);
        charBuffer.flip();
        return sUtf8Charset.encode(charBuffer).array();
    }
}
