package gt.research.losf.journal.db;

import gt.research.losf.FileConfig;
import gt.research.losf.journal.IConfigReader;
import gt.research.losf.journal.IFileInfo;

/**
 * Created by GT on 2016/5/25.
 */
public class DBConfigReader implements IConfigReader {
    @Override
    public void fill(FileConfig config, IFileInfo info) {
        if (!(info instanceof File)) {
            return;
        }

    }
}
