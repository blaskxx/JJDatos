package sample.Model.FileManagement;

import java.io.Serializable;

/**
 * Created by Jose on 21/09/2014.
 */
public class ServerInformation implements Serializable {
    private String emailAddress;
    private String passwordAddress;

    @Override
    public String toString() {
        return "ServerInformation{" +
                "emailAddress='" + emailAddress + '\'' +
                ", passwordAddress='" + passwordAddress + '\'' +
                '}';
    }

    public ServerInformation(String emailAddress, String passwordAddress) {
        this.emailAddress = emailAddress;
        this.passwordAddress = passwordAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPasswordAddress() {
        return passwordAddress;
    }

    public void setPasswordAddress(String passwordAddress) {
        this.passwordAddress = passwordAddress;
    }

    public ServerInformation() {
    }
}
