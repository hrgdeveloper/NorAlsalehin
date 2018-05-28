package com.developer.hrg.noralsalehin.Helps;

/**
 * Created by hamid on 5/25/2018.
 */

public class SimpleResponse {
    boolean error ;
    String message ;

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

    public SimpleResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
}
