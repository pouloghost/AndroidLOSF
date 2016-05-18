package gt.research.losf.journal;

/**
 * Created by GT on 2016/5/18.
 */
public interface IBlockInfo {
    char STATE_PROGRESS = 'p';
    char STATE_DELETE = 'd';
    char STATE_NEW = 'n';

    char getState();

    String getUri();

    int getBlockId();

    int getOffset();

    boolean isLegal();
}
