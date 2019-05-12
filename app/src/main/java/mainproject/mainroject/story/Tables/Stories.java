package mainproject.mainroject.story.Tables;


public class Stories {
    private String Author, storyNaMe,  Logosrc, LogoUrl, story_content, story_price, CoverUrl,STDESC,stRating,StrType,StrNameTypeSearch,StorySavingsrc,publishDate;
    int numofrankers;

    private int Likes,Reports;
     private int disLikes;

    public Stories(){}

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        this.Likes = likes;
    }

    public int getDislikes() {
        return disLikes;
    }

    public void setDislikes(int dislikes) {
        this.disLikes = dislikes;
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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

//
//    public Stories(String Authors, String logo_src, String logourl, String content, String storyprice) {
//        this.Author = Authors;
//        this.Logosrc = logo_src;
//        this.LogoUrl = logourl;
//        this.story_content = content;
//        this.story_price = storyprice;
//
//
//    }
}
