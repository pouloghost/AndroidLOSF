package gt.research.losf.journal;

import java.util.List;

/**
 * Created by ayi.zty on 2016/3/28.
 */
public interface IJournal {
    int addBlock(FileBlockInfo info);

    int addBlock(int id, String uri, int offset);

    int deleteBlock(int id);

    int deleteBlock(String uri);

    boolean isFull();

    int getSize();

    FileBlockInfo getBlock(int id);

    List<FileBlockInfo> getBlocks(String uri);
}
