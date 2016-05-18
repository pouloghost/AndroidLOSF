package gt.research.losf.journal.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import gt.research.losf.journal.db.Block;

import gt.research.losf.journal.db.BlockDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig blockDaoConfig;

    private final BlockDao blockDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        blockDaoConfig = daoConfigMap.get(BlockDao.class).clone();
        blockDaoConfig.initIdentityScope(type);

        blockDao = new BlockDao(blockDaoConfig, this);

        registerDao(Block.class, blockDao);
    }
    
    public void clear() {
        blockDaoConfig.getIdentityScope().clear();
    }

    public BlockDao getBlockDao() {
        return blockDao;
    }

}
