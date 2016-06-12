package gt.research.losf.gen;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class JournalGen {
    private static final String sDbRoot = "D:\\Workbench\\AndroidLOSF\\losf\\src\\main\\java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "gt.research.losf.journal.db");

        addBlock(schema);
        addFile(schema);

        new DaoGenerator().generateAll(schema, sDbRoot);
    }

    private static void addFile(Schema schema) {
        Entity file = schema.addEntity("File");
        file.addIntProperty("id").notNull().primaryKey().index();
        file.addStringProperty("file").notNull();
        file.addStringProperty("url").notNull();
        file.addStringProperty("md5").notNull();
        file.addIntProperty("state").notNull();
        file.addStringProperty("cookie");
        file.addStringProperty("etag");
    }

    private static void addBlock(Schema schema) {
        Entity block = schema.addEntity("Block");
        block.addIntProperty("id").notNull().primaryKey().index();
        block.addIntProperty("fileId").notNull();
        block.addStringProperty("url").notNull();
        block.addIntProperty("fileOffset").notNull();
        block.addIntProperty("downloadOffset").notNull();
        block.addIntProperty("read").notNull();
        block.addIntProperty("length").notNull();
        block.addStringProperty("file").notNull();
        block.addIntProperty("network").notNull();
        block.addIntProperty("retry").notNull();
        block.addStringProperty("md5").notNull();
    }
}
