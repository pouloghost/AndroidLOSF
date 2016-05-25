package gt.research.losf.download.task;

import gt.research.losf.TaskConfig;
import gt.research.losf.journal.IConfigReader;
import gt.research.losf.journal.db.DBConfigReader;

/**
 * Created by GT on 2016/5/25.
 */
public class FileToDBCreator implements FileInfo.ICreator {
    private static final IConfigReader sConverter = new DBConfigReader();
    private static final ITaskConfigReader sReader = new FileTaskConfigReader();

    @Override
    public FileInfo create() {
        TaskConfig config = sReader.readConfig();
        FileInfo info = new FileInfo();
        sConverter.fill(config, info);
        return info;
    }
}
