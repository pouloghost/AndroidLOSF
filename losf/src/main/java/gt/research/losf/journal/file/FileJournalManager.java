//package gt.research.losf.journal.file;
//
//import android.util.ArrayMap;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//import gt.research.losf.journal.IBlockInfo;
//import gt.research.losf.journal.IJournal;
//import gt.research.losf.util.LogUtils;
//
///**
// * Created by GT on 2016/3/16.
// * manages a particular type of journal
// */
//public class FileJournalManager implements IJournal {
//    public static class Factory {
//        private static final ArrayMap<String, FileJournalManager> sInstances = new ArrayMap<>();
//        private static final Random mRandom = new Random(System.currentTimeMillis());
//
//        public static FileJournalManager getJournal(String name) {
//            if (null == sInstances.get(name)) {
//                synchronized (sInstances) {
//                    if (null == sInstances.get(name)) {
//                        sInstances.put(name, new FileJournalManager(name));
//                    }
//                }
//            }
//            return sInstances.get(name);
//        }
//
//        public static int generateId() {
//            return mRandom.nextInt();
//        }
//    }
//
//    private LinkedList<IJournal> mJournals = new LinkedList<>();
//    private int mLastSuffix = 0;
//    private String mParentPath;
//    private String mNamePrefix;
//
//    private FileJournalManager(String name) {
//        final File file = new File(name);
//        ensurePath(file);
//        loadExistingJournals(file);
//    }
//
//    @Override
//    public int addBlock(IBlockInfo info) {
//        for (IJournal journal : mJournals) {
//            if (!journal.isFull()) {
//                return journal.addBlock(info);
//            }
//        }
//        IJournal journal = expandJournals();
//        if (null != journal) {
//            journal.addBlock(info);
//        }
//        return RESULT_FAIL;
//    }
//
//    @Override
//    public int addBlock(int id, String uri, int offset, int network, int end, String md5) {
//        return addBlock(new FileBlockInfo(uri, id, offset, network, end, md5));
//    }
//
//    @Override
//    public int deleteBlock(int id) {
//        for (IJournal journal : mJournals) {
//            if (0 != journal.getSize()) {
//                int result = journal.deleteBlock(id);
//                if (RESULT_SUCCESS == result) {
//                    return RESULT_SUCCESS;
//                }
//            }
//        }
//        return RESULT_FAIL;
//    }
//
//    @Override
//    public int deleteBlock(String uri) {
//        boolean deleted = false;
//        for (IJournal journal : mJournals) {
//            if (0 != journal.getSize()) {
//                int result = journal.deleteBlock(uri);
//                if (RESULT_SUCCESS == result) {
//                    deleted = true;
//                }
//            }
//        }
//        return deleted ? RESULT_SUCCESS : RESULT_FAIL;
//    }
//
//    @Override
//    public boolean isFull() {
//        return false;
//    }
//
//    @Override
//    public int getSize() {
//        int size = 0;
//        for (IJournal journal : mJournals) {
//            size += journal.getSize();
//        }
//        return size;
//    }
//
//    @Override
//    public IBlockInfo getBlock(int id) {
//        IBlockInfo blockInfo = null;
//        for (IJournal journal : mJournals) {
//            blockInfo = journal.getBlock(id);
//            if (null != blockInfo) {
//                return blockInfo;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<IBlockInfo> getBlocks(String uri) {
//        List<IBlockInfo> resultList = new LinkedList<>();
//        for (IJournal journal : mJournals) {
//            List<IBlockInfo> list = journal.getBlocks(uri);
//            if (null == list) {
//                continue;
//            }
//            resultList.addAll(list);
//        }
//        return resultList.isEmpty() ? null : resultList;
//    }
//
//    @Override
//    public List<IBlockInfo> getAllBlocks() {
//        //// TODO: 2016/5/30 add impl
//        return null;
//    }
//
//    private void ensurePath(File file) {
//        File parent = file.getParentFile();
//        if (null == parent) {
//            return;
//        }
//        parent.mkdirs();
//        mParentPath = parent.getAbsolutePath();
//    }
//
//    private void loadExistingJournals(File file) {
//        File parent = file.getParentFile();
//        mNamePrefix = file.getName();
//
//        File[] journals = parent.listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String filename) {
//                return filename.startsWith(mNamePrefix);
//            }
//        });
//
//        if (null == journals || 0 == journals.length) {
//            journals = new File[]{new File(file.getParent(), file.getName() + 0)};
//        }
//        for (File journalFile : journals) {
//            try {
//                IJournal journal = new FileJournal(journalFile);
//                if (0 == journal.getSize()) {
//                    journalFile.delete();
//                    continue;
//                }
//                mJournals.add(new FileJournal(journalFile));
//                int count = Integer.valueOf(journalFile.getName().substring(mNamePrefix.length()));
//                if (count > mLastSuffix) {
//                    mLastSuffix = count;
//                }
//            } catch (IOException e) {
//                LogUtils.exception(this, e);
//            }
//        }
//    }
//
//    private IJournal expandJournals() {
//        ++mLastSuffix;
//        try {
//            IJournal journal = new FileJournal(new File(mParentPath, mNamePrefix + mLastSuffix));
//            mJournals.add(journal);
//            return journal;
//        } catch (IOException e) {
//            LogUtils.exception(this, e);
//        }
//        return null;
//    }
//}
