package gt.research.losf.journal;

import android.util.ArrayMap;

import java.io.IOException;
import java.io.RandomAccessFile;

import gt.research.losf.util.LogUtils;

/**
 * Created by ayi.zty on 2016/3/16.
 * manages a particular type of journal
 */
public class JournalManager {
    private RandomAccessFile mFile;

    private JournalManager(String name) {

    }

    public static class Factory {
        private static final ArrayMap<String, JournalManager> sInstances = new ArrayMap<>();

        public static JournalManager getJournal(String name) {
            if (null == sInstances.get(name)) {
                synchronized (sInstances) {
                    if (null == sInstances.get(name)) {
                        sInstances.put(name, new JournalManager(name));
                    }
                }
            }
            return sInstances.get(name);
        }
    }
}
