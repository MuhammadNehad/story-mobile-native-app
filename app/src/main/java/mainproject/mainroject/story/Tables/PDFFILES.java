package mainproject.mainroject.story.Tables;

/**
 * Created by UNiversaL on 4/4/2018.
 */

public class PDFFILES {
    private String PdfAuthor, PdfLogoSrc, PdfSTDESC, PdfstoryNaMe,stRating,LogoUrl,StrType;
    private String Pdfstory_price;
    private String story_content;

    private int likes,Reports;
    private int dislikes;
    public PDFFILES(){}

    public String getPdfAuthor() {
        return PdfAuthor;
    }

    public void setPdfAuthor(String pdfAuthor) {
        PdfAuthor = pdfAuthor;
    }

    public String getPdfLogoSrc() {
        return PdfLogoSrc;
    }

    public void setPdfLogoSrc(String pdfLogoSrc) {
        PdfLogoSrc = pdfLogoSrc;
    }

    public String getPdfSTDESC() {
        return PdfSTDESC;
    }

    public void setPdfSTDESC(String pdfSTDESC) {
        PdfSTDESC = pdfSTDESC;
    }

    public String getPdfstoryNaMe() {
        return PdfstoryNaMe;
    }

    public void setPdfstoryNaMe(String pdfstoryNaMe) {
        PdfstoryNaMe = pdfstoryNaMe;
    }

    public String getStRating() {
        return stRating;
    }

    public void setStRating(String stRating) {
        this.stRating = stRating;
    }

    public String getPdfstory_price() {
        return Pdfstory_price;
    }

    public void setPdfstory_price(String pdfstory_price) {
        Pdfstory_price = pdfstory_price;
    }

    public String getStory_content() {
        return story_content;
    }

    public void setStory_content(String story_content) {
        this.story_content = story_content;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReports() {
        return Reports;
    }

    public void setReports(int reports) {
        Reports = reports;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getStrType() {
        return StrType;
    }

    public void setStrType(String strType) {
        StrType = strType;
    }
}
