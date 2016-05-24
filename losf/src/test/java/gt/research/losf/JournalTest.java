package gt.research.losf;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.file.FileBlockInfo;
import gt.research.losf.journal.file.FileJournal;

/**
 * Created by GT on 2016/5/17.
 */
public class JournalTest {
    private IJournal mJournal;

    @Before
    public void setup() {
        try {
            mJournal = new FileJournal("aaa");
        } catch (IOException e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void size() {
        Assert.assertEquals(mJournal.getSize(), 1);
    }

    @Test
    public void add() {
        mJournal.addBlock(1, "f", 0);
        Assert.assertEquals(mJournal.getSize(), 1);
        Assert.assertEquals(mJournal.isFull(), false);
        IBlockInfo blockInfo = mJournal.getBlock(1);
        Assert.assertNotNull(blockInfo);
        Assert.assertEquals(2, blockInfo.getBlockId());
        Assert.assertEquals("f", blockInfo.getUri());
        Assert.assertEquals(0, blockInfo.getOffset());
        Assert.assertEquals(FileBlockInfo.STATE_NEW, blockInfo.getBlockState());
    }

    @Test
    public void delete() {
        mJournal.deleteBlock(1);
        Assert.assertEquals(mJournal.getSize(), 0);
        Assert.assertEquals(mJournal.isFull(), false);
        IBlockInfo blockInfo = mJournal.getBlock(1);
        Assert.assertNull(blockInfo);
    }
}
