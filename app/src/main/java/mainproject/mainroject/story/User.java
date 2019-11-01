package mainproject.mainroject.story;

import com.google.firebase.database.PropertyName;

import java.util.Date;

/**
 * Created by john on 10/9/2017.
 */

public class User {
    private String Email,Name,Password,UserImg,userpaypalacc,PhoneNumber, Totalpvmsgs;
    private Double storageUserSize;
    private Date RegisterDate;
    private boolean disclaimerchecked,policiesAndTermsChecked;
    private int TotalVotesTo,TotalVotesAgainst,TotalHonestVotes;
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

    public int getTotalHonestVotes() {
        return TotalHonestVotes;
    }

    public void setTotalHonestVotes(int totalHonestVotes) {
        TotalHonestVotes = totalHonestVotes;
    }

    public int getTotalVotesAgainst() {
        return TotalVotesAgainst;
    }

    public void setTotalVotesAgainst(int totalVotesAgainst) {
        TotalVotesAgainst = totalVotesAgainst;
    }

    public int getTotalVotesTo() {
        return TotalVotesTo;
    }

    public void setTotalVotesTo(int totalVotesTo) {
        TotalVotesTo = totalVotesTo;
    }

    public Double getStorageUserSize() {
        return storageUserSize;
    }

    public void setStorageUserSize(Double storageUserSize) {
        this.storageUserSize = storageUserSize;
    }

    public Date getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(Date registerDate) {
        RegisterDate = registerDate;
    }

    public boolean isPoliciesAndTermsChecked() {
        return policiesAndTermsChecked;
    }

    public void setPoliciesAndTermsChecked(boolean policiesAndTermsChecked) {
        this.policiesAndTermsChecked = policiesAndTermsChecked;
    }

    public boolean isDisclaimerchecked() {
        return disclaimerchecked;
    }

    public void setDisclaimerchecked(boolean disclaimerchecked) {
        this.disclaimerchecked = disclaimerchecked;
    }
}
