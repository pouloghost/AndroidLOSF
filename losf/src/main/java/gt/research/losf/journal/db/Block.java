package gt.research.losf.journal.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "BLOCK".
 */
public class Block {

    private int id;
    /** Not-null value. */
    private String uri;
    /** Not-null value. */
    private String state;
    private int offset;


    public Block() {
    }

    public Block(int id) {
        this.id = id;
    }

    public Block(int id, String uri, String state, int offset) {
        this.id = id;
        this.uri = uri;
        this.state = state;
        this.offset = offset;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getUri() {
        return uri;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /** Not-null value. */
    public String getState() {
        return state;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setState(String state) {
        this.state = state;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
