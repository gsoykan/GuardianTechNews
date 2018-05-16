package com.bucketsoft.user.project6newsfeedappstep1;

public class GuardianItem {

    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webURL;
    private String contributorName;


    public GuardianItem(String sectionName, String webPublicationDate, String webTitle, String webURL) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webURL = webURL;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }
}
