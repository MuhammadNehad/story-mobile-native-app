package mainproject.mainroject.story.Tables;

public class Images {
    private String ImageName;
    private String ImgUrl;
    private String OwnerEmail;
    public Images() {
    }
    public Images(String ImgName,String ImgUrl1, String ownerEmail) {
        this.ImgUrl= ImgUrl1;

        this.ImageName= ImgName;
        this.OwnerEmail = ownerEmail;

    }
    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getOwnerEmail() {
        return OwnerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        OwnerEmail = ownerEmail;
    }



}
