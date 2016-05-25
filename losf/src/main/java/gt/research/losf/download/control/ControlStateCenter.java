package gt.research.losf.download.control;

/**
 * Created by GT on 2016/5/25.
 */
public class ControlStateCenter {
    private volatile static ControlStateCenter sInstance;

    public static ControlStateCenter getInstance() {
        if (null == sInstance) {
            synchronized (ControlStateCenter.class) {
                if (null == sInstance) {
                    sInstance = new ControlStateCenter();
                }
            }
        }
        return sInstance;
    }

    private ControlStateCenter() {
    }

    private int mNetwork;

    public synchronized int getNetwork() {
        return mNetwork;
    }
}
