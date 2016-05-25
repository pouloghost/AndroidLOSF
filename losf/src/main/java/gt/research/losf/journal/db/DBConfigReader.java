package gt.research.losf.journal.db;

import gt.research.losf.BlockConfig;
import gt.research.losf.TaskConfig;
import gt.research.losf.download.task.FileInfo;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IConfigReader;

/**
 * Created by GT on 2016/5/25.
 */
public class DBConfigReader implements IConfigReader {
    @Override
    public void fill(TaskConfig config, FileInfo info) {
        info.setNetwork(config.network);
        info.setState(IBlockInfo.STATE_NEW);
        info.setUrl(info.getUrl());

        info.ensureBlockSize(config.blocks.length);
        for (BlockConfig blockConfig : config.blocks) {
            DBBlockInfo blockInfo = new DBBlockInfo();
            blockInfo.setNetwork(config.network);
            blockInfo.setUri(config.url);
            blockInfo.setState(String.valueOf(IBlockInfo.STATE_NEW));

            blockInfo.setOffset(blockConfig.offset);
            blockInfo.setEnd(blockConfig.end);
            blockInfo.setMd5(blockConfig.md5);
            blockInfo.setId((int) System.currentTimeMillis());

            info.addBlock(blockInfo);
        }
    }
}
