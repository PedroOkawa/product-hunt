package greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here

import com.google.gson.annotations.SerializedName;
// KEEP INCLUDES END
/**
 * Entity mapped to table "THUMBNAIL".
 */
public class Thumbnail {

    // KEEP FIELDS - put your custom fields here

    private Long id;
    @SerializedName("image_url")
    private String image;
    // KEEP FIELDS END

    public Thumbnail() {
    }

    public Thumbnail(Long id) {
        this.id = id;
    }

    public Thumbnail(Long id, String image) {
        this.id = id;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
