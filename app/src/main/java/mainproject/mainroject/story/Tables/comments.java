package mainproject.mainroject.story.Tables;

/**
 * Created by UNiversaL on 4/2/2018.
 */

public class comments
{
    private String username1, usercomment,user___Email,currentstoryname;

    public comments()
    {

    }
    public comments(String userName1, String usercomment, String user___Email, String currentStoryName) {
        this.username1 = userName1;
        this.usercomment = usercomment;
        this.user___Email = user___Email;
        this.currentstoryname = currentStoryName;
    }
    public String getUserName1() {
        return username1;
    }

    public void setUserName1(String username1) {
        this.username1 = username1;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }

    public String getUser___Email() {
        return user___Email;
    }

    public void setUser___Email(String user___Email) {
        this.user___Email = user___Email;
    }

    public String getCurrentStoryName() {
        return currentstoryname;
    }

    public void setCurrentStoryName(String currentstoryname) {
        this.currentstoryname   = currentstoryname;
    }




}
