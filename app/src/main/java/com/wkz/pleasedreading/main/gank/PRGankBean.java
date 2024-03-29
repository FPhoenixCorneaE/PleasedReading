package com.wkz.pleasedreading.main.gank;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wkz.pleasedreading.BR;

import java.io.Serializable;
import java.util.List;

public class PRGankBean extends BaseObservable implements Serializable {

    private static final long serialVersionUID = -507191316820836764L;
    /**
     * error : false
     * results : [{"_id":"5bba1b899d212261127b79d1","createdAt":"2018-10-07T14:43:21.406Z","desc":"Android自动屏幕适配插件，大大减轻你和UI设计师的工作量","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vipvym5j30ny09o758","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vipycxjj30gy09gt9j"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"web","type":"Android","url":"http://tangpj.com/2018/09/29/calces-screen/","used":true,"who":"PJ Tang"},{"_id":"5bbb01d29d2122610aba3458","createdAt":"2018-10-08T07:05:54.881Z","desc":"PixelShot是一个非常棒的Android库，可以将您应用中的任何视图保存为图像","publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/Muddz/PixelShot","used":true,"who":"lijinshanmx"},{"_id":"5bbb01f69d2122610ee409d7","createdAt":"2018-10-08T07:06:30.814Z","desc":"安卓平台下，图片或视频转化为ascii，合并视频用到ffmpeg库。后期会加入带色彩的ascii码图片或视频","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viqnrjvj30u01hctcg","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viry9ksg30uk1ib7wn"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/GodFengShen/PicOrVideoToAscii","used":true,"who":"lijinshanmx"},{"_id":"5bbb02069d21226111b86f0e","createdAt":"2018-10-08T07:06:46.371Z","desc":"高仿抖音照片电影功能。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vit6hxhg30a00hs1l1","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viu5glgg30a00ktu0z"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/yellowcath/PhotoMovie","used":true,"who":"lijinshanmx"},{"_id":"5bbb04139d21226111b86f10","createdAt":"2018-10-08T07:15:31.553Z","desc":"flutter自定义波浪view.","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viui2f0g30740aoayh"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/While1true/WaveView_flutter","used":true,"who":"lijinshanmx"},{"_id":"5bbb07ba9d2122610aba345a","createdAt":"2018-10-08T07:31:06.287Z","desc":"Flutter图片选择器。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viumciuj305k0c1ta4","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viur17lj305k0c1jtl","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viutmagj305k09w0tk","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viuxayxj305k09wgmo"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/Sh1d0w/multi_image_picker","used":true,"who":"lijinshanmx"},{"_id":"5bbb07d19d2122610aba345b","createdAt":"2018-10-08T07:31:29.33Z","desc":"仿android安卓抖音v2.5加载框控件。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vivbxuxg30hs0xke81","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vivpvwpg30hs0xku0x"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/CCY0122/douyinloadingview","used":true,"who":"lijinshanmx"},{"_id":"5bbb07ea9d2122610aba345c","createdAt":"2018-10-08T07:31:54.85Z","desc":"适用于Android的轻巧且易于使用的Audio Visualizer。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vivxuzjg308w0cjq6f","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viw1541g308w0d1dm1","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viw3xurg308w0czte0","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viw79f6g308w0bwk0k"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/gauravk95/audio-visualizer-android","used":true,"who":"lijinshanmx"},{"_id":"5bbb08259d2122610ee409db","createdAt":"2018-10-08T07:32:53.505Z","desc":"Android仿火币K线图实现。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viwdt0uj30u01hctdn","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0viwmyw0j30u01hcaco"],"publishedAt":"2018-10-08T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/fujianlian/KLineChart","used":true,"who":"lijinshanmx"},{"_id":"5b977a759d212206c1b383d3","createdAt":"2018-09-11T08:19:01.268Z","desc":"手把手教你实现抖音视频特效","publishedAt":"2018-09-19T00:00:00.0Z","source":"web","type":"Android","url":"https://www.jianshu.com/p/5bb7f2a0da90","used":true,"who":"xue5455"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    @Bindable
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
        //提示该属性刷新了
        notifyPropertyChanged(BR.error);
    }

    @Bindable
    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
        notifyPropertyChanged(BR.results);
    }

    public static class ResultsBean extends BaseObservable implements Serializable, Parcelable {
        private static final long serialVersionUID = -7027641487501151048L;
        /**
         * _id : 5bba1b899d212261127b79d1
         * createdAt : 2018-10-07T14:43:21.406Z
         * desc : Android自动屏幕适配插件，大大减轻你和UI设计师的工作量
         * images : ["https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vipvym5j30ny09o758","https://ww1.sinaimg.cn/large/0073sXn7ly1fw0vipycxjj30gy09gt9j"]
         * publishedAt : 2018-10-08T00:00:00.0Z
         * source : web
         * type : Android
         * url : http://tangpj.com/2018/09/29/calces-screen/
         * used : true
         * who : PJ Tang
         */

        @SerializedName("_id")
        private String id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;
        private String playAddr;
        private String cover;

        public ResultsBean() {
        }

        protected ResultsBean(Parcel in) {
            id = in.readString();
            createdAt = in.readString();
            desc = in.readString();
            publishedAt = in.readString();
            source = in.readString();
            type = in.readString();
            url = in.readString();
            used = in.readByte() != 0;
            who = in.readString();
            images = in.createStringArrayList();
            playAddr = in.readString();
            cover = in.readString();
        }

        public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel in) {
                return new ResultsBean(in);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
            }
        };

        @Bindable
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
            notifyPropertyChanged(BR.id);
        }

        @Bindable
        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            notifyPropertyChanged(BR.createdAt);
        }

        @Bindable
        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
            notifyPropertyChanged(BR.desc);
        }

        @Bindable
        public String getPublishedAt() {
            if (publishedAt != null && publishedAt.contains("T")) {
                publishedAt = publishedAt.replace("T", " ");
            }
            if (publishedAt != null && publishedAt.contains("Z")) {
                publishedAt = publishedAt.replace("Z", "");
            }
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
            notifyPropertyChanged(BR.publishedAt);
        }

        @Bindable
        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
            notifyPropertyChanged(BR.source);
        }

        @Bindable
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
            notifyPropertyChanged(BR.type);
        }

        @Bindable
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
            notifyPropertyChanged(BR.url);
        }

        @Bindable
        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
            notifyPropertyChanged(BR.used);
        }

        @Bindable
        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
            notifyPropertyChanged(BR.who);
        }

        @Bindable
        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
            notifyPropertyChanged(BR.images);
        }

        @Bindable
        public String getPlayAddr() {
            return playAddr;
        }

        public void setPlayAddr(String playAddr) {
            this.playAddr = playAddr;
            notifyPropertyChanged(BR.playAddr);
        }

        @Bindable
        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
            notifyPropertyChanged(BR.cover);
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "id='" + id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    ", images=" + images +
                    ", playAddr='" + playAddr + '\'' +
                    ", cover='" + cover + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(createdAt);
            dest.writeString(desc);
            dest.writeString(publishedAt);
            dest.writeString(source);
            dest.writeString(type);
            dest.writeString(url);
            dest.writeByte((byte) (used ? 1 : 0));
            dest.writeString(who);
            dest.writeStringList(images);
            dest.writeString(playAddr);
            dest.writeString(cover);
        }
    }

    @Override
    public String toString() {
        return "PRGankBean{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
