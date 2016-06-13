package gt.research.losf;

public class FileConfig {
    public static final int NET_WIFI = 10000;
    public static final int NET_4G = 1000;
    public static final int NET_3G = 100;
    public static final int NET_2G = 10;

    // server address
    public String url;
    // 0 - n, 0 means all condition
    public int network;
    // how many times a block can retry
    public int retry;
    // total md5, verify after download finish
    public String md5;

    public long length;

    public BlockConfig[] blocks;
}
