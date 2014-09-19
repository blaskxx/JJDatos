package sample.Model.FileManagement;

import java.io.Serializable;

/**
 * Created by Jose on 18/09/2014.
 */
public class LoginFile implements Serializable {
    private boolean autoLogin;
    private String user;
    private String password;
    private int port;
    private String url;
    private String nameService;
    private String privilege;
    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public LoginFile(boolean autoLogin, String user, String password, int port, String url, String nameService, String privilege, boolean rememberMe) {
        this.autoLogin = autoLogin;
        this.user = user;
        this.password = password;
        this.port = port;
        this.url = url;
        this.nameService = nameService;
        this.privilege = privilege;
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginFile{" +
                "privilege='" + privilege + '\'' +
                ", nameService='" + nameService + '\'' +
                ", url='" + url + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", user='" + user + '\'' +
                ", autoLogin=" + autoLogin +
                '}';
    }

    public LoginFile() {

    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }


}
