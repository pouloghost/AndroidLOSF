package gt.research.losf.journal.db;

import gt.research.losf.journal.IBlockInfo;

/**
 * Created by GT on 2016/5/18.
 */
public class DBBlockInfo extends Block implements IBlockInfo {
    public DBBlockInfo(Block block) {
        super(block.getId(), block.getUrl(), block.getFileOffset(),
                block.getDownloadOffset(), block.getRead(), block.getLength(),
                block.getFile(), block.getNetwork(), block.getRetry(), block.getMd5());
    }

    public DBBlockInfo(int id, String url, int fileOffset,
                       int downloadOffset, int read, int length,
                       String file, int network, int retry, String md5) {
        super(id, url, fileOffset, downloadOffset,
                read, length, file, network, retry, md5);
    }
}
