package gt.research.losf.journal;

/**
 * Created by GT on 2016/5/18.
 */
public interface IBlockInfo {
    char getState();

    String getUri();

    int getBlockId();

    int getOffset();

    boolean isLegal();
}
