package gt.research.losf.journal.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;

/**
 * Created by ayi.zty on 2016/5/18.
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
    public int addBlock(int id, String uri, int offset) {
        return addBlock(new DBBlockInfo(uri, id, offset));
    }

    @Override
    public int deleteBlock(int id) {
        mDao.deleteByKey(id);
        return RESULT_SUCCESS;
    }

    @Override
    public int deleteBlock(String uri) {
        mDao.queryBuilder().where(BlockDao.Properties.Uri.eq(uri)).
                buildDelete().executeDeleteWithoutDetachingEntities();
        return RESULT_SUCCESS;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public IBlockInfo getBlock(int id) {
        Block block = mDao.load(id);
        return null == block ? null : new DBBlockInfo(block);
    }

    @Override
    public List<IBlockInfo> getBlocks(String uri) {
        List<Block> blocks = mDao.queryBuilder().where(BlockDao.Properties.Uri.eq(uri)).
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
}
