package sample.Model.FileManagement;

import java.io.Serializable;

/**
 * Created by Jose on 20/09/2014.
 */
public class EmailDBA implements Serializable {
    private String firstNameDBA;
    private String secondNameDBA;
    private String headerEmailDBA;
    private String bodyEmailDBA;

    public EmailDBA() {
    }

    public EmailDBA(String firstNameDBA, String secondNameDBA, String headerEmailDBA, String bodyEmailDBA) {
        this.firstNameDBA = firstNameDBA;
        this.secondNameDBA = secondNameDBA;
        this.headerEmailDBA = headerEmailDBA;
        this.bodyEmailDBA = bodyEmailDBA;
    }

    @Override
    public String toString() {
        return "EmailDBA{" +
                "firstNameDBA='" + firstNameDBA + '\'' +
                ", secondNameDBA='" + secondNameDBA + '\'' +
                ", headerEmailDBA='" + headerEmailDBA + '\'' +
                ", bodyEmailDBA='" + bodyEmailDBA + '\'' +
                '}';
    }

    public String getFirstNameDBA() {
        return firstNameDBA;
    }

    public void setFirstNameDBA(String firstNameDBA) {
        this.firstNameDBA = firstNameDBA;
    }

    public String getSecondNameDBA() {
        return secondNameDBA;
    }

    public void setSecondNameDBA(String secondNameDBA) {
        this.secondNameDBA = secondNameDBA;
    }

    public String getHeaderEmailDBA() {
        return headerEmailDBA;
    }

    public void setHeaderEmailDBA(String headerEmailDBA) {
        this.headerEmailDBA = headerEmailDBA;
    }

    public String getBodyEmailDBA() {
        return bodyEmailDBA;
    }

    public void setBodyEmailDBA(String bodyEmailDBA) {
        this.bodyEmailDBA = bodyEmailDBA;
    }
}
