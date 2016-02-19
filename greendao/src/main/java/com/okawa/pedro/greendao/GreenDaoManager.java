package com.okawa.pedro.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoManager {

    private static final int DATABASE_VERSION = 1;
    private static final String PACKAGE_NAME = "greendao";
    private static final String OUTPUT_DIR = "../app/src/main/java-gen";

    private static final String ENTITY_SESSION = "Session";
    private static final String FIELD_SESSION_ACCESS_TOKEN = "accessToken";
    private static final String FIELD_SESSION_TOKEN_TYPE = "tokenType";
    private static final String FILED_SESSION_EXPIRES_IN = "expiresIn";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DATABASE_VERSION, PACKAGE_NAME);
        createTables(schema);
        new DaoGenerator().generateAll(schema, OUTPUT_DIR);
    }

    private static void createTables(Schema schema) {

        Entity Session = schema.addEntity(ENTITY_SESSION);

        Session.addIdProperty().primaryKey().unique().autoincrement();
        Session.addStringProperty(FIELD_SESSION_ACCESS_TOKEN);
        Session.addStringProperty(FIELD_SESSION_TOKEN_TYPE);
        Session.addLongProperty(FILED_SESSION_EXPIRES_IN);

    }

}
