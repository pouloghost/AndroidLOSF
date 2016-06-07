package gt.research.losf.download.task;

import gt.research.losf.FileConfig;
import gt.research.losf.IFileConfigReader;
import gt.research.losf.journal.IConfigReader;
import gt.research.losf.journal.IFileInfo;
import gt.research.losf.journal.db.DBConfigReader;
import gt.research.losf.journal.db.DBFileInfo;

/**
 * Created by GT on 2016/5/25.
 */
public class FileToDBCreator implements IFileInfo.ICreator {
    private static final IConfigReader sConverter = new DBConfigReader();
    private static final IFileConfigReader sReader = new FileFileConfigReader();

    @Override
    public IFileInfo create(Object context) {
        FileConfig config = sReader.readConfig();
        IFileInfo info = new DBFileInfo();
        sConverter.fill(config, info);
        return info;
    }
}
