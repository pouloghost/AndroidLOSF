package gt.research.losf.journal;

import java.util.List;

/**
 * Created by GT on 2016/3/28.
 */
public interface IJournal {
    int RESULT_SUCCESS = 0;
    int RESULT_FAIL = -1;

    int RESULT_INDEX_EXCEPTION = -1;
    int RESULT_INDEX_FULL = -2;

    int addBlock(IBlockInfo info);

    int addBlock(int id, int fileId, String url, int fileOffset, int downloadOffset,
                 int read, int length, String file,
                 int network, int retry, String md5);

    int deleteBlock(int id);

    int deleteBlock(String url);

    IBlockInfo getBlock(int id);

    List<IBlockInfo> getBlocks(String url);

    List<IBlockInfo> getAllBlocks();

    int addFile(IFileInfo info);

    int addFile(int id, String file, String url, String md5, int state);

    int deleteFile(int id);

    int deleteFile(String url);

    IFileInfo getFile(int id);

    IFileInfo getFile(String url);

    List<IFileInfo> getAllFiles();

    IFileInfo readFileBlocks(IFileInfo info);
}
