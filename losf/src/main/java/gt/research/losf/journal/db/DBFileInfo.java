package gt.research.losf.journal.db;

import java.util.ArrayList;
import java.util.List;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IFileInfo;

/**
 * Created by GT on 2016/6/2.
 */
public class DBFileInfo extends File implements IFileInfo {
    private List<IBlockInfo> mBlocks;

    public DBFileInfo() {

    }

    public DBFileInfo(File file) {
        super(file.getId(), file.getFile(), file.getUrl(),
                file.getMd5(), file.getState());
    }

    public DBFileInfo(int id, String file, String url, String md5, int state) {
        super(id, file, url, md5, state);
    }

    @Override
    public List<IBlockInfo> getBlocks() {
        return mBlocks;
    }

    @Override
    public void addBlock(IBlockInfo block) {
        mBlocks.add(block);
    }

    @Override
    public void addBlocks(List<IBlockInfo> blocks) {
        mBlocks.addAll(blocks);
    }

    @Override
    public void ensureBlockSize(int size) {
        mBlocks = new ArrayList<>(size);
    }
}
