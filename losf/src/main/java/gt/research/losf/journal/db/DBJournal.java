package gt.research.losf.journal.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IFileInfo;
import gt.research.losf.journal.IJournal;

/**
 * Created by GT on 2016/5/18.
 */
public class DBJournal implements IJournal {
    private static final String sDBName = "losf.db";

    private BlockDao mBlockDao;
    private FileDao mFileDao;

    public DBJournal(Context context) {
        DaoMaster dao = new DaoMaster(new DaoMaster.DevOpenHelper(context, sDBName, null).
                getWritableDatabase());
        DaoSession daoSession = dao.newSession();
        mBlockDao = daoSession.getBlockDao();
        mFileDao = daoSession.getFileDao();
    }

    @Override
    public int addBlock(IBlockInfo info) {
        if (!(info instanceof Block)) {
            return RESULT_FAIL;
        }
        long id = mBlockDao.insertOrReplace((Block) info);
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
        mBlockDao.deleteByKey(id);
        return RESULT_SUCCESS;
    }

    @Override
    public int deleteBlock(String url) {
        mBlockDao.queryBuilder().where(BlockDao.Properties.Url.eq(url)).
                buildDelete().executeDeleteWithoutDetachingEntities();
        return RESULT_SUCCESS;
    }

    @Override
    public IBlockInfo getBlock(int id) {
        Block block = mBlockDao.load(id);
        return null == block ? null : new DBBlockInfo(block);
    }

    @Override
    public List<IBlockInfo> getBlocks(String url) {
        List<Block> blocks = mBlockDao.queryBuilder().where(BlockDao.Properties.Url.eq(url)).
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
        List<Block> blocks = mBlockDao.loadAll();
        List<IBlockInfo> converts = new ArrayList<>(blocks.size());
        for (Block block : blocks) {
            converts.add(new DBBlockInfo(block));
        }
        return converts;
    }

    @Override
    public int addFile(IFileInfo info) {
        if (!(info instanceof File)) {
            return RESULT_FAIL;
        }
        long id = mFileDao.insertOrReplace((File) info);
        return id < 0 ? RESULT_FAIL : RESULT_SUCCESS;
    }

    @Override
    public int addFile(int id, String file, String url, String md5, int state) {
        return addFile(new DBFileInfo(id, file, url, md5, state));
    }

    @Override
    public int deleteFile(int id) {
        mFileDao.deleteByKey(id);
        return RESULT_SUCCESS;
    }

    @Override
    public int deleteFile(String url) {
        mFileDao.queryBuilder().where(FileDao.Properties.Url.eq(url)).
                buildDelete().executeDeleteWithoutDetachingEntities();
        return RESULT_SUCCESS;
    }

    @Override
    public IFileInfo getFile(int id) {
        File file = mFileDao.load(id);
        return null == file ? null : new DBFileInfo(file);
    }

    @Override
    public IFileInfo getFile(String url) {
        List<File> files = mFileDao.queryBuilder().where(FileDao.Properties.Url.eq(url)).
                build().list();
        if (null == files || files.isEmpty()) {
            return null;
        }
        return new DBFileInfo(files.get(0));
    }

    @Override
    public List<IFileInfo> getAllFiles() {
        List<File> files = mFileDao.loadAll();
        List<IFileInfo> converts = new ArrayList<>(files.size());
        for (File file : files) {
            converts.add(new DBFileInfo(file));
        }
        return converts;
    }

    @Override
    public IFileInfo readFileBlocks(IFileInfo info) {
        info.addBlocks(getBlocks(info.getUrl()));
        return info;
    }
}
