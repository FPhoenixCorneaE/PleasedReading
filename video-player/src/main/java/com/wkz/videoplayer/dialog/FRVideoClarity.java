package com.wkz.videoplayer.dialog;


import java.io.Serializable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/1/29
 *     desc  : 清晰度实体类
 *     revise:
 * </pre>
 */
public class FRVideoClarity implements Serializable {

    private static final long serialVersionUID = 523120972761618256L;
    /**
     * 清晰度等级
     */
    private String grade;
    /**
     * 270P、480P、720P、1080P、4K ...
     */
    private String p;
    /**
     * 视频链接地址
     */
    private String videoUrl;

    public FRVideoClarity() {
    }

    public FRVideoClarity(String grade, String p, String videoUrl) {
        this.grade = grade;
        this.p = p;
        this.videoUrl = videoUrl;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "FRVideoClarity{" +
                "grade='" + grade + '\'' +
                ", p='" + p + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}