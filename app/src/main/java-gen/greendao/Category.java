package greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import com.google.gson.annotations.SerializedName;

/**
 * Entity mapped to table "CATEGORY".
 */
public class Category {

    // KEEP FIELDS - put your custom fields here

    public static final long CATEGORY_TECH_ID = 1;
    public static final long CATEGORY_GAMES_ID = 2;
    public static final long CATEGORY_PODCASTS_ID = 3;
    public static final long CATEGORY_BOOKS_ID = 4;

    private Long id;
    private String slug;
    private String name;
    @SerializedName("item_name")
    private String itemName;
    // KEEP FIELDS END

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String slug, String name, String itemName) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.itemName = itemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
