package com.okawa.pedro.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoManager {

    private static final int DATABASE_VERSION = 1;
    private static final String PACKAGE_NAME = "greendao";
    private static final String OUTPUT_DIR = "../app/src/main/java-gen";

    private static final String ENTITY_SESSION = "Session";
    private static final String FIELD_SESSION_ACCESS_TOKEN = "accessToken";
    private static final String FIELD_SESSION_TOKEN_TYPE = "tokenType";
    private static final String FILED_SESSION_EXPIRES_IN = "expiresIn";

    private static final String ENTITY_CATEGORY = "Category";
    private static final String FIELD_CATEGORY_ID = "categoryId";
    private static final String FIELD_CATEGORY_SLUG = "slug";
    private static final String FIELD_CATEGORY_NAME = "name";
    private static final String FIELD_CATEGORY_ITEM_NAME = "itemName";

    private static final String ENTITY_USER = "User";
    private static final String FIELD_USER_ID = "userId";
    private static final String FIELD_USER_CREATED_AT = "createdAt";
    private static final String FIELD_USER_NAME = "name";
    private static final String FIELD_USER_IMAGE = "image";
    private static final String FIELD_USER_USERNAME = "username";
    private static final String FIELD_USER_HEADLINE = "headline";
    private static final String FIELD_USER_TWITTER_USER = "twitterUser";
    private static final String FIELD_USER_TWITTER_PROFILE = "twitterProfile";
    private static final String FIELD_USER_POSTS = "posts";

    private static final String ENTITY_POST = "Post";
    private static final String FIELD_POST_ID = "postId";
    private static final String FIELD_POST_DATE = "date";
    private static final String FIELD_POST_NAME = "name";
    private static final String FIELD_POST_IMAGE = "image";
    private static final String FIELD_POST_TAGLINE = "tagline";
    private static final String FIELD_POST_VOTES_COUNT = "votesCount";
    private static final String FIELD_POST_REDIRECT_URL = "redirectUrl";
    private static final String FIELD_POST_SCREENSHOT_SMALL = "screenshotSmall";
    private static final String FIELD_POST_SCREENSHOT_BIG = "screenshotBig";
    private static final String FIELD_POST_MAKERS = "makers";
    private static final String FIELD_POST_USER = "user";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DATABASE_VERSION, PACKAGE_NAME);
        createTables(schema);
        new DaoGenerator().generateAll(schema, OUTPUT_DIR);
    }

    private static void createTables(Schema schema) {

        /* SESSION */
        Entity session = schema.addEntity(ENTITY_SESSION);

        session.addIdProperty().primaryKey();
        session.addStringProperty(FIELD_SESSION_ACCESS_TOKEN);
        session.addStringProperty(FIELD_SESSION_TOKEN_TYPE);
        session.addLongProperty(FILED_SESSION_EXPIRES_IN);

        session.setHasKeepSections(true);

        /* CATEGORY */

        Entity category = schema.addEntity(ENTITY_CATEGORY);

        category.addLongProperty(FIELD_CATEGORY_ID).primaryKey();
        category.addStringProperty(FIELD_CATEGORY_SLUG);
        category.addStringProperty(FIELD_CATEGORY_NAME);
        category.addStringProperty(FIELD_CATEGORY_ITEM_NAME);

        category.setHasKeepSections(true);

        /* USER */

        Entity user = schema.addEntity(ENTITY_USER);

        Property userId = user.addLongProperty(FIELD_USER_ID).primaryKey().getProperty();
        user.addDateProperty(FIELD_USER_CREATED_AT);
        user.addStringProperty(FIELD_USER_NAME);
        user.addStringProperty(FIELD_USER_IMAGE);
        user.addStringProperty(FIELD_USER_USERNAME);
        user.addStringProperty(FIELD_USER_HEADLINE);
        user.addStringProperty(FIELD_USER_TWITTER_USER);
        user.addStringProperty(FIELD_USER_TWITTER_PROFILE);

        user.setHasKeepSections(true);

        /* POST */

        Entity post = schema.addEntity(ENTITY_POST);

        Property postId = post.addLongProperty(FIELD_POST_ID).primaryKey().getProperty();
        post.addDateProperty(FIELD_POST_DATE);
        post.addStringProperty(FIELD_POST_NAME);
        post.addStringProperty(FIELD_POST_IMAGE);
        post.addStringProperty(FIELD_POST_TAGLINE);
        post.addLongProperty(FIELD_POST_VOTES_COUNT);
        post.addStringProperty(FIELD_POST_REDIRECT_URL);
        post.addStringProperty(FIELD_POST_SCREENSHOT_SMALL);
        post.addStringProperty(FIELD_POST_SCREENSHOT_BIG);

        post.setHasKeepSections(true);

        /* RELATIONSHIP USER */

        user.addToMany(user, postId).setName(FIELD_USER_POSTS);

        /* RELATIONSHIP POST */

        post.addToMany(user, userId).setName(FIELD_POST_MAKERS);
        post.addToOne(user, userId).setName(FIELD_POST_USER);

    }

}
