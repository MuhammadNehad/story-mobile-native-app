package mainproject.mainroject.story.Tables;

import java.util.ArrayList;

/**
 * Created by UNiversaL on 4/9/2018.
 */

public class chatdatabaseinserver {
    private String messageowner,message_content;
    private ArrayList<String> Recievers;

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
}
