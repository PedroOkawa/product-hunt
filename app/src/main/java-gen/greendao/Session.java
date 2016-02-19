package greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "SESSION".
 */
public class Session {

    private Long id;
    private String accessToken;
    private String tokenType;
    private Long expiresIn;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Session() {
    }

    public Session(Long id) {
        this.id = id;
    }

    public Session(Long id, String accessToken, String tokenType, Long expiresIn) {
        this.id = id;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    // KEEP METHODS - put your custom methods here

    public String getToken() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(tokenType).append(" ").append(accessToken).toString();
    }
    // KEEP METHODS END

}
