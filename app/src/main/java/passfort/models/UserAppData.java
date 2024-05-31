package passfort.models;

public class UserAppData {
    private int userId;
    private String username;
    private String password;
    private String apps;

    public UserAppData(int userId, String username, String password, String apps) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.apps = apps;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApps() {
        return apps;
    }

    public void setApps(String apps) {
        this.apps = apps;
    }

    
}
