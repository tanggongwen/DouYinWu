package com.lidong.photopicker;

/**
 * 图片实体
 */
public class Image {
    public String path;
    public String name;
    public long time;
    public boolean isVideo = false;
    public Long size;

    public Image(String path, String name, long time,boolean isVideo,Long size){
        this.path = path;
        this.name = name;
        this.time = time;
        this.isVideo = isVideo;
        this.size = size;
    }

    public Image(String path, String name, long time,boolean isVideo){
        this.path = path;
        this.name = name;
        this.time = time;
        this.isVideo = isVideo;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
