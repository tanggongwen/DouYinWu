package com.lidong.photopicker;


/**
 * 本地视频文件的数据库信息
 */
public class VideoFile {
    public VideoFile() {
    }

    private int _id;

    private String filePath;//文件的本地Uri

    private String desc;      // 文件的描述

    private String createTime;// 文件创建时间

    private long fileSize;    // 文件大小

    private long fileLength;  // 文件时长

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
