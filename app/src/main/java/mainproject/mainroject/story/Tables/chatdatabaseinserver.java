package mainproject.mainroject.story.Tables;

import java.util.ArrayList;

/**
 * Created by UNiversaL on 4/9/2018.
 */

public class chatdatabaseinserver {
    private String messageowner,message_content;
    private ArrayList<String> Recievers;
    private boolean viewed;
    private int respectful,Likes,disLikes,rudness,reports;

    public int getRespectful() {
        return respectful;
    }

    public void setRespectful(int respectful) {
        this.respectful = respectful;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(int disLikes) {
        this.disLikes = disLikes;
    }

    public int getRudness() {
        return rudness;
    }

    public void setRudness(int rudness) {
        this.rudness = rudness;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public chatdatabaseinserver(){}

    public String getMessageowner() {
        return messageowner;
    }

    public void setMessageowner(String messageowner) {
        this.messageowner = messageowner;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public ArrayList<String> getRecievers() {
        return Recievers;
    }

    public void setRecievers(ArrayList<String> recievers) {
        Recievers = recievers;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
