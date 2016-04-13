package gt.research.losf.journal;

/**
 * Created by ayi.zty on 2016/3/28.
 */
public interface IJournal {
    int addBlock(BlockInfo info);

    int addBlock(int id, String uri, int offset);

    int deleteBlock(int id);

    int deleteBlock(String uri);
}
