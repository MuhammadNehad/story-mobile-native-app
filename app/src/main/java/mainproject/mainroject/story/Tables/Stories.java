package mainproject.mainroject.story.Tables;


import java.util.Date;

public class Stories {
    private String Author, storyNaMe,  Logosrc, LogoUrl, story_content, story_price, CoverUrl,STDESC,stRating,StrType,StrNameTypeSearch,StorySavingsrc
            ,subCategory,StrCatSearchObj;
    private Date publishDate;
    int numofrankers;

    private int likes,Reports;
     private int dislikes;

    public Stories(){}

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getLogosrc() {
        return Logosrc;
    }

    public void setLogosrc(String logosrc) {
        Logosrc = logosrc;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public String getStory_content() {
        return story_content;
    }

    public void setStory_content(String story_content) {
        this.story_content = story_content;
    }

    public String getStory_price() {
        return story_price;
    }

    public void setStory_price(String story_price) {
        this.story_price = story_price;
    }

    public String getStoryNaMe() {
        return storyNaMe;
    }

    public void setStoryNaMe(String storyNaMe) {
        this.storyNaMe = storyNaMe;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        CoverUrl = coverUrl;
    }

    public String getSTDESC() {
        return STDESC;
    }

    public void setSTDESC(String STDESC) {
        this.STDESC = STDESC;
    }

    public String getStRating() {
        return stRating;
    }

    public void setStRating(String stRating) {
        this.stRating = stRating;
    }

    public int getNumofrankers() {
        return numofrankers;
    }

    public void setNumofrankers(int numofrankers) {
        this.numofrankers = numofrankers;
    }

    public String getStrType() {
        return StrType;
    }

    public void setStrType(String strType) {
        StrType = strType;
    }

    public String getStrNameTypeSearch() {
        return StrNameTypeSearch;
    }

    public void setStrNameTypeSearch(String strNameTypeSearch) {
        StrNameTypeSearch = strNameTypeSearch;
    }

    public int getReports() {
        return Reports;
    }

    public void setReports(int reports) {
        Reports = reports;
    }

    public String getStorySavingsrc() {
        return StorySavingsrc;
    }

    public void setStorySavingsrc(String storySavingsrc) {
        StorySavingsrc = storySavingsrc;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

//
    public Stories(String Authors, String StoryName, String logourl, String strType) {
        this.Author = Authors;
        this.storyNaMe= StoryName;
        this.LogoUrl = logourl;
        this.StrType = strType;
//        this.story_price = storyprice;


    }

    public String getStrCatSearchObj() {
        return StrCatSearchObj;
    }

    public void setStrCatSearchObj(String strCatSearchObj) {
        StrCatSearchObj = strCatSearchObj;
    }
}
