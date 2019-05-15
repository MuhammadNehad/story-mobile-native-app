package mainproject.mainroject.story.Tables;

public class Followings {
    private String ID,CurUserFollowingName, CurrentUserName;
    private int voteAgainst, voteTo, voteHonor;

    public int getVoteAgainst() {
        return voteAgainst;
    }

    public void setVoteAgainst(int voteAgainst) {
        this.voteAgainst = voteAgainst;
    }

    public int getVoteTo() {
        return voteTo;
    }

    public void setVoteTo(int voteTo) {
        this.voteTo = voteTo;
    }

    public int getVoteHonor() {
        return voteHonor;
    }

    public void setVoteHonor(int voteHonor) {
        this.voteHonor = voteHonor;
    }

    public String getCurUserFollowingName() {
        return CurUserFollowingName;
    }

    public void setCurUserFollowingName(String curUserFollowingName) {
        CurUserFollowingName = curUserFollowingName;
    }

    public String getCurrentUserName() {
        return CurrentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        CurrentUserName = currentUserName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
