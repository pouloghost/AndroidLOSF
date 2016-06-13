package gt.research.losf;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by GT on 2016/6/13.
 */
public class MD5Utils {
    public static String getMd5String(MessageDigest digest) {
        if (null == digest) {
            return null;
        }
        BigInteger integer = new BigInteger(1, digest.digest());
        return integer.toString(16);
    }
}
