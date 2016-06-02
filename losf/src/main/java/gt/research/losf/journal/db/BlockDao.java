package gt.research.losf.journal.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import gt.research.losf.journal.db.Block;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BLOCK".
*/
public class BlockDao extends AbstractDao<Block, Integer> {

    public static final String TABLENAME = "BLOCK";

    /**
     * Properties of entity Block.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, int.class, "id", true, "ID");
        public final static Property FileId = new Property(1, int.class, "fileId", false, "FILE_ID");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property FileOffset = new Property(3, int.class, "fileOffset", false, "FILE_OFFSET");
        public final static Property DownloadOffset = new Property(4, int.class, "downloadOffset", false, "DOWNLOAD_OFFSET");
        public final static Property Read = new Property(5, int.class, "read", false, "READ");
        public final static Property Length = new Property(6, int.class, "length", false, "LENGTH");
        public final static Property File = new Property(7, String.class, "file", false, "FILE");
        public final static Property Network = new Property(8, int.class, "network", false, "NETWORK");
        public final static Property Retry = new Property(9, int.class, "retry", false, "RETRY");
        public final static Property Md5 = new Property(10, String.class, "md5", false, "MD5");
    };


    public BlockDao(DaoConfig config) {
        super(config);
    }
    
    public BlockDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BLOCK\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"FILE_ID\" INTEGER NOT NULL ," + // 1: fileId
                "\"URL\" TEXT NOT NULL ," + // 2: url
                "\"FILE_OFFSET\" INTEGER NOT NULL ," + // 3: fileOffset
                "\"DOWNLOAD_OFFSET\" INTEGER NOT NULL ," + // 4: downloadOffset
                "\"READ\" INTEGER NOT NULL ," + // 5: read
                "\"LENGTH\" INTEGER NOT NULL ," + // 6: length
                "\"FILE\" TEXT NOT NULL ," + // 7: file
                "\"NETWORK\" INTEGER NOT NULL ," + // 8: network
                "\"RETRY\" INTEGER NOT NULL ," + // 9: retry
                "\"MD5\" TEXT NOT NULL );"); // 10: md5
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BLOCK_ID ON BLOCK" +
                " (\"ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BLOCK\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Block entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getFileId());
        stmt.bindString(3, entity.getUrl());
        stmt.bindLong(4, entity.getFileOffset());
        stmt.bindLong(5, entity.getDownloadOffset());
        stmt.bindLong(6, entity.getRead());
        stmt.bindLong(7, entity.getLength());
        stmt.bindString(8, entity.getFile());
        stmt.bindLong(9, entity.getNetwork());
        stmt.bindLong(10, entity.getRetry());
        stmt.bindString(11, entity.getMd5());
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Block readEntity(Cursor cursor, int offset) {
        Block entity = new Block( //
            cursor.getInt(offset + 0), // id
            cursor.getInt(offset + 1), // fileId
            cursor.getString(offset + 2), // url
            cursor.getInt(offset + 3), // fileOffset
            cursor.getInt(offset + 4), // downloadOffset
            cursor.getInt(offset + 5), // read
            cursor.getInt(offset + 6), // length
            cursor.getString(offset + 7), // file
            cursor.getInt(offset + 8), // network
            cursor.getInt(offset + 9), // retry
            cursor.getString(offset + 10) // md5
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Block entity, int offset) {
        entity.setId(cursor.getInt(offset + 0));
        entity.setFileId(cursor.getInt(offset + 1));
        entity.setUrl(cursor.getString(offset + 2));
        entity.setFileOffset(cursor.getInt(offset + 3));
        entity.setDownloadOffset(cursor.getInt(offset + 4));
        entity.setRead(cursor.getInt(offset + 5));
        entity.setLength(cursor.getInt(offset + 6));
        entity.setFile(cursor.getString(offset + 7));
        entity.setNetwork(cursor.getInt(offset + 8));
        entity.setRetry(cursor.getInt(offset + 9));
        entity.setMd5(cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(Block entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(Block entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
