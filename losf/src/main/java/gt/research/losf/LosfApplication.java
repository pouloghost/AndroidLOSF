package gt.research.losf;

import android.app.Application;

/**
 * Created by GT on 2016/5/30.
 */
public class LosfApplication extends Application {
    private volatile static LosfApplication sInstance;

    public static LosfApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
