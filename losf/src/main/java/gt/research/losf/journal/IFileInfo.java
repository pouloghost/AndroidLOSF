package gt.research.losf.journal;

import java.util.List;

/**
 * Created by GT on 2016/6/2.
 */
public interface IFileInfo {
    int STATE_NEW = 0;
    int STATE_DOWNLOADING = 1;
    int STATE_FINISH = 2;
    int STATE_FAIL = 3;
    // clear md5 if verification is ok
    String MD5_SUCCESS = "s";

    int getId();

    String getFile();

    String getUrl();

    String getMd5();

    int getState();

    void setState(int state);

    String getCookie();

    void setCookie(String cookie);

    String getEtag();

    void setEtag(String etag);

    long getLength();

    List<IBlockInfo> getBlocks();

    void addBlock(IBlockInfo block);

    void addBlocks(List<IBlockInfo> blocks);

    void ensureBlockSize(int size);

    interface ICreator {
        IFileInfo create(Object context);
    }
}
