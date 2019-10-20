package mainproject.mainroject.story.Tables;

import java.util.Date;

/**
 * Created by UNiversaL on 4/3/2018.
 */

public class purchasedstories {
    private String Story_Publishers,purchasername,story_name,Story_Author,Story_Desc,Story_Content,Story_ImgUrl,StorySrc,strytype;
    private Date purchaseDate;
    public purchasedstories(){}
    public purchasedstories(String story_name, String story_author, String story_desc, String story_content, String story_imgUrl,String strysrc) {
        this.story_name = story_name;
        Story_Author = story_author;
        Story_Desc = story_desc;
        Story_Content = story_content;
        Story_ImgUrl = story_imgUrl;
        StorySrc =strysrc;
    }

    public String getStory_name() {
        return story_name;
    }

    public void setStory_name(String story_name) {
        this.story_name = story_name;
    }

    public String getStory_Author() {
        return Story_Author;
    }

    public void setStory_Author(String story_Author) {
        Story_Author = story_Author;
    }

    public String getStory_Desc() {
        return Story_Desc;
    }

    public void setStory_Desc(String story_Desc) {
        Story_Desc = story_Desc;
    }

    public String getStory_Content() {
        return Story_Content;
    }

    public void setStory_Content(String story_Content) {
        Story_Content = story_Content;
    }

    public String getStory_ImgUrl() {
        return Story_ImgUrl;
    }

    public void setStory_ImgUrl(String story_ImgUrl) {
        Story_ImgUrl = story_ImgUrl;
    }

    public String getPurchasername() {
        return purchasername;
    }

    public void setPurchasername(String purchasername) {
        this.purchasername = purchasername;
    }

    public String getStorySrc() {
        return StorySrc;
    }

    public void setStorySrc(String storySrc) {
        StorySrc = storySrc;
    }

    public String getStrytype() {
        return strytype;
    }

    public void setStrytype(String strytype) {
        this.strytype = strytype;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getStory_Publishers() {
        return Story_Publishers;
    }

    public void setStory_Publishers(String story_Publishers) {
        Story_Publishers = story_Publishers;
    }
}
