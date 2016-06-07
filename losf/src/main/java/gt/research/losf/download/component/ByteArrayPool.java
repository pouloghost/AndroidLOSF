package gt.research.losf.download.component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by GT on 2016/6/7.
 */
public class ByteArrayPool {
    private static final int sSize = 1024;
    private static final int sLimit = 6;

    private volatile static ByteArrayPool sInstance;

    public static ByteArrayPool getInstance() {
        if (null == sInstance) {
            synchronized (ByteArrayPool.class) {
                if (null == sInstance) {
                    sInstance = new ByteArrayPool();
                }
            }
        }
        return sInstance;
    }

    private ByteArrayPool() {
        mPool = new LinkedList<>();
    }

    private Queue<byte[]> mPool;

    public synchronized byte[] get() {
        if (mPool.isEmpty()) {
            return new byte[sSize];
        }
        return mPool.poll();
    }

    public synchronized void offer(byte[] bytes) {
        if (null == bytes) {
            return;
        }
        if (mPool.size() >= sLimit) {
            return;
        }
        mPool.offer(bytes);
    }
}
