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
    private static final String FILED_SESSION_EXPIRES_IN = "expiresIn";

    private static final String ENTITY_CATEGORY = "Category";
    private static final String FIELD_CATEGORY_ID = "id";
    private static final String FIELD_CATEGORY_SLUG = "slug";
    private static final String FIELD_CATEGORY_NAME = "name";
    private static final String FIELD_CATEGORY_ITEM_NAME = "itemName";

    private static final String ENTITY_USER = "User";
    private static final String FIELD_USER_ID = "id";
    private static final String FIELD_USER_CREATED_AT = "createdAt";
    private static final String FIELD_USER_NAME = "name";
    private static final String FIELD_USER_USERNAME = "username";
    private static final String FIELD_USER_HEADLINE = "headline";
    private static final String FIELD_USER_TWITTER_USER = "twitterUser";
    private static final String FIELD_USER_PROFILE_URL = "profileUrl";
    private static final String FIELD_USER_POSTS = "posts";
    private static final String FIELD_USER_AVATAR = "avatar";

    private static final String ENTITY_AVATAR = "Avatar";
    private static final String FIELD_AVATAR_ID = "id";
    private static final String FIELD_AVATAR_ORIGINAL = "original";

    private static final String ENTITY_POST = "Post";
    private static final String FIELD_POST_ID = "id";
    private static final String FIELD_POST_CATEGORY_ID = "categoryIdFK";
    private static final String FIELD_POST_USER_ID = "userIdFK";
    private static final String FIELD_POST_THUMBNAIL_ID = "thumbnailIdFK";
    private static final String FIELD_POST_DATE = "date";
    private static final String FIELD_POST_NAME = "name";
    private static final String FIELD_POST_TAGLINE = "tagline";
    private static final String FIELD_POST_VOTES_COUNT = "votesCount";
    private static final String FIELD_POST_REDIRECT_URL = "redirectUrl";
    private static final String FIELD_POST_MAKERS = "makers";
    private static final String FIELD_POST_USER = "user";
    private static final String FIELD_POST_THUMBNAIL = "thumbnail";
    private static final String FIELD_POST_SCREENSHOT = "screenshot";

    private static final String ENTITY_THUMBNAIL = "Thumbnail";
    private static final String FIELD_THUMBNAIL_ID = "id";
    private static final String FIELD_THUMBNAIL_IMAGE = "image";

    private static final String ENTITY_SCREENSHOT = "Screenshot";
    private static final String FIELD_SCREENSHOT_ID = "id";
    private static final String FIELD_SCREENSHOT_BIG = "small";
    private static final String FIELD_SCREENSHOT_SMALL = "big";

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

        Property userIdPK = user.addLongProperty(FIELD_USER_ID).primaryKey().getProperty();
        user.addStringProperty(FIELD_USER_CREATED_AT);
        user.addStringProperty(FIELD_USER_NAME);
        user.addStringProperty(FIELD_USER_USERNAME);
        user.addStringProperty(FIELD_USER_HEADLINE);
        user.addStringProperty(FIELD_USER_TWITTER_USER);
        user.addStringProperty(FIELD_USER_PROFILE_URL);

        user.setHasKeepSections(true);

        /* AVATAR */

        Entity avatar = schema.addEntity(ENTITY_AVATAR);

        avatar.addLongProperty(FIELD_AVATAR_ID).primaryKey();
        avatar.addStringProperty(FIELD_AVATAR_ORIGINAL);

        avatar.setHasKeepSections(true);

        /* POST */

        Entity post = schema.addEntity(ENTITY_POST);

        Property postIdPK = post.addLongProperty(FIELD_POST_ID).primaryKey().getProperty();
        Property userIdFK = post.addLongProperty(FIELD_POST_USER_ID).getProperty();
        Property thumbnailIdFK = post.addLongProperty(FIELD_POST_THUMBNAIL_ID).getProperty();
        Property categoryIdFK = post.addLongProperty(FIELD_POST_CATEGORY_ID).getProperty();
        post.addStringProperty(FIELD_POST_DATE);
        post.addStringProperty(FIELD_POST_NAME);
        post.addStringProperty(FIELD_POST_TAGLINE);
        post.addLongProperty(FIELD_POST_VOTES_COUNT);
        post.addStringProperty(FIELD_POST_REDIRECT_URL);

        post.setHasKeepSections(true);

        /* THUMBNAIL */

        Entity thumbnail = schema.addEntity(ENTITY_THUMBNAIL);

        thumbnail.addLongProperty(FIELD_THUMBNAIL_ID).primaryKey().getProperty();
        thumbnail.addStringProperty(FIELD_THUMBNAIL_IMAGE);

        thumbnail.setHasKeepSections(true);

        /* SCREENSHOT */

        Entity screenshot = schema.addEntity(ENTITY_SCREENSHOT);

        screenshot.addLongProperty(FIELD_SCREENSHOT_ID).primaryKey();
        screenshot.addStringProperty(FIELD_SCREENSHOT_SMALL);
        screenshot.addStringProperty(FIELD_SCREENSHOT_BIG);

        screenshot.setHasKeepSections(true);

        /* RELATIONSHIP USER 1 > AVATAR 1 */

        user.addToOne(avatar, userIdPK, FIELD_USER_AVATAR);

        /* RELATIONSHIP USER 1 > POST N */

        user.addToMany(post, postIdPK, FIELD_USER_POSTS);

        /* RELATIONSHIP CATEGORY 1 > POST N */

        category.addToMany(post, categoryIdFK);

        /* RELATIONSHIP POST 1 > USER 1 */

        post.addToOne(user, userIdFK, FIELD_POST_USER);

        /* RELATIONSHIP POST 1 > MAKERS N */

        post.addToMany(user, userIdPK, FIELD_POST_MAKERS);

        /* RELATIONSHIP POST 1 > THUMBNAIL 1 */

        post.addToOne(thumbnail, thumbnailIdFK, FIELD_POST_THUMBNAIL);

        /* RELATIONSHIP POST 1 > SCREENSHOT 1 */

        post.addToOne(screenshot, postIdPK, FIELD_POST_SCREENSHOT);
    }

}
