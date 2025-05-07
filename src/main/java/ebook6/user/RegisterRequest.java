package ebook6.user;

/**
 * Class to define formatting of a registration request
 *
 * @author Xiaoyao Yu
 * Created by Xiaoyao Yu
 */

public class RegisterRequest {
    private String email;
    private String password;
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
