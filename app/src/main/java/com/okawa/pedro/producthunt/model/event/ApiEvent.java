package com.okawa.pedro.producthunt.model.event;

/**
 * Created by pokawa on 25/02/16.
 */
public class ApiEvent {

    public static final int PROCESS_SESSION_ID = 0x0000;
    public static final int PROCESS_CATEGORIES_ID = 0x0001;
    public static final int PROCESS_POSTS_ID = 0x0002;
    public static final int PROCESS_COMMENTS_ID = 0x0003;
    public static final int PROCESS_VOTES_ID = 0x0004;

    private int type;
    private boolean error;
    private String message;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
