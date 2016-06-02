package gt.research.losf.journal;

/**
 * Created by GT on 2016/5/18.
 */
public interface IBlockInfo {
    int getId();

    String getUrl();

    int getFileOffset();

    int getDownloadOffset();

    int getRead();

    void setRead(int read);

    int getLength();

    String getFile();

    int getNetwork();

    int getRetry();

    void setRetry(int retry);

    String getMd5();
}
