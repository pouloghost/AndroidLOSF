package gt.research.losf.journal.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;

/**
 * Created by GT on 2016/5/18.
 */
public class DBJournal implements IJournal {
    private static final String sDBName = "blocks.db";

    private BlockDao mDao;

    public DBJournal(Context context) {
        SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(context, sDBName, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        mDao = daoSession.getBlockDao();
    }

    @Override
    public int addBlock(IBlockInfo info) {
        if (!(info instanceof Block)) {
            return RESULT_FAIL;
        }
        long id = mDao.insertOrReplace((Block) info);
        return id < 0 ? RESULT_FAIL : RESULT_SUCCESS;
    }

    @Override
    public int addBlock(int id, String url, int fileOffset,
                        int downloadOffset, int read, int length,
                        String file, int network, int retry, String md5) {
        return addBlock(new DBBlockInfo(id, url, fileOffset,
                downloadOffset, read, length, file, network, retry, md5));
    }

    @Override
    public int deleteBlock(int id) {
        mDao.deleteByKey(id);
        return RESULT_SUCCESS;
    }

    @Override
    public int deleteBlock(String url) {
        mDao.queryBuilder().where(BlockDao.Properties.Url.eq(url)).
                buildDelete().executeDeleteWithoutDetachingEntities();
        return RESULT_SUCCESS;
    }

    @Override
    public IBlockInfo getBlock(int id) {
        Block block = mDao.load(id);
        return null == block ? null : new DBBlockInfo(block);
    }

    @Override
    public List<IBlockInfo> getBlocks(String url) {
        List<Block> blocks = mDao.queryBuilder().where(BlockDao.Properties.Url.eq(url)).
                build().list();
        if (null == blocks || blocks.isEmpty()) {
            return null;
        }
        List<IBlockInfo> result = new ArrayList<>(blocks.size());
        for (Block block : blocks) {
            result.add(new DBBlockInfo(block));
        }
        return result;
    }

    @Override
    public List<IBlockInfo> getAllBlocks() {
        List<Block> blocks = mDao.loadAll();
        List<IBlockInfo> converts = new ArrayList<>(blocks.size());
        for (Block block : blocks) {
            converts.add(new DBBlockInfo(block));
        }
        return converts;
    }
}
