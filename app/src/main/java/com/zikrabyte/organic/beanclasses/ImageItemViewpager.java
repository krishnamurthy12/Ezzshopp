package com.zikrabyte.organic.beanclasses;


/**
 * Created by Rudresh J R on 02-05-2017.
 */

public class ImageItemViewpager {
    private int imageId;
    private String heading, subheading, description;

    public ImageItemViewpager(int imageId, String heading, String subheading, String description) {
        this.imageId = imageId;
        this.heading = heading;
        this.subheading = subheading;
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
