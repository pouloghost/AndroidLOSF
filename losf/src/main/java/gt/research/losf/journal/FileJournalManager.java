package gt.research.losf.journal;

import android.util.ArrayMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ayi.zty on 2016/3/16.
 * manages a particular type of journal
 */
public class FileJournalManager implements IJournal {
    private LinkedList<IJournal> mJournals = new LinkedList<>();

    private FileJournalManager(String name) {

    }

    @Override
    public int addBlock(FileBlockInfo info) {
        return 0;
    }

    @Override
    public int addBlock(int id, String uri, int offset) {
        return 0;
    }

    @Override
    public int deleteBlock(int id) {
        return 0;
    }

    @Override
    public int deleteBlock(String uri) {
        return 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public FileBlockInfo getBlock(int id) {
        return null;
    }

    @Override
    public List<FileBlockInfo> getBlocks(String uri) {
        return null;
    }

    public static class Factory {
        private static final ArrayMap<String, FileJournalManager> sInstances = new ArrayMap<>();

        public static FileJournalManager getJournal(String name) {
            if (null == sInstances.get(name)) {
                synchronized (sInstances) {
                    if (null == sInstances.get(name)) {
                        sInstances.put(name, new FileJournalManager(name));
                    }
                }
            }
            return sInstances.get(name);
        }
    }
}
