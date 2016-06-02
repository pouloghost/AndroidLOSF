package gt.research.losf.journal;

import gt.research.losf.FileConfig;

/**
 * Created by GT on 2016/5/25.
 */
public interface IConfigReader {
    void fill(FileConfig config, IFileInfo info);
}
