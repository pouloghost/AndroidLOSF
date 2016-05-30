package gt.research.losf.journal;

import gt.research.losf.LosfApplication;
import gt.research.losf.journal.db.DBJournal;

/**
 * Created by GT on 2016/5/30.
 */
public class JournalMaker {
    private static volatile IJournal sInstance;

    public static IJournal get() {
        if (null == sInstance) {
            synchronized (JournalMaker.class) {
                if (null == sInstance) {
                    sInstance = new DBJournal(LosfApplication.getInstance());
                }
            }
        }
        return sInstance;
    }
}
