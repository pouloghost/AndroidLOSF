package gt.research.losf.journal.db;

import android.os.SystemClock;

import gt.research.losf.BlockConfig;
import gt.research.losf.FileConfig;
import gt.research.losf.journal.IConfigReader;
import gt.research.losf.journal.IFileInfo;

import static gt.research.losf.journal.IFileInfo.STATE_NEW;

/**
 * Created by GT on 2016/5/25.
 */
public class DBConfigReader implements IConfigReader {
    @Override
    public void fill(FileConfig config, IFileInfo info) {
        if (!(info instanceof File)) {
            return;
        }
        ((File) info).setMd5(config.md5);
        ((File) info).setUrl(config.url);
        int index = config.url.lastIndexOf('/');
        String file = config.url.substring(index + 1);
        ((File) info).setFile(file);
        setId((File) info);

        info.setState(STATE_NEW);

        info.ensureBlockSize(config.blocks.length);
        for (BlockConfig blockConfig : config.blocks) {
            DBBlockInfo blockInfo = new DBBlockInfo();
            blockInfo.setMd5(blockConfig.md5);
            blockInfo.setNetwork(config.network);
            setId(blockInfo);
            blockInfo.setFile(file);
            blockInfo.setUrl(config.url);
            blockInfo.setDownloadOffset(blockConfig.offset);
            blockInfo.setFileId(info.getId());
            blockInfo.setLength(blockConfig.length);
            blockInfo.setRead(0);
            blockInfo.setRetry(config.retry);
            //// TODO: 2016/6/7 change when using losf
            blockInfo.setFileOffset(blockConfig.offset);
            info.addBlock(blockInfo);
        }
    }

    private void setId(File info) {
        //// TODO: 2016/6/7 find a better way
        info.setId((int) SystemClock.currentThreadTimeMillis());
    }

    private void setId(DBBlockInfo blockInfo) {
        blockInfo.setId((int) SystemClock.currentThreadTimeMillis());
    }
}
