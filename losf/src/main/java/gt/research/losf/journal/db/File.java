package gt.research.losf.journal.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FILE".
 */
public class File {

    private int id;
    /** Not-null value. */
    private String file;
    /** Not-null value. */
    private String url;
    /** Not-null value. */
    private String md5;
    private int state;
    private long length;
    private String cookie;
    private String etag;

    public File() {
    }

    public File(int id) {
        this.id = id;
    }

    public File(int id, String file, String url, String md5, int state, long length, String cookie, String etag) {
        this.id = id;
        this.file = file;
        this.url = url;
        this.md5 = md5;
        this.state = state;
        this.length = length;
        this.cookie = cookie;
        this.etag = etag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getFile() {
        return file;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFile(String file) {
        this.file = file;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

    /** Not-null value. */
    public String getMd5() {
        return md5;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

}
