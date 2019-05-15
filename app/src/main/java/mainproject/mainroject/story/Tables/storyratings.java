package mainproject.mainroject.story.Tables;

/**
 * Created by UNiversaL on 4/2/2018.
 */

public class storyratings {
    private String STRYname;
    private float styrates;
    private String userEmail;
    public storyratings(String STRYname, float styrates, String userEmail) {
        this.STRYname = STRYname;
        this.styrates = styrates;
        this.userEmail = userEmail;
    }


public storyratings(){}
    public String getSTRYname() {
        return STRYname;
    }

    public void setSTRYname(String STRYname) {
        this.STRYname = STRYname;
    }

    public float getStyrates() {
        return styrates;
    }

    public void setStyrates(float styrates) {
        this.styrates = styrates;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
