package mainproject.mainroject.story.Tables;

import java.util.Date;

public class loginDetails {
    private String UserName, Email;
    private Date TimeLoggedin;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Date getTimeLoggedin() {
        return TimeLoggedin;
    }

    public void setTimeLoggedin(Date timeLoggedin) {
        TimeLoggedin = timeLoggedin;
    }
}
