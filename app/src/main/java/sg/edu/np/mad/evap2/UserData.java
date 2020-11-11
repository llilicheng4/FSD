package sg.edu.np.mad.evap2;

public class UserData {
    private String username, email,  password;

    public UserData(){

    }

    public UserData(String myUsername, String myEmail, String myPwd){
        this.username = myUsername;
        this.email = myEmail;
        this.password = myPwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
