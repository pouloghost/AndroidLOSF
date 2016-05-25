package gt.research.losf.gen;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class JournalGen {
    private static final String sDbRoot = "D:\\Workbench\\AndroidLOSF\\losf\\src\\main\\java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "gt.research.losf.journal.db");

        addBlock(schema);

        new DaoGenerator().generateAll(schema, sDbRoot);
    }

    private static void addBlock(Schema schema) {
        Entity block = schema.addEntity("Block");
        block.addIntProperty("id").notNull().primaryKey().index();
        block.addStringProperty("uri").notNull();
        block.addStringProperty("state").notNull();
        block.addIntProperty("offset").notNull();
        block.addIntProperty("network").notNull();
        block.addIntProperty("end").notNull();
        block.addStringProperty("md5");
    }
}
