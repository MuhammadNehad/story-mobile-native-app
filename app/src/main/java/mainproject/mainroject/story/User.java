package mainproject.mainroject.story;

import com.google.firebase.database.PropertyName;

/**
 * Created by john on 10/9/2017.
 */

public class User {
    private String Email,Name,Password,UserImg,userpaypalacc,PhoneNumber, Totalpvmsgs;
    User() {
    }

    public User(String name, String mail, String password) {
        this.Name = name;
        this.Email = mail;
        this.Password = password;

    }
    @PropertyName("name")
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getMail() {
        return Email;
    }

    public void setMail(String mail) {
        this.Email = mail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
    @PropertyName("userImg")
    public String getUserImg() {
        return UserImg;
    }

    public void setUserImg(String userImg) {
        this.UserImg = userImg;
    }

    public String getTotalpvmsgs() {
        return Totalpvmsgs;
    }

    public void setTotalpvmsgs(String totalpvmsgs) {
        this.Totalpvmsgs = totalpvmsgs;
    }

    public String getUserpaypalacc() {
        return userpaypalacc;
    }

    public void setUserpaypalacc(String userpaypalacc) {
        this.userpaypalacc = userpaypalacc;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }
}