package com.wkz.pleasedreading.main.toutiao;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wkz.pleasedreading.BR;

import java.io.Serializable;
import java.util.List;

public class PRTouTiaoVideoBean implements Observable, Serializable, Parcelable {
    private String message;
    private int total_number;
    private boolean has_more;
    private int login_status;
    private int show_et_status;
    private String post_content_hint;
    private boolean has_more_to_refresh;
    private int action_to_last_stick;
    private int feed_flag;
    private TipsBean tips;
    private int hide_topcell_count;
    private List<DataBean> data;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    public PRTouTiaoVideoBean() {
    }

    protected PRTouTiaoVideoBean(Parcel in) {
        message = in.readString();
        total_number = in.readInt();
        has_more = in.readByte() != 0;
        login_status = in.readInt();
        show_et_status = in.readInt();
        post_content_hint = in.readString();
        has_more_to_refresh = in.readByte() != 0;
        action_to_last_stick = in.readInt();
        feed_flag = in.readInt();
        tips = in.readParcelable(TipsBean.class.getClassLoader());
        hide_topcell_count = in.readInt();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<PRTouTiaoVideoBean> CREATOR = new Creator<PRTouTiaoVideoBean>() {
        @Override
        public PRTouTiaoVideoBean createFromParcel(Parcel in) {
            return new PRTouTiaoVideoBean(in);
        }

        @Override
        public PRTouTiaoVideoBean[] newArray(int size) {
            return new PRTouTiaoVideoBean[size];
        }
    };

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyChange(BR.message);
    }

    @Bindable
    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
        notifyChange(BR.total_number);
    }

    @Bindable
    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
        notifyChange(BR.has_more);
    }

    @Bindable
    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
        notifyChange(BR.login_status);
    }

    @Bindable
    public int getShow_et_status() {
        return show_et_status;
    }

    public void setShow_et_status(int show_et_status) {
        this.show_et_status = show_et_status;
        notifyChange(BR.show_et_status);
    }

    @Bindable
    public String getPost_content_hint() {
        return post_content_hint;
    }

    public void setPost_content_hint(String post_content_hint) {
        this.post_content_hint = post_content_hint;
        notifyChange(BR.post_content_hint);
    }

    @Bindable
    public boolean isHas_more_to_refresh() {
        return has_more_to_refresh;
    }

    public void setHas_more_to_refresh(boolean has_more_to_refresh) {
        this.has_more_to_refresh = has_more_to_refresh;
        notifyChange(BR.has_more_to_refresh);
    }

    @Bindable
    public int getAction_to_last_stick() {
        return action_to_last_stick;
    }

    public void setAction_to_last_stick(int action_to_last_stick) {
        this.action_to_last_stick = action_to_last_stick;
        notifyChange(BR.action_to_last_stick);
    }

    @Bindable
    public int getFeed_flag() {
        return feed_flag;
    }

    public void setFeed_flag(int feed_flag) {
        this.feed_flag = feed_flag;
        notifyChange(BR.feed_flag);
    }

    @Bindable
    public TipsBean getTips() {
        return tips;
    }

    public void setTips(TipsBean tips) {
        this.tips = tips;
        notifyChange(BR.tips);
    }

    @Bindable
    public int getHide_topcell_count() {
        return hide_topcell_count;
    }

    public void setHide_topcell_count(int hide_topcell_count) {
        this.hide_topcell_count = hide_topcell_count;
        notifyChange(BR.hide_topcell_count);
    }

    @Bindable
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        notifyChange(BR.data);
    }

    private synchronized void notifyChange(int propertyId) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.notifyChange(this, propertyId);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.add(callback);

    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry.remove(callback);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeInt(total_number);
        dest.writeByte((byte) (has_more ? 1 : 0));
        dest.writeInt(login_status);
        dest.writeInt(show_et_status);
        dest.writeString(post_content_hint);
        dest.writeByte((byte) (has_more_to_refresh ? 1 : 0));
        dest.writeInt(action_to_last_stick);
        dest.writeInt(feed_flag);
        dest.writeParcelable(tips, flags);
        dest.writeInt(hide_topcell_count);
        dest.writeTypedList(data);
    }

    public static class TipsBean implements Observable, Serializable, Parcelable {
        /**
         * type : app
         * display_duration : 2
         * display_info : 今日头条推荐引擎有20条更新
         * display_template : 今日头条推荐引擎有%s条更新
         * open_url :
         * web_url :
         * download_url :
         * app_name : 今日头条
         * package_name :
         */

        private String type;
        private int display_duration;
        private String display_info;
        private String display_template;
        private String open_url;
        private String web_url;
        private String download_url;
        private String app_name;
        private String package_name;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

        public TipsBean() {
        }

        protected TipsBean(Parcel in) {
            type = in.readString();
            display_duration = in.readInt();
            display_info = in.readString();
            display_template = in.readString();
            open_url = in.readString();
            web_url = in.readString();
            download_url = in.readString();
            app_name = in.readString();
            package_name = in.readString();
        }

        public static final Creator<TipsBean> CREATOR = new Creator<TipsBean>() {
            @Override
            public TipsBean createFromParcel(Parcel in) {
                return new TipsBean(in);
            }

            @Override
            public TipsBean[] newArray(int size) {
                return new TipsBean[size];
            }
        };

        @Bindable
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
            notifyChange(BR.type);
        }

        @Bindable
        public int getDisplay_duration() {
            return display_duration;
        }

        public void setDisplay_duration(int display_duration) {
            this.display_duration = display_duration;
            notifyChange(BR.display_duration);
        }

        @Bindable
        public String getDisplay_info() {
            return display_info;
        }

        public void setDisplay_info(String display_info) {
            this.display_info = display_info;
            notifyChange(BR.display_info);
        }

        @Bindable
        public String getDisplay_template() {
            return display_template;
        }

        public void setDisplay_template(String display_template) {
            this.display_template = display_template;
            notifyChange(BR.display_template);
        }

        @Bindable
        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
            notifyChange(BR.open_url);
        }

        @Bindable
        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
            notifyChange(BR.web_url);
        }

        @Bindable
        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
            notifyChange(BR.download_url);
        }

        @Bindable
        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
            notifyChange(BR.app_name);
        }

        @Bindable
        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
            notifyChange(BR.package_name);
        }

        private synchronized void notifyChange(int propertyId) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = new PropertyChangeRegistry();
            }
            propertyChangeRegistry.notifyChange(this, propertyId);
        }

        @Override
        public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = new PropertyChangeRegistry();
            }
            propertyChangeRegistry.add(callback);

        }

        @Override
        public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            if (propertyChangeRegistry != null) {
                propertyChangeRegistry.remove(callback);
            }
        }

        @Override
        public String toString() {
            return "TipsBean{" +
                    "type='" + type + '\'' +
                    ", display_duration=" + display_duration +
                    ", display_info='" + display_info + '\'' +
                    ", display_template='" + display_template + '\'' +
                    ", open_url='" + open_url + '\'' +
                    ", web_url='" + web_url + '\'' +
                    ", download_url='" + download_url + '\'' +
                    ", app_name='" + app_name + '\'' +
                    ", package_name='" + package_name + '\'' +
                    ", propertyChangeRegistry=" + propertyChangeRegistry +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeInt(display_duration);
            dest.writeString(display_info);
            dest.writeString(display_template);
            dest.writeString(open_url);
            dest.writeString(web_url);
            dest.writeString(download_url);
            dest.writeString(app_name);
            dest.writeString(package_name);
        }
    }

    public static class DataBean implements Observable, Serializable, Parcelable {
        /**
         * content : {"abstract":"国家大剧院建院11周年“公众开放日”纪实——歌声就在你我身边","action_extra":"{\"channel_id\": 5443492146}","action_list":[{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}],"aggr_type":1,"allow_download":false,"article_sub_type":0,"article_type":0,"article_url":"http://toutiao.com/group/6638467783703134734/","article_version":0,"ban_comment":0,"ban_danmaku":false,"behot_time":1545701217,"bury_count":0,"cell_flag":262155,"cell_layout_style":1,"cell_type":0,"comment_count":0,"content_decoration":"","control_panel":{"recommend_sponsor":{"icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","label":"帮上头条","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6638467783703134734\u0026item_id=6638467783703134734"}},"cursor":1545701217000,"danmaku_count":0,"digg_count":3,"display_url":"http://toutiao.com/group/6638467783703134734/","filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:9330931","is_selected":false,"name":"拉黑作者:国家大剧院"}],"forward_info":{"forward_count":0},"group_flags":32832,"group_id":6638467783703134734,"has_m3u8_video":false,"has_mp4_video":0,"has_video":true,"hot":0,"ignore_web_transform":1,"interaction_data":"","is_subject":false,"item_id":6638467783703134734,"item_version":0,"keywords":"国家大剧院","large_image_list":[{"height":326,"uri":"video1609/14d77000b318ede1389b0","url":"http://p3-tt.bytecdn.cn/video1609/14d77000b318ede1389b0","url_list":[{"url":"http://p3-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"},{"url":"http://p1-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"},{"url":"http://p1-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"}],"width":580}],"level":0,"log_pb":{"impr_id":"201812250926570100120590179333EB9","is_following":"0"},"media_info":{"avatar_url":"http://p3.pstatp.com/large/888e0003965c8715e7ae","follow":false,"is_star_user":false,"media_id":3437488639,"name":"国家大剧院","recommend_reason":"","recommend_type":0,"user_id":3437214977,"user_verified":true,"verified_content":""},"media_name":"国家大剧院","middle_image":{"height":360,"uri":"list/14d77000b318ede1389b0","url":"http://p9-tt.bytecdn.cn/list/300x196/14d77000b318ede1389b0.webp","url_list":[{"url":"http://p9-tt.bytecdn.cn/list/300x196/14d77000b318ede1389b0.webp"},{"url":"http://p1-tt.bytecdn.cn/list/300x196/14d77000b318ede1389b0.webp"},{"url":"http://p3-tt.bytecdn.cn/list/300x196/14d77000b318ede1389b0.webp"}],"width":640},"need_client_impr_recycle":1,"play_auth_token":"HMAC-SHA1:2.0:1545787618047816368:bab42eac5b9e4a8eb25a91fc371ad533:3B5WzOTXsoHg+Ci295WyBTqIxo4=","play_biz_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDU3ODc2MTgsInZlciI6InYxIiwiYWsiOiJiYWI0MmVhYzViOWU0YThlYjI1YTkxZmMzNzFhZDUzMyIsInN1YiI6InBnY18xMDgwcCJ9.9bXWa7OC4boYI8_qvROvaVi--yiopNYofj4oShLTg2s","publish_time":1545638726,"read_count":303,"repin_count":1,"rid":"201812250926570100120590179333EB9","share_count":6,"share_info":{"cover_image":null,"description":null,"on_suppress":0,"share_type":{"pyq":0,"qq":0,"qzone":0,"wx":0},"share_url":"https://m.toutiaoimg.cn/a6638467783703134734/?iid=5034850950\u0026app=news_article\u0026is_hit_share_recommend=0","title":"歌声就在你我身边 - 今日头条","token_type":1,"weixin_cover_image":{"height":1373,"uri":"large/tos-cn-i-0000/b4dfb94a-0752-11e9-b852-7cd30a5014de","url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/b4dfb94a-0752-11e9-b852-7cd30a5014de","url_list":[{"url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/b4dfb94a-0752-11e9-b852-7cd30a5014de"},{"url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/b4dfb94a-0752-11e9-b852-7cd30a5014de"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/b4dfb94a-0752-11e9-b852-7cd30a5014de"}],"width":1280}},"share_type":2,"share_url":"https://m.toutiaoimg.cn/a6638467783703134734/?iid=5034850950\u0026app=news_article\u0026is_hit_share_recommend=0","show_dislike":true,"show_portrait":false,"show_portrait_article":false,"source":"国家大剧院","source_icon_style":2,"source_open_url":"sslocal://profile?refer=video\u0026uid=3437214977","tag":"video_music","tag_id":6638467783703134734,"tip":0,"title":"歌声就在你我身边","ugc_recommend":{"activity":"","reason":"国家大剧院官方账号"},"url":"http://toutiao.com/group/6638467783703134734/","user_info":{"avatar_url":"http://p3.pstatp.com/thumb/888e0003965c8715e7ae","description":"国家大剧院官方账号，艺术改变生活！","follow":false,"follower_count":0,"name":"国家大剧院","schema":"sslocal://profile?uid=3437214977\u0026refer=video","user_auth_info":"{\"auth_type\":\"0\",\"auth_info\":\"国家大剧院官方账号\"}","user_id":3437214977,"user_verified":true,"verified_content":"国家大剧院官方账号"},"user_repin":0,"user_verified":1,"verified_content":"国家大剧院官方账号","video_detail_info":{"detail_video_large_image":{"height":326,"uri":"video1609/14d77000b318ede1389b0","url":"http://p9-tt.bytecdn.cn/video1609/14d77000b318ede1389b0","url_list":[{"url":"http://p9-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"},{"url":"http://p9-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"},{"url":"http://p9-tt.bytecdn.cn/video1609/14d77000b318ede1389b0"}],"width":580},"direct_play":1,"group_flags":32832,"show_pgc_subscribe":1,"video_id":"v02004720000bgg9687a1hantolaktig","video_preloading_flag":1,"video_type":0,"video_watch_count":314,"video_watching_count":0},"video_duration":90,"video_id":"v02004720000bgg9687a1hantolaktig","video_style":3}
         * code :
         */

        private String content;
        private String code;
        private String videoUrl;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            content = in.readString();
            code = in.readString();
            videoUrl = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        @Bindable
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
            notifyChange(BR.content);
        }

        @Bindable
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
            notifyChange(BR.code);
        }

        private synchronized void notifyChange(int propertyId) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = new PropertyChangeRegistry();
            }
            propertyChangeRegistry.notifyChange(this, propertyId);
        }

        @Override
        public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = new PropertyChangeRegistry();
            }
            propertyChangeRegistry.add(callback);

        }

        @Override
        public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            if (propertyChangeRegistry != null) {
                propertyChangeRegistry.remove(callback);
            }
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(content);
            dest.writeString(code);
            dest.writeString(videoUrl);
        }

        @Bindable
        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            notifyChange(BR.videoUrl);
        }

        public static class ContentBean implements Serializable, Observable, Parcelable {

            /**
             * abstract : 据@北京消防 ，12月26日早9时34分，119指挥中心接到海淀区北京交通大学东校区2号楼起火的报警，经核实，现场为2号楼实验室内学生进行垃圾渗滤液污水处理科研试验时发生爆炸，目前消防部门正在全力处置。
             * action_extra : {"channel_id": 3431225546}
             * action_list : [{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}]
             * aggr_type : 1
             * allow_download : false
             * article_sub_type : 0
             * article_type : 0
             * article_url : http://toutiao.com/group/6639166438802211342/
             * article_version : 0
             * ban_comment : 0
             * ban_danmaku : false
             * behot_time : 1545815212
             * bury_count : 3501
             * cell_flag : 262155
             * cell_layout_style : 1
             * cell_type : 0
             * comment_count : 71
             * content_decoration :
             * control_panel : {"recommend_sponsor":{"icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","label":"帮上头条","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6639166438802211342&item_id=6639166438802211342"}}
             * cursor : 1545815212999
             * danmaku_count : 0
             * digg_count : 127
             * display_url : http://toutiao.com/group/6639166438802211342/
             * filter_words : [{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:9300515","is_selected":false,"name":"拉黑作者:成都晚报"},{"id":"6:557067","is_selected":false,"name":"不想看:北京交通大学"}]
             * forward_info : {"forward_count":4}
             * group_flags : 32832
             * group_id : 6639166438802212000
             * has_m3u8_video : false
             * has_mp4_video : 0
             * has_video : true
             * hot : 0
             * ignore_web_transform : 1
             * interaction_data :
             * is_subject : false
             * item_id : 6639166438802212000
             * item_version : 0
             * keywords : 视频,北京,实验室,北京交通大学
             * large_image_list : [{"height":326,"uri":"video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url":"http://p1-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url_list":[{"url":"http://p1-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p3-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"}],"width":580}]
             * level : 0
             * log_pb : {"impr_id":"2018122617065201001206004671257CD","is_following":"0"}
             * media_info : {"avatar_url":"http://p2.pstatp.com/large/4d0004b5e2689a454f","follow":false,"is_star_user":false,"media_id":5950542483,"name":"成都晚报","recommend_reason":"","recommend_type":0,"user_id":5950542483,"user_verified":true,"verified_content":""}
             * media_name : 成都晚报
             * middle_image : {"height":352,"uri":"list/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url":"http://p3-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp","url_list":[{"url":"http://p3-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"},{"url":"http://p1-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"},{"url":"http://p1-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"}],"width":626}
             * need_client_impr_recycle : 1
             * play_auth_token : HMAC-SHA1:2.0:1545901613175298070:bab42eac5b9e4a8eb25a91fc371ad533:j25HnbaS4M2y9ENHEwpecYEouWQ=
             * play_biz_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDU5MDE2MTMsInZlciI6InYxIiwiYWsiOiJiYWI0MmVhYzViOWU0YThlYjI1YTkxZmMzNzFhZDUzMyIsInN1YiI6InBnY18xMDgwcCJ9.-DtY8SHVZEm5VlnozihmaGt88mE2X1iST0qKEAp7zc0
             * publish_time : 1545801395
             * read_count : 20161
             * repin_count : 38
             * rid : 2018122617065201001206004671257CD
             * share_count : 226
             * share_info : {"cover_image":null,"description":null,"on_suppress":0,"share_type":{"pyq":2,"qq":0,"qzone":0,"wx":0},"share_url":"https://m.toutiaoimg.cn/a6639166438802211342/?iid=5034850950&app=news_article&is_hit_share_recommend=0","title":"现场视频曝光 北京交通大学实验室发生爆炸 - 今日头条","token_type":1,"weixin_cover_image":{"height":1455,"uri":"large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38","url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38","url_list":[{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"}],"width":1378}}
             * share_type : 2
             * share_url : https://m.toutiaoimg.cn/a6639166438802211342/?iid=5034850950&app=news_article&is_hit_share_recommend=0
             * show_dislike : true
             * show_portrait : false
             * show_portrait_article : false
             * source : 成都晚报
             * source_icon_style : 4
             * source_open_url : sslocal://profile?refer=video&uid=5950542483
             * tag : video_domestic
             * tag_id : 6639166438802212000
             * tip : 0
             * title : 现场视频曝光 北京交通大学实验室发生爆炸
             * ugc_recommend : {"activity":"","reason":"成都晚报官方账号"}
             * url : http://toutiao.com/group/6639166438802211342/
             * user_info : {"avatar_url":"http://p3.pstatp.com/thumb/4d0004b5e2689a454f","description":"1956年创刊，西部最具影响力媒体之一。知成都，观天下。","follow":false,"follower_count":0,"name":"成都晚报","schema":"sslocal://profile?uid=5950542483&refer=video","user_auth_info":"{\"auth_type\":\"0\",\"auth_info\":\"成都晚报官方账号\"}","user_id":5950542483,"user_verified":true,"verified_content":"成都晚报官方账号"}
             * user_repin : 0
             * user_verified : 1
             * verified_content : 成都晚报官方账号
             * video_detail_info : {"detail_video_large_image":{"height":326,"uri":"video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url_list":[{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p3-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"}],"width":580},"direct_play":1,"group_flags":32832,"show_pgc_subscribe":1,"video_id":"v02004870000bghgt1ij2boubbp3l9hg","video_preloading_flag":1,"video_type":0,"video_watch_count":65600,"video_watching_count":0}
             * video_duration : 18
             * video_id : v02004870000bghgt1ij2boubbp3l9hg
             * video_style : 3
             */

            @SerializedName("abstract")
            private String abstractX;
            private String action_extra;
            private int aggr_type;
            private boolean allow_download;
            private int article_sub_type;
            private int article_type;
            private String article_url;
            private int article_version;
            private int ban_comment;
            private boolean ban_danmaku;
            private int behot_time;
            private int bury_count;
            private int cell_flag;
            private int cell_layout_style;
            private int cell_type;
            private int comment_count;
            private String content_decoration;
            private ControlPanelBean control_panel;
            private long cursor;
            private int danmaku_count;
            private int digg_count;
            private String display_url;
            private ForwardInfoBean forward_info;
            private int group_flags;
            private long group_id;
            private boolean has_m3u8_video;
            private int has_mp4_video;
            private boolean has_video;
            private int hot;
            private int ignore_web_transform;
            private String interaction_data;
            private boolean is_subject;
            private long item_id;
            private int item_version;
            private String keywords;
            private int level;
            private LogPbBean log_pb;
            private MediaInfoBean media_info;
            private String media_name;
            private MiddleImageBean middle_image;
            private int need_client_impr_recycle;
            private String play_auth_token;
            private String play_biz_token;
            private int publish_time;
            private int read_count;
            private int repin_count;
            private String rid;
            private int share_count;
            private ShareInfoBean share_info;
            private int share_type;
            private String share_url;
            private boolean show_dislike;
            private boolean show_portrait;
            private boolean show_portrait_article;
            private String source;
            private int source_icon_style;
            private String source_open_url;
            private String tag;
            private long tag_id;
            private int tip;
            private String title;
            private UgcRecommendBean ugc_recommend;
            private String url;
            private UserInfoBean user_info;
            private int user_repin;
            private int user_verified;
            private String verified_content;
            private VideoDetailInfoBean video_detail_info;
            private int video_duration;
            private String video_id;
            private int video_style;
            private List<ActionListBean> action_list;
            private List<FilterWordsBean> filter_words;
            private List<LargeImageListBean> large_image_list;
            private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

            public ContentBean() {
            }

            protected ContentBean(Parcel in) {
                abstractX = in.readString();
                action_extra = in.readString();
                aggr_type = in.readInt();
                allow_download = in.readByte() != 0;
                article_sub_type = in.readInt();
                article_type = in.readInt();
                article_url = in.readString();
                article_version = in.readInt();
                ban_comment = in.readInt();
                ban_danmaku = in.readByte() != 0;
                behot_time = in.readInt();
                bury_count = in.readInt();
                cell_flag = in.readInt();
                cell_layout_style = in.readInt();
                cell_type = in.readInt();
                comment_count = in.readInt();
                content_decoration = in.readString();
                cursor = in.readLong();
                danmaku_count = in.readInt();
                digg_count = in.readInt();
                display_url = in.readString();
                forward_info = in.readParcelable(ForwardInfoBean.class.getClassLoader());
                group_flags = in.readInt();
                group_id = in.readLong();
                has_m3u8_video = in.readByte() != 0;
                has_mp4_video = in.readInt();
                has_video = in.readByte() != 0;
                hot = in.readInt();
                ignore_web_transform = in.readInt();
                interaction_data = in.readString();
                is_subject = in.readByte() != 0;
                item_id = in.readLong();
                item_version = in.readInt();
                keywords = in.readString();
                level = in.readInt();
                log_pb = in.readParcelable(LogPbBean.class.getClassLoader());
                media_info = in.readParcelable(MediaInfoBean.class.getClassLoader());
                media_name = in.readString();
                middle_image = in.readParcelable(MiddleImageBean.class.getClassLoader());
                need_client_impr_recycle = in.readInt();
                play_auth_token = in.readString();
                play_biz_token = in.readString();
                publish_time = in.readInt();
                read_count = in.readInt();
                repin_count = in.readInt();
                rid = in.readString();
                share_count = in.readInt();
                share_info = in.readParcelable(ShareInfoBean.class.getClassLoader());
                share_type = in.readInt();
                share_url = in.readString();
                show_dislike = in.readByte() != 0;
                show_portrait = in.readByte() != 0;
                show_portrait_article = in.readByte() != 0;
                source = in.readString();
                source_icon_style = in.readInt();
                source_open_url = in.readString();
                tag = in.readString();
                tag_id = in.readLong();
                tip = in.readInt();
                title = in.readString();
                ugc_recommend = in.readParcelable(UgcRecommendBean.class.getClassLoader());
                url = in.readString();
                user_info = in.readParcelable(UserInfoBean.class.getClassLoader());
                user_repin = in.readInt();
                user_verified = in.readInt();
                verified_content = in.readString();
                video_detail_info = in.readParcelable(VideoDetailInfoBean.class.getClassLoader());
                video_duration = in.readInt();
                video_id = in.readString();
                video_style = in.readInt();
                action_list = in.createTypedArrayList(ActionListBean.CREATOR);
                filter_words = in.createTypedArrayList(FilterWordsBean.CREATOR);
                large_image_list = in.createTypedArrayList(LargeImageListBean.CREATOR);
            }

            public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
                @Override
                public ContentBean createFromParcel(Parcel in) {
                    return new ContentBean(in);
                }

                @Override
                public ContentBean[] newArray(int size) {
                    return new ContentBean[size];
                }
            };

            @Bindable
            public String getAbstractX() {
                return abstractX;
            }

            public void setAbstractX(String abstractX) {
                this.abstractX = abstractX;
                notifyChange(BR.abstractX);
            }

            @Bindable
            public String getAction_extra() {
                return action_extra;
            }

            public void setAction_extra(String action_extra) {
                this.action_extra = action_extra;
                notifyChange(BR.action_extra);
            }

            @Bindable
            public int getAggr_type() {
                return aggr_type;
            }

            public void setAggr_type(int aggr_type) {
                this.aggr_type = aggr_type;
                notifyChange(BR.aggr_type);
            }

            @Bindable
            public boolean getAllow_download() {
                return allow_download;
            }

            public void setAllow_download(boolean allow_download) {
                this.allow_download = allow_download;
                notifyChange(BR.allow_download);
            }

            @Bindable
            public int getArticle_sub_type() {
                return article_sub_type;
            }

            public void setArticle_sub_type(int article_sub_type) {
                this.article_sub_type = article_sub_type;
                notifyChange(BR.article_sub_type);
            }

            @Bindable
            public int getArticle_type() {
                return article_type;
            }

            public void setArticle_type(int article_type) {
                this.article_type = article_type;
                notifyChange(BR.article_type);
            }

            @Bindable
            public String getArticle_url() {
                return article_url;
            }

            public void setArticle_url(String article_url) {
                this.article_url = article_url;
                notifyChange(BR.article_url);
            }

            @Bindable
            public int getArticle_version() {
                return article_version;
            }

            public void setArticle_version(int article_version) {
                this.article_version = article_version;
                notifyChange(BR.article_version);
            }

            @Bindable
            public int getBan_comment() {
                return ban_comment;
            }

            public void setBan_comment(int ban_comment) {
                this.ban_comment = ban_comment;
                notifyChange(BR.ban_comment);
            }

            @Bindable
            public boolean getBan_danmaku() {
                return ban_danmaku;
            }

            public void setBan_danmaku(boolean ban_danmaku) {
                this.ban_danmaku = ban_danmaku;
                notifyChange(BR.ban_danmaku);
            }

            @Bindable
            public int getBehot_time() {
                return behot_time;
            }

            public void setBehot_time(int behot_time) {
                this.behot_time = behot_time;
                notifyChange(BR.behot_time);
            }

            @Bindable
            public int getBury_count() {
                return bury_count;
            }

            public void setBury_count(int bury_count) {
                this.bury_count = bury_count;
                notifyChange(BR.bury_count);
            }

            @Bindable
            public int getCell_flag() {
                return cell_flag;
            }

            public void setCell_flag(int cell_flag) {
                this.cell_flag = cell_flag;
                notifyChange(BR.cell_flag);
            }

            @Bindable
            public int getCell_layout_style() {
                return cell_layout_style;
            }

            public void setCell_layout_style(int cell_layout_style) {
                this.cell_layout_style = cell_layout_style;
                notifyChange(BR.cell_layout_style);
            }

            @Bindable
            public int getCell_type() {
                return cell_type;
            }

            public void setCell_type(int cell_type) {
                this.cell_type = cell_type;
                notifyChange(BR.cell_type);
            }

            @Bindable
            public int getComment_count() {
                return comment_count;
            }

            public void setComment_count(int comment_count) {
                this.comment_count = comment_count;
                notifyChange(BR.comment_count);
            }

            @Bindable
            public String getContent_decoration() {
                return content_decoration;
            }

            public void setContent_decoration(String content_decoration) {
                this.content_decoration = content_decoration;
                notifyChange(BR.content_decoration);
            }

            @Bindable
            public ControlPanelBean getControl_panel() {
                return control_panel;
            }

            public void setControl_panel(ControlPanelBean control_panel) {
                this.control_panel = control_panel;
                notifyChange(BR.control_panel);
            }

            @Bindable
            public long getCursor() {
                return cursor;
            }

            public void setCursor(long cursor) {
                this.cursor = cursor;
                notifyChange(BR.cursor);
            }

            @Bindable
            public int getDanmaku_count() {
                return danmaku_count;
            }

            public void setDanmaku_count(int danmaku_count) {
                this.danmaku_count = danmaku_count;
                notifyChange(BR.danmaku_count);
            }

            @Bindable
            public int getDigg_count() {
                return digg_count;
            }

            public void setDigg_count(int digg_count) {
                this.digg_count = digg_count;
                notifyChange(BR.digg_count);
            }

            @Bindable
            public String getDisplay_url() {
                return display_url;
            }

            public void setDisplay_url(String display_url) {
                this.display_url = display_url;
                notifyChange(BR.display_url);
            }

            @Bindable
            public ForwardInfoBean getForward_info() {
                return forward_info;
            }

            public void setForward_info(ForwardInfoBean forward_info) {
                this.forward_info = forward_info;
                notifyChange(BR.forward_info);
            }

            @Bindable
            public int getGroup_flags() {
                return group_flags;
            }

            public void setGroup_flags(int group_flags) {
                this.group_flags = group_flags;
                notifyChange(BR.group_flags);
            }

            @Bindable
            public long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(long group_id) {
                this.group_id = group_id;
                notifyChange(BR.group_id);
            }

            @Bindable
            public boolean getHas_m3u8_video() {
                return has_m3u8_video;
            }

            public void setHas_m3u8_video(boolean has_m3u8_video) {
                this.has_m3u8_video = has_m3u8_video;
                notifyChange(BR.has_m3u8_video);
            }

            @Bindable
            public int getHas_mp4_video() {
                return has_mp4_video;
            }

            public void setHas_mp4_video(int has_mp4_video) {
                this.has_mp4_video = has_mp4_video;
                notifyChange(BR.has_mp4_video);
            }

            @Bindable
            public boolean getHas_video() {
                return has_video;
            }

            public void setHas_video(boolean has_video) {
                this.has_video = has_video;
                notifyChange(BR.has_video);
            }

            @Bindable
            public int getHot() {
                return hot;
            }

            public void setHot(int hot) {
                this.hot = hot;
                notifyChange(BR.hot);
            }

            @Bindable
            public int getIgnore_web_transform() {
                return ignore_web_transform;
            }

            public void setIgnore_web_transform(int ignore_web_transform) {
                this.ignore_web_transform = ignore_web_transform;
                notifyChange(BR.ignore_web_transform);
            }

            @Bindable
            public String getInteraction_data() {
                return interaction_data;
            }

            public void setInteraction_data(String interaction_data) {
                this.interaction_data = interaction_data;
                notifyChange(BR.interaction_data);
            }

            @Bindable
            public boolean getIs_subject() {
                return is_subject;
            }

            public void setIs_subject(boolean is_subject) {
                this.is_subject = is_subject;
                notifyChange(BR.is_subject);
            }

            @Bindable
            public long getItem_id() {
                return item_id;
            }

            public void setItem_id(long item_id) {
                this.item_id = item_id;
                notifyChange(BR.item_id);
            }

            @Bindable
            public int getItem_version() {
                return item_version;
            }

            public void setItem_version(int item_version) {
                this.item_version = item_version;
                notifyChange(BR.item_version);
            }

            @Bindable
            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
                notifyChange(BR.keywords);
            }

            @Bindable
            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
                notifyChange(BR.level);
            }

            @Bindable
            public LogPbBean getLog_pb() {
                return log_pb;
            }

            public void setLog_pb(LogPbBean log_pb) {
                this.log_pb = log_pb;
                notifyChange(BR.log_pb);
            }

            @Bindable
            public MediaInfoBean getMedia_info() {
                return media_info;
            }

            public void setMedia_info(MediaInfoBean media_info) {
                this.media_info = media_info;
                notifyChange(BR.media_info);
            }

            @Bindable
            public String getMedia_name() {
                return media_name;
            }

            public void setMedia_name(String media_name) {
                this.media_name = media_name;
                notifyChange(BR.media_name);
            }

            @Bindable
            public MiddleImageBean getMiddle_image() {
                return middle_image;
            }

            public void setMiddle_image(MiddleImageBean middle_image) {
                this.middle_image = middle_image;
                notifyChange(BR.middle_image);
            }

            @Bindable
            public int getNeed_client_impr_recycle() {
                return need_client_impr_recycle;
            }

            public void setNeed_client_impr_recycle(int need_client_impr_recycle) {
                this.need_client_impr_recycle = need_client_impr_recycle;
                notifyChange(BR.need_client_impr_recycle);
            }

            @Bindable
            public String getPlay_auth_token() {
                return play_auth_token;
            }

            public void setPlay_auth_token(String play_auth_token) {
                this.play_auth_token = play_auth_token;
                notifyChange(BR.play_auth_token);
            }

            @Bindable
            public String getPlay_biz_token() {
                return play_biz_token;
            }

            public void setPlay_biz_token(String play_biz_token) {
                this.play_biz_token = play_biz_token;
                notifyChange(BR.play_biz_token);
            }

            @Bindable
            public int getPublish_time() {
                return publish_time;
            }

            public void setPublish_time(int publish_time) {
                this.publish_time = publish_time;
                notifyChange(BR.publish_time);
            }

            @Bindable
            public int getRead_count() {
                return read_count;
            }

            public void setRead_count(int read_count) {
                this.read_count = read_count;
                notifyChange(BR.read_count);
            }

            @Bindable
            public int getRepin_count() {
                return repin_count;
            }

            public void setRepin_count(int repin_count) {
                this.repin_count = repin_count;
                notifyChange(BR.repin_count);
            }

            @Bindable
            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
                notifyChange(BR.rid);
            }

            @Bindable
            public int getShare_count() {
                return share_count;
            }

            public void setShare_count(int share_count) {
                this.share_count = share_count;
                notifyChange(BR.share_count);
            }

            @Bindable
            public ShareInfoBean getShare_info() {
                return share_info;
            }

            public void setShare_info(ShareInfoBean share_info) {
                this.share_info = share_info;
                notifyChange(BR.share_info);
            }

            @Bindable
            public int getShare_type() {
                return share_type;
            }

            public void setShare_type(int share_type) {
                this.share_type = share_type;
                notifyChange(BR.share_type);
            }

            @Bindable
            public String getShare_url() {
                return share_url;
            }

            public void setShare_url(String share_url) {
                this.share_url = share_url;
                notifyChange(BR.share_url);
            }

            @Bindable
            public boolean getShow_dislike() {
                return show_dislike;
            }

            public void setShow_dislike(boolean show_dislike) {
                this.show_dislike = show_dislike;
                notifyChange(BR.show_dislike);
            }

            @Bindable
            public boolean getShow_portrait() {
                return show_portrait;
            }

            public void setShow_portrait(boolean show_portrait) {
                this.show_portrait = show_portrait;
                notifyChange(BR.show_portrait);
            }

            @Bindable
            public boolean getShow_portrait_article() {
                return show_portrait_article;
            }

            public void setShow_portrait_article(boolean show_portrait_article) {
                this.show_portrait_article = show_portrait_article;
                notifyChange(BR.show_portrait_article);
            }

            @Bindable
            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
                notifyChange(BR.source);
            }

            @Bindable
            public int getSource_icon_style() {
                return source_icon_style;
            }

            public void setSource_icon_style(int source_icon_style) {
                this.source_icon_style = source_icon_style;
                notifyChange(BR.source_icon_style);
            }

            @Bindable
            public String getSource_open_url() {
                return source_open_url;
            }

            public void setSource_open_url(String source_open_url) {
                this.source_open_url = source_open_url;
                notifyChange(BR.source_open_url);
            }

            @Bindable
            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
                notifyChange(BR.tag);
            }

            @Bindable
            public long getTag_id() {
                return tag_id;
            }

            public void setTag_id(long tag_id) {
                this.tag_id = tag_id;
                notifyChange(BR.tag_id);
            }

            @Bindable
            public int getTip() {
                return tip;
            }

            public void setTip(int tip) {
                this.tip = tip;
                notifyChange(BR.tip);
            }

            @Bindable
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
                notifyChange(BR.title);
            }

            @Bindable
            public UgcRecommendBean getUgc_recommend() {
                return ugc_recommend;
            }

            public void setUgc_recommend(UgcRecommendBean ugc_recommend) {
                this.ugc_recommend = ugc_recommend;
                notifyChange(BR.ugc_recommend);
            }

            @Bindable
            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
                notifyChange(BR.url);
            }

            @Bindable
            public UserInfoBean getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoBean user_info) {
                this.user_info = user_info;
                notifyChange(BR.user_info);
            }

            @Bindable
            public int getUser_repin() {
                return user_repin;
            }

            public void setUser_repin(int user_repin) {
                this.user_repin = user_repin;
                notifyChange(BR.user_repin);
            }

            @Bindable
            public int getUser_verified() {
                return user_verified;
            }

            public void setUser_verified(int user_verified) {
                this.user_verified = user_verified;
                notifyChange(BR.user_verified);
            }

            @Bindable
            public String getVerified_content() {
                return verified_content;
            }

            public void setVerified_content(String verified_content) {
                this.verified_content = verified_content;
                notifyChange(BR.verified_content);
            }

            @Bindable
            public VideoDetailInfoBean getVideo_detail_info() {
                return video_detail_info;
            }

            public void setVideo_detail_info(VideoDetailInfoBean video_detail_info) {
                this.video_detail_info = video_detail_info;
                notifyChange(BR.video_detail_info);
            }

            @Bindable
            public int getVideo_duration() {
                return video_duration;
            }

            public void setVideo_duration(int video_duration) {
                this.video_duration = video_duration;
                notifyChange(BR.video_duration);
            }

            @Bindable
            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
                notifyChange(BR.video_id);
            }

            @Bindable
            public int getVideo_style() {
                return video_style;
            }

            public void setVideo_style(int video_style) {
                this.video_style = video_style;
                notifyChange(BR.video_style);
            }

            @Bindable
            public List<ActionListBean> getAction_list() {
                return action_list;
            }

            public void setAction_list(List<ActionListBean> action_list) {
                this.action_list = action_list;
                notifyChange(BR.action_list);
            }

            @Bindable
            public List<FilterWordsBean> getFilter_words() {
                return filter_words;
            }

            public void setFilter_words(List<FilterWordsBean> filter_words) {
                this.filter_words = filter_words;
                notifyChange(BR.filter_words);
            }

            @Bindable
            public List<LargeImageListBean> getLarge_image_list() {
                return large_image_list;
            }

            public void setLarge_image_list(List<LargeImageListBean> large_image_list) {
                this.large_image_list = large_image_list;
                notifyChange(BR.large_image_list);
            }

            private synchronized void notifyChange(int propertyId) {
                if (propertyChangeRegistry == null) {
                    propertyChangeRegistry = new PropertyChangeRegistry();
                }
                propertyChangeRegistry.notifyChange(this, propertyId);
            }

            @Override
            public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                if (propertyChangeRegistry == null) {
                    propertyChangeRegistry = new PropertyChangeRegistry();
                }
                propertyChangeRegistry.add(callback);

            }

            @Override
            public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                if (propertyChangeRegistry != null) {
                    propertyChangeRegistry.remove(callback);
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(abstractX);
                dest.writeString(action_extra);
                dest.writeInt(aggr_type);
                dest.writeByte((byte) (allow_download ? 1 : 0));
                dest.writeInt(article_sub_type);
                dest.writeInt(article_type);
                dest.writeString(article_url);
                dest.writeInt(article_version);
                dest.writeInt(ban_comment);
                dest.writeByte((byte) (ban_danmaku ? 1 : 0));
                dest.writeInt(behot_time);
                dest.writeInt(bury_count);
                dest.writeInt(cell_flag);
                dest.writeInt(cell_layout_style);
                dest.writeInt(cell_type);
                dest.writeInt(comment_count);
                dest.writeString(content_decoration);
                dest.writeLong(cursor);
                dest.writeInt(danmaku_count);
                dest.writeInt(digg_count);
                dest.writeString(display_url);
                dest.writeParcelable(forward_info, flags);
                dest.writeInt(group_flags);
                dest.writeLong(group_id);
                dest.writeByte((byte) (has_m3u8_video ? 1 : 0));
                dest.writeInt(has_mp4_video);
                dest.writeByte((byte) (has_video ? 1 : 0));
                dest.writeInt(hot);
                dest.writeInt(ignore_web_transform);
                dest.writeString(interaction_data);
                dest.writeByte((byte) (is_subject ? 1 : 0));
                dest.writeLong(item_id);
                dest.writeInt(item_version);
                dest.writeString(keywords);
                dest.writeInt(level);
                dest.writeParcelable(log_pb, flags);
                dest.writeParcelable(media_info, flags);
                dest.writeString(media_name);
                dest.writeParcelable(middle_image, flags);
                dest.writeInt(need_client_impr_recycle);
                dest.writeString(play_auth_token);
                dest.writeString(play_biz_token);
                dest.writeInt(publish_time);
                dest.writeInt(read_count);
                dest.writeInt(repin_count);
                dest.writeString(rid);
                dest.writeInt(share_count);
                dest.writeParcelable(share_info, flags);
                dest.writeInt(share_type);
                dest.writeString(share_url);
                dest.writeByte((byte) (show_dislike ? 1 : 0));
                dest.writeByte((byte) (show_portrait ? 1 : 0));
                dest.writeByte((byte) (show_portrait_article ? 1 : 0));
                dest.writeString(source);
                dest.writeInt(source_icon_style);
                dest.writeString(source_open_url);
                dest.writeString(tag);
                dest.writeLong(tag_id);
                dest.writeInt(tip);
                dest.writeString(title);
                dest.writeParcelable(ugc_recommend, flags);
                dest.writeString(url);
                dest.writeParcelable(user_info, flags);
                dest.writeInt(user_repin);
                dest.writeInt(user_verified);
                dest.writeString(verified_content);
                dest.writeParcelable(video_detail_info, flags);
                dest.writeInt(video_duration);
                dest.writeString(video_id);
                dest.writeInt(video_style);
                dest.writeTypedList(action_list);
                dest.writeTypedList(filter_words);
                dest.writeTypedList(large_image_list);
            }

            public static class ControlPanelBean implements Serializable, Observable, Parcelable {
                /**
                 * recommend_sponsor : {"icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","label":"帮上头条","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6639166438802211342&item_id=6639166438802211342"}
                 */

                private RecommendSponsorBean recommend_sponsor;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public ControlPanelBean() {
                }

                protected ControlPanelBean(Parcel in) {
                    recommend_sponsor = in.readParcelable(RecommendSponsorBean.class.getClassLoader());
                }

                public static final Creator<ControlPanelBean> CREATOR = new Creator<ControlPanelBean>() {
                    @Override
                    public ControlPanelBean createFromParcel(Parcel in) {
                        return new ControlPanelBean(in);
                    }

                    @Override
                    public ControlPanelBean[] newArray(int size) {
                        return new ControlPanelBean[size];
                    }
                };

                @Bindable
                public RecommendSponsorBean getRecommend_sponsor() {
                    return recommend_sponsor;
                }

                public void setRecommend_sponsor(RecommendSponsorBean recommend_sponsor) {
                    this.recommend_sponsor = recommend_sponsor;
                    notifyChange(BR.recommend_sponsor);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeParcelable(recommend_sponsor, flags);
                }

                public static class RecommendSponsorBean implements Serializable, Observable, Parcelable {
                    /**
                     * icon_url : http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4
                     * label : 帮上头条
                     * night_icon_url : http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b
                     * target_url : https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6639166438802211342&item_id=6639166438802211342
                     */

                    private String icon_url;
                    private String label;
                    private String night_icon_url;
                    private String target_url;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public RecommendSponsorBean() {
                    }

                    protected RecommendSponsorBean(Parcel in) {
                        icon_url = in.readString();
                        label = in.readString();
                        night_icon_url = in.readString();
                        target_url = in.readString();
                    }

                    public static final Creator<RecommendSponsorBean> CREATOR = new Creator<RecommendSponsorBean>() {
                        @Override
                        public RecommendSponsorBean createFromParcel(Parcel in) {
                            return new RecommendSponsorBean(in);
                        }

                        @Override
                        public RecommendSponsorBean[] newArray(int size) {
                            return new RecommendSponsorBean[size];
                        }
                    };

                    @Bindable
                    public String getIcon_url() {
                        return icon_url;
                    }

                    public void setIcon_url(String icon_url) {
                        this.icon_url = icon_url;
                        notifyChange(BR.icon_url);
                    }

                    @Bindable
                    public String getLabel() {
                        return label;
                    }

                    public void setLabel(String label) {
                        this.label = label;
                        notifyChange(BR.label);
                    }

                    @Bindable
                    public String getNight_icon_url() {
                        return night_icon_url;
                    }

                    public void setNight_icon_url(String night_icon_url) {
                        this.night_icon_url = night_icon_url;
                        notifyChange(BR.night_icon_url);
                    }

                    @Bindable
                    public String getTarget_url() {
                        return target_url;
                    }

                    public void setTarget_url(String target_url) {
                        this.target_url = target_url;
                        notifyChange(BR.target_url);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(icon_url);
                        dest.writeString(label);
                        dest.writeString(night_icon_url);
                        dest.writeString(target_url);
                    }
                }
            }

            public static class ForwardInfoBean implements Serializable, Observable, Parcelable {
                /**
                 * forward_count : 4
                 */

                private int forward_count;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public ForwardInfoBean() {
                }

                protected ForwardInfoBean(Parcel in) {
                    forward_count = in.readInt();
                }

                public static final Creator<ForwardInfoBean> CREATOR = new Creator<ForwardInfoBean>() {
                    @Override
                    public ForwardInfoBean createFromParcel(Parcel in) {
                        return new ForwardInfoBean(in);
                    }

                    @Override
                    public ForwardInfoBean[] newArray(int size) {
                        return new ForwardInfoBean[size];
                    }
                };

                @Bindable
                public int getForward_count() {
                    return forward_count;
                }

                public void setForward_count(int forward_count) {
                    this.forward_count = forward_count;
                    notifyChange(BR.forward_count);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(forward_count);
                }
            }

            public static class LogPbBean implements Serializable, Observable, Parcelable {
                /**
                 * impr_id : 2018122617065201001206004671257CD
                 * is_following : 0
                 */

                private String impr_id;
                private String is_following;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public LogPbBean() {
                }

                protected LogPbBean(Parcel in) {
                    impr_id = in.readString();
                    is_following = in.readString();
                }

                public static final Creator<LogPbBean> CREATOR = new Creator<LogPbBean>() {
                    @Override
                    public LogPbBean createFromParcel(Parcel in) {
                        return new LogPbBean(in);
                    }

                    @Override
                    public LogPbBean[] newArray(int size) {
                        return new LogPbBean[size];
                    }
                };

                @Bindable
                public String getImpr_id() {
                    return impr_id;
                }

                public void setImpr_id(String impr_id) {
                    this.impr_id = impr_id;
                    notifyChange(BR.impr_id);
                }

                @Bindable
                public String getIs_following() {
                    return is_following;
                }

                public void setIs_following(String is_following) {
                    this.is_following = is_following;
                    notifyChange(BR.is_following);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(impr_id);
                    dest.writeString(is_following);
                }
            }

            public static class MediaInfoBean implements Serializable, Observable, Parcelable {
                /**
                 * avatar_url : http://p2.pstatp.com/large/4d0004b5e2689a454f
                 * follow : false
                 * is_star_user : false
                 * media_id : 5950542483
                 * name : 成都晚报
                 * recommend_reason :
                 * recommend_type : 0
                 * user_id : 5950542483
                 * user_verified : true
                 * verified_content :
                 */

                private String avatar_url;
                private boolean follow;
                private boolean is_star_user;
                private long media_id;
                private String name;
                private String recommend_reason;
                private int recommend_type;
                private long user_id;
                private boolean user_verified;
                private String verified_content;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public MediaInfoBean() {
                }

                protected MediaInfoBean(Parcel in) {
                    avatar_url = in.readString();
                    follow = in.readByte() != 0;
                    is_star_user = in.readByte() != 0;
                    media_id = in.readLong();
                    name = in.readString();
                    recommend_reason = in.readString();
                    recommend_type = in.readInt();
                    user_id = in.readLong();
                    user_verified = in.readByte() != 0;
                    verified_content = in.readString();
                }

                public static final Creator<MediaInfoBean> CREATOR = new Creator<MediaInfoBean>() {
                    @Override
                    public MediaInfoBean createFromParcel(Parcel in) {
                        return new MediaInfoBean(in);
                    }

                    @Override
                    public MediaInfoBean[] newArray(int size) {
                        return new MediaInfoBean[size];
                    }
                };

                @Bindable
                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                    notifyChange(BR.avatar_url);
                }

                @Bindable
                public boolean isFollow() {
                    return follow;
                }

                public void setFollow(boolean follow) {
                    this.follow = follow;
                    notifyChange(BR.follow);
                }

                @Bindable
                public boolean isIs_star_user() {
                    return is_star_user;
                }

                public void setIs_star_user(boolean is_star_user) {
                    this.is_star_user = is_star_user;
                    notifyChange(BR.is_star_user);
                }

                @Bindable
                public long getMedia_id() {
                    return media_id;
                }

                public void setMedia_id(long media_id) {
                    this.media_id = media_id;
                    notifyChange(BR.media_id);
                }

                @Bindable
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                    notifyChange(BR.name);
                }

                @Bindable
                public String getRecommend_reason() {
                    return recommend_reason;
                }

                public void setRecommend_reason(String recommend_reason) {
                    this.recommend_reason = recommend_reason;
                    notifyChange(BR.recommend_reason);
                }

                @Bindable
                public int getRecommend_type() {
                    return recommend_type;
                }

                public void setRecommend_type(int recommend_type) {
                    this.recommend_type = recommend_type;
                    notifyChange(BR.recommend_type);
                }

                @Bindable
                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                    notifyChange(BR.user_id);
                }

                @Bindable
                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                    notifyChange(BR.user_verified);
                }

                @Bindable
                public String getVerified_content() {
                    return verified_content;
                }

                public void setVerified_content(String verified_content) {
                    this.verified_content = verified_content;
                    notifyChange(BR.verified_content);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(avatar_url);
                    dest.writeByte((byte) (follow ? 1 : 0));
                    dest.writeByte((byte) (is_star_user ? 1 : 0));
                    dest.writeLong(media_id);
                    dest.writeString(name);
                    dest.writeString(recommend_reason);
                    dest.writeInt(recommend_type);
                    dest.writeLong(user_id);
                    dest.writeByte((byte) (user_verified ? 1 : 0));
                    dest.writeString(verified_content);
                }
            }

            public static class MiddleImageBean implements Serializable, Observable, Parcelable {
                /**
                 * height : 352
                 * uri : list/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                 * url : http://p3-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp
                 * url_list : [{"url":"http://p3-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"},{"url":"http://p1-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"},{"url":"http://p1-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp"}]
                 * width : 626
                 */

                private int height;
                private String uri;
                private String url;
                private int width;
                private List<UrlListBean> url_list;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public MiddleImageBean() {
                }

                protected MiddleImageBean(Parcel in) {
                    height = in.readInt();
                    uri = in.readString();
                    url = in.readString();
                    width = in.readInt();
                    url_list = in.createTypedArrayList(UrlListBean.CREATOR);
                }

                public static final Creator<MiddleImageBean> CREATOR = new Creator<MiddleImageBean>() {
                    @Override
                    public MiddleImageBean createFromParcel(Parcel in) {
                        return new MiddleImageBean(in);
                    }

                    @Override
                    public MiddleImageBean[] newArray(int size) {
                        return new MiddleImageBean[size];
                    }
                };

                @Bindable
                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                    notifyChange(BR.height);
                }

                @Bindable
                public String getUri() {
                    return uri;
                }

                public void setUri(String uri) {
                    this.uri = uri;
                    notifyChange(BR.uri);
                }

                @Bindable
                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                    notifyChange(BR.url);
                }

                @Bindable
                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                    notifyChange(BR.width);
                }

                @Bindable
                public List<UrlListBean> getUrl_list() {
                    return url_list;
                }

                public void setUrl_list(List<UrlListBean> url_list) {
                    this.url_list = url_list;
                    notifyChange(BR.url_list);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(height);
                    dest.writeString(uri);
                    dest.writeString(url);
                    dest.writeInt(width);
                    dest.writeTypedList(url_list);
                }

                public static class UrlListBean implements Serializable, Observable, Parcelable {
                    /**
                     * url : http://p3-tt.bytecdn.cn/list/300x196/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58.webp
                     */

                    private String url;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public UrlListBean() {
                    }

                    protected UrlListBean(Parcel in) {
                        url = in.readString();
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(url);
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    public static final Creator<UrlListBean> CREATOR = new Creator<UrlListBean>() {
                        @Override
                        public UrlListBean createFromParcel(Parcel in) {
                            return new UrlListBean(in);
                        }

                        @Override
                        public UrlListBean[] newArray(int size) {
                            return new UrlListBean[size];
                        }
                    };

                    @Bindable
                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                        notifyChange(BR.url);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }
                }
            }

            public static class ShareInfoBean implements Serializable, Observable, Parcelable {
                /**
                 * cover_image : null
                 * description : null
                 * on_suppress : 0
                 * share_type : {"pyq":2,"qq":0,"qzone":0,"wx":0}
                 * share_url : https://m.toutiaoimg.cn/a6639166438802211342/?iid=5034850950&app=news_article&is_hit_share_recommend=0
                 * title : 现场视频曝光 北京交通大学实验室发生爆炸 - 今日头条
                 * token_type : 1
                 * weixin_cover_image : {"height":1455,"uri":"large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38","url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38","url_list":[{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"}],"width":1378}
                 */

                private Object cover_image;
                private Object description;
                private int on_suppress;
                private ShareTypeBean share_type;
                private String share_url;
                private String title;
                private int token_type;
                private WeixinCoverImageBean weixin_cover_image;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public ShareInfoBean() {
                }

                protected ShareInfoBean(Parcel in) {
                    on_suppress = in.readInt();
                    share_type = in.readParcelable(ShareTypeBean.class.getClassLoader());
                    share_url = in.readString();
                    title = in.readString();
                    token_type = in.readInt();
                    weixin_cover_image = in.readParcelable(WeixinCoverImageBean.class.getClassLoader());
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(on_suppress);
                    dest.writeParcelable(share_type, flags);
                    dest.writeString(share_url);
                    dest.writeString(title);
                    dest.writeInt(token_type);
                    dest.writeParcelable(weixin_cover_image, flags);
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                public static final Creator<ShareInfoBean> CREATOR = new Creator<ShareInfoBean>() {
                    @Override
                    public ShareInfoBean createFromParcel(Parcel in) {
                        return new ShareInfoBean(in);
                    }

                    @Override
                    public ShareInfoBean[] newArray(int size) {
                        return new ShareInfoBean[size];
                    }
                };

                @Bindable
                public Object getCover_image() {
                    return cover_image;
                }

                public void setCover_image(Object cover_image) {
                    this.cover_image = cover_image;
                    notifyChange(BR.cover_image);
                }

                @Bindable
                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                    notifyChange(BR.description);
                }

                @Bindable
                public int getOn_suppress() {
                    return on_suppress;
                }

                public void setOn_suppress(int on_suppress) {
                    this.on_suppress = on_suppress;
                    notifyChange(BR.on_suppress);
                }

                @Bindable
                public ShareTypeBean getShare_type() {
                    return share_type;
                }

                public void setShare_type(ShareTypeBean share_type) {
                    this.share_type = share_type;
                    notifyChange(BR.share_type);
                }

                @Bindable
                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                    notifyChange(BR.share_url);
                }

                @Bindable
                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                    notifyChange(BR.title);
                }

                @Bindable
                public int getToken_type() {
                    return token_type;
                }

                public void setToken_type(int token_type) {
                    this.token_type = token_type;
                    notifyChange(BR.token_type);
                }

                @Bindable
                public WeixinCoverImageBean getWeixin_cover_image() {
                    return weixin_cover_image;
                }

                public void setWeixin_cover_image(WeixinCoverImageBean weixin_cover_image) {
                    this.weixin_cover_image = weixin_cover_image;
                    notifyChange(BR.weixin_cover_image);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                public static class ShareTypeBean implements Serializable, Observable, Parcelable {
                    /**
                     * pyq : 2
                     * qq : 0
                     * qzone : 0
                     * wx : 0
                     */

                    private int pyq;
                    private int qq;
                    private int qzone;
                    private int wx;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public ShareTypeBean() {
                    }

                    protected ShareTypeBean(Parcel in) {
                        pyq = in.readInt();
                        qq = in.readInt();
                        qzone = in.readInt();
                        wx = in.readInt();
                    }

                    public static final Creator<ShareTypeBean> CREATOR = new Creator<ShareTypeBean>() {
                        @Override
                        public ShareTypeBean createFromParcel(Parcel in) {
                            return new ShareTypeBean(in);
                        }

                        @Override
                        public ShareTypeBean[] newArray(int size) {
                            return new ShareTypeBean[size];
                        }
                    };

                    @Bindable
                    public int getPyq() {
                        return pyq;
                    }

                    public void setPyq(int pyq) {
                        this.pyq = pyq;
                        notifyChange(BR.pyq);
                    }

                    @Bindable
                    public int getQq() {
                        return qq;
                    }

                    public void setQq(int qq) {
                        this.qq = qq;
                        notifyChange(BR.qq);
                    }

                    @Bindable
                    public int getQzone() {
                        return qzone;
                    }

                    public void setQzone(int qzone) {
                        this.qzone = qzone;
                        notifyChange(BR.qzone);
                    }

                    @Bindable
                    public int getWx() {
                        return wx;
                    }

                    public void setWx(int wx) {
                        this.wx = wx;
                        notifyChange(BR.wx);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(pyq);
                        dest.writeInt(qq);
                        dest.writeInt(qzone);
                        dest.writeInt(wx);
                    }
                }

                public static class WeixinCoverImageBean implements Serializable, Observable, Parcelable {
                    /**
                     * height : 1455
                     * uri : large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38
                     * url : http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38
                     * url_list : [{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38"}]
                     * width : 1378
                     */

                    private int height;
                    private String uri;
                    private String url;
                    private int width;
                    private List<UrlListBeanX> url_list;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public WeixinCoverImageBean() {
                    }

                    protected WeixinCoverImageBean(Parcel in) {
                        height = in.readInt();
                        uri = in.readString();
                        url = in.readString();
                        width = in.readInt();
                        url_list = in.createTypedArrayList(UrlListBeanX.CREATOR);
                    }

                    public static final Creator<WeixinCoverImageBean> CREATOR = new Creator<WeixinCoverImageBean>() {
                        @Override
                        public WeixinCoverImageBean createFromParcel(Parcel in) {
                            return new WeixinCoverImageBean(in);
                        }

                        @Override
                        public WeixinCoverImageBean[] newArray(int size) {
                            return new WeixinCoverImageBean[size];
                        }
                    };

                    @Bindable
                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                        notifyChange(BR.height);
                    }

                    @Bindable
                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                        notifyChange(BR.uri);
                    }

                    @Bindable
                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                        notifyChange(BR.url);
                    }

                    @Bindable
                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                        notifyChange(BR.width);
                    }

                    @Bindable
                    public List<UrlListBeanX> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBeanX> url_list) {
                        this.url_list = url_list;
                        notifyChange(BR.url_list);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(height);
                        dest.writeString(uri);
                        dest.writeString(url);
                        dest.writeInt(width);
                        dest.writeTypedList(url_list);
                    }

                    public static class UrlListBeanX implements Serializable, Observable, Parcelable {
                        /**
                         * url : http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/7228351a-08cd-11e9-b579-7cd30adf6a38
                         */

                        private String url;
                        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                        public UrlListBeanX() {
                        }

                        protected UrlListBeanX(Parcel in) {
                            url = in.readString();
                        }

                        public static final Creator<UrlListBeanX> CREATOR = new Creator<UrlListBeanX>() {
                            @Override
                            public UrlListBeanX createFromParcel(Parcel in) {
                                return new UrlListBeanX(in);
                            }

                            @Override
                            public UrlListBeanX[] newArray(int size) {
                                return new UrlListBeanX[size];
                            }
                        };

                        @Bindable
                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                            notifyChange(BR.url);
                        }

                        private synchronized void notifyChange(int propertyId) {
                            if (propertyChangeRegistry == null) {
                                propertyChangeRegistry = new PropertyChangeRegistry();
                            }
                            propertyChangeRegistry.notifyChange(this, propertyId);
                        }

                        @Override
                        public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                            if (propertyChangeRegistry == null) {
                                propertyChangeRegistry = new PropertyChangeRegistry();
                            }
                            propertyChangeRegistry.add(callback);

                        }

                        @Override
                        public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                            if (propertyChangeRegistry != null) {
                                propertyChangeRegistry.remove(callback);
                            }
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeString(url);
                        }
                    }
                }
            }

            public static class UgcRecommendBean implements Serializable, Observable, Parcelable {
                /**
                 * activity :
                 * reason : 成都晚报官方账号
                 */

                private String activity;
                private String reason;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public UgcRecommendBean() {
                }

                protected UgcRecommendBean(Parcel in) {
                    activity = in.readString();
                    reason = in.readString();
                }

                public static final Creator<UgcRecommendBean> CREATOR = new Creator<UgcRecommendBean>() {
                    @Override
                    public UgcRecommendBean createFromParcel(Parcel in) {
                        return new UgcRecommendBean(in);
                    }

                    @Override
                    public UgcRecommendBean[] newArray(int size) {
                        return new UgcRecommendBean[size];
                    }
                };

                @Bindable
                public String getActivity() {
                    return activity;
                }

                public void setActivity(String activity) {
                    this.activity = activity;
                    notifyChange(BR.activity);
                }

                @Bindable
                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                    notifyChange(BR.reason);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(activity);
                    dest.writeString(reason);
                }
            }

            public static class UserInfoBean implements Serializable, Observable, Parcelable {
                /**
                 * avatar_url : http://p3.pstatp.com/thumb/4d0004b5e2689a454f
                 * description : 1956年创刊，西部最具影响力媒体之一。知成都，观天下。
                 * follow : false
                 * follower_count : 0
                 * name : 成都晚报
                 * schema : sslocal://profile?uid=5950542483&refer=video
                 * user_auth_info : {"auth_type":"0","auth_info":"成都晚报官方账号"}
                 * user_id : 5950542483
                 * user_verified : true
                 * verified_content : 成都晚报官方账号
                 */

                private String avatar_url;
                private String description;
                private boolean follow;
                private int follower_count;
                private String name;
                private String schema;
                private String user_auth_info;
                private long user_id;
                private boolean user_verified;
                private String verified_content;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public UserInfoBean() {
                }

                protected UserInfoBean(Parcel in) {
                    avatar_url = in.readString();
                    description = in.readString();
                    follow = in.readByte() != 0;
                    follower_count = in.readInt();
                    name = in.readString();
                    schema = in.readString();
                    user_auth_info = in.readString();
                    user_id = in.readLong();
                    user_verified = in.readByte() != 0;
                    verified_content = in.readString();
                }

                public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
                    @Override
                    public UserInfoBean createFromParcel(Parcel in) {
                        return new UserInfoBean(in);
                    }

                    @Override
                    public UserInfoBean[] newArray(int size) {
                        return new UserInfoBean[size];
                    }
                };

                @Bindable
                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                    notifyChange(BR.avatar_url);
                }

                @Bindable
                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                    notifyChange(BR.description);
                }

                @Bindable
                public boolean isFollow() {
                    return follow;
                }

                public void setFollow(boolean follow) {
                    this.follow = follow;
                    notifyChange(BR.follow);
                }

                @Bindable
                public int getFollower_count() {
                    return follower_count;
                }

                public void setFollower_count(int follower_count) {
                    this.follower_count = follower_count;
                    notifyChange(BR.follower_count);
                }

                @Bindable
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                    notifyChange(BR.name);
                }

                @Bindable
                public String getSchema() {
                    return schema;
                }

                public void setSchema(String schema) {
                    this.schema = schema;
                    notifyChange(BR.schema);
                }

                @Bindable
                public String getUser_auth_info() {
                    return user_auth_info;
                }

                public void setUser_auth_info(String user_auth_info) {
                    this.user_auth_info = user_auth_info;
                    notifyChange(BR.user_auth_info);
                }

                @Bindable
                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                    notifyChange(BR.user_id);
                }

                @Bindable
                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                    notifyChange(BR.user_verified);
                }

                @Bindable
                public String getVerified_content() {
                    return verified_content;
                }

                public void setVerified_content(String verified_content) {
                    this.verified_content = verified_content;
                    notifyChange(BR.verified_content);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(avatar_url);
                    dest.writeString(description);
                    dest.writeByte((byte) (follow ? 1 : 0));
                    dest.writeInt(follower_count);
                    dest.writeString(name);
                    dest.writeString(schema);
                    dest.writeString(user_auth_info);
                    dest.writeLong(user_id);
                    dest.writeByte((byte) (user_verified ? 1 : 0));
                    dest.writeString(verified_content);
                }
            }

            public static class VideoDetailInfoBean implements Serializable, Observable, Parcelable {
                /**
                 * detail_video_large_image : {"height":326,"uri":"video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58","url_list":[{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p3-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"}],"width":580}
                 * direct_play : 1
                 * group_flags : 32832
                 * show_pgc_subscribe : 1
                 * video_id : v02004870000bghgt1ij2boubbp3l9hg
                 * video_preloading_flag : 1
                 * video_type : 0
                 * video_watch_count : 65600
                 * video_watching_count : 0
                 */

                private DetailVideoLargeImageBean detail_video_large_image;
                private int direct_play;
                private int group_flags;
                private int show_pgc_subscribe;
                private String video_id;
                private int video_preloading_flag;
                private int video_type;
                private int video_watch_count;
                private int video_watching_count;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public VideoDetailInfoBean() {
                }

                protected VideoDetailInfoBean(Parcel in) {
                    detail_video_large_image = in.readParcelable(DetailVideoLargeImageBean.class.getClassLoader());
                    direct_play = in.readInt();
                    group_flags = in.readInt();
                    show_pgc_subscribe = in.readInt();
                    video_id = in.readString();
                    video_preloading_flag = in.readInt();
                    video_type = in.readInt();
                    video_watch_count = in.readInt();
                    video_watching_count = in.readInt();
                }

                public static final Creator<VideoDetailInfoBean> CREATOR = new Creator<VideoDetailInfoBean>() {
                    @Override
                    public VideoDetailInfoBean createFromParcel(Parcel in) {
                        return new VideoDetailInfoBean(in);
                    }

                    @Override
                    public VideoDetailInfoBean[] newArray(int size) {
                        return new VideoDetailInfoBean[size];
                    }
                };

                @Bindable
                public DetailVideoLargeImageBean getDetail_video_large_image() {
                    return detail_video_large_image;
                }

                public void setDetail_video_large_image(DetailVideoLargeImageBean detail_video_large_image) {
                    this.detail_video_large_image = detail_video_large_image;
                    notifyChange(BR.detail_video_large_image);
                }

                @Bindable
                public int getDirect_play() {
                    return direct_play;
                }

                public void setDirect_play(int direct_play) {
                    this.direct_play = direct_play;
                    notifyChange(BR.direct_play);
                }

                @Bindable
                public int getGroup_flags() {
                    return group_flags;
                }

                public void setGroup_flags(int group_flags) {
                    this.group_flags = group_flags;
                    notifyChange(BR.group_flags);
                }

                @Bindable
                public int getShow_pgc_subscribe() {
                    return show_pgc_subscribe;
                }

                public void setShow_pgc_subscribe(int show_pgc_subscribe) {
                    this.show_pgc_subscribe = show_pgc_subscribe;
                    notifyChange(BR.show_pgc_subscribe);
                }

                @Bindable
                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                    notifyChange(BR.video_id);
                }

                @Bindable
                public int getVideo_preloading_flag() {
                    return video_preloading_flag;
                }

                public void setVideo_preloading_flag(int video_preloading_flag) {
                    this.video_preloading_flag = video_preloading_flag;
                    notifyChange(BR.video_preloading_flag);
                }

                @Bindable
                public int getVideo_type() {
                    return video_type;
                }

                public void setVideo_type(int video_type) {
                    this.video_type = video_type;
                    notifyChange(BR.video_type);
                }

                @Bindable
                public int getVideo_watch_count() {
                    return video_watch_count;
                }

                public void setVideo_watch_count(int video_watch_count) {
                    this.video_watch_count = video_watch_count;
                    notifyChange(BR.video_watch_count);
                }

                @Bindable
                public int getVideo_watching_count() {
                    return video_watching_count;
                }

                public void setVideo_watching_count(int video_watching_count) {
                    this.video_watching_count = video_watching_count;
                    notifyChange(BR.video_watching_count);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeParcelable(detail_video_large_image, flags);
                    dest.writeInt(direct_play);
                    dest.writeInt(group_flags);
                    dest.writeInt(show_pgc_subscribe);
                    dest.writeString(video_id);
                    dest.writeInt(video_preloading_flag);
                    dest.writeInt(video_type);
                    dest.writeInt(video_watch_count);
                    dest.writeInt(video_watching_count);
                }

                public static class DetailVideoLargeImageBean implements Serializable, Observable, Parcelable {
                    /**
                     * height : 326
                     * uri : video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                     * url : http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                     * url_list : [{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p3-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"}]
                     * width : 580
                     */

                    private int height;
                    private String uri;
                    private String url;
                    private int width;
                    private List<UrlListBeanXX> url_list;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public DetailVideoLargeImageBean() {
                    }

                    protected DetailVideoLargeImageBean(Parcel in) {
                        height = in.readInt();
                        uri = in.readString();
                        url = in.readString();
                        width = in.readInt();
                        url_list = in.createTypedArrayList(UrlListBeanXX.CREATOR);
                    }

                    public static final Creator<DetailVideoLargeImageBean> CREATOR = new Creator<DetailVideoLargeImageBean>() {
                        @Override
                        public DetailVideoLargeImageBean createFromParcel(Parcel in) {
                            return new DetailVideoLargeImageBean(in);
                        }

                        @Override
                        public DetailVideoLargeImageBean[] newArray(int size) {
                            return new DetailVideoLargeImageBean[size];
                        }
                    };

                    @Bindable
                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                        notifyChange(BR.height);
                    }

                    @Bindable
                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                        notifyChange(BR.uri);
                    }

                    @Bindable
                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                        notifyChange(BR.url);
                    }

                    @Bindable
                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                        notifyChange(BR.width);
                    }

                    @Bindable
                    public List<UrlListBeanXX> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBeanXX> url_list) {
                        this.url_list = url_list;
                        notifyChange(BR.url_list);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(height);
                        dest.writeString(uri);
                        dest.writeString(url);
                        dest.writeInt(width);
                        dest.writeTypedList(url_list);
                    }

                    public static class UrlListBeanXX implements Serializable, Observable, Parcelable {
                        /**
                         * url : http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                         */

                        private String url;
                        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                        public UrlListBeanXX() {
                        }

                        protected UrlListBeanXX(Parcel in) {
                            url = in.readString();
                        }

                        public static final Creator<UrlListBeanXX> CREATOR = new Creator<UrlListBeanXX>() {
                            @Override
                            public UrlListBeanXX createFromParcel(Parcel in) {
                                return new UrlListBeanXX(in);
                            }

                            @Override
                            public UrlListBeanXX[] newArray(int size) {
                                return new UrlListBeanXX[size];
                            }
                        };

                        @Bindable
                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                            notifyChange(BR.url);
                        }

                        private synchronized void notifyChange(int propertyId) {
                            if (propertyChangeRegistry == null) {
                                propertyChangeRegistry = new PropertyChangeRegistry();
                            }
                            propertyChangeRegistry.notifyChange(this, propertyId);
                        }

                        @Override
                        public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                            if (propertyChangeRegistry == null) {
                                propertyChangeRegistry = new PropertyChangeRegistry();
                            }
                            propertyChangeRegistry.add(callback);

                        }

                        @Override
                        public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                            if (propertyChangeRegistry != null) {
                                propertyChangeRegistry.remove(callback);
                            }
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeString(url);
                        }
                    }
                }
            }

            public static class ActionListBean implements Serializable, Observable, Parcelable {

                /**
                 * action : 1
                 * desc :
                 * extra : {}
                 */

                private int action;
                private String desc;
                private ExtraBean extra;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public ActionListBean() {
                }

                protected ActionListBean(Parcel in) {
                    action = in.readInt();
                    desc = in.readString();
                }

                public static final Creator<ActionListBean> CREATOR = new Creator<ActionListBean>() {
                    @Override
                    public ActionListBean createFromParcel(Parcel in) {
                        return new ActionListBean(in);
                    }

                    @Override
                    public ActionListBean[] newArray(int size) {
                        return new ActionListBean[size];
                    }
                };

                @Bindable
                public int getAction() {
                    return action;
                }

                public void setAction(int action) {
                    this.action = action;
                    notifyChange(BR.action);
                }

                @Bindable
                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                    notifyChange(BR.desc);
                }

                @Bindable
                public ExtraBean getExtra() {
                    return extra;
                }

                public void setExtra(ExtraBean extra) {
                    this.extra = extra;
                    notifyChange(BR.extra);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(action);
                    dest.writeString(desc);
                }

                public static class ExtraBean {
                }
            }

            public static class FilterWordsBean implements Serializable, Observable, Parcelable {

                /**
                 * id : 8:0
                 * is_selected : false
                 * name : 看过了
                 */

                private String id;
                private boolean is_selected;
                private String name;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public FilterWordsBean() {
                }

                protected FilterWordsBean(Parcel in) {
                    id = in.readString();
                    is_selected = in.readByte() != 0;
                    name = in.readString();
                }

                public static final Creator<FilterWordsBean> CREATOR = new Creator<FilterWordsBean>() {
                    @Override
                    public FilterWordsBean createFromParcel(Parcel in) {
                        return new FilterWordsBean(in);
                    }

                    @Override
                    public FilterWordsBean[] newArray(int size) {
                        return new FilterWordsBean[size];
                    }
                };

                @Bindable
                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                    notifyChange(BR.id);
                }

                @Bindable
                public boolean isIs_selected() {
                    return is_selected;
                }

                public void setIs_selected(boolean is_selected) {
                    this.is_selected = is_selected;
                    notifyChange(BR.is_selected);
                }

                @Bindable
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                    notifyChange(BR.name);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(id);
                    dest.writeByte((byte) (is_selected ? 1 : 0));
                    dest.writeString(name);
                }
            }

            public static class LargeImageListBean implements Serializable, Observable, Parcelable {

                /**
                 * height : 326
                 * uri : video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                 * url : http://p1-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                 * url_list : [{"url":"http://p1-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p9-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"},{"url":"http://p3-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58"}]
                 * width : 580
                 */

                private int height;
                private String uri;
                private String url;
                private int width;
                private List<UrlListBean> url_list;
                private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                public LargeImageListBean() {
                }

                protected LargeImageListBean(Parcel in) {
                    height = in.readInt();
                    uri = in.readString();
                    url = in.readString();
                    width = in.readInt();
                    url_list = in.createTypedArrayList(UrlListBean.CREATOR);
                }

                public static final Creator<LargeImageListBean> CREATOR = new Creator<LargeImageListBean>() {
                    @Override
                    public LargeImageListBean createFromParcel(Parcel in) {
                        return new LargeImageListBean(in);
                    }

                    @Override
                    public LargeImageListBean[] newArray(int size) {
                        return new LargeImageListBean[size];
                    }
                };

                @Bindable
                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                    notifyChange(BR.height);
                }

                @Bindable
                public String getUri() {
                    return uri;
                }

                public void setUri(String uri) {
                    this.uri = uri;
                    notifyChange(BR.uri);
                }

                @Bindable
                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                    notifyChange(BR.url);
                }

                @Bindable
                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                    notifyChange(BR.width);
                }

                @Bindable
                public List<UrlListBean> getUrl_list() {
                    return url_list;
                }

                public void setUrl_list(List<UrlListBean> url_list) {
                    this.url_list = url_list;
                    notifyChange(BR.url_list);
                }

                private synchronized void notifyChange(int propertyId) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.notifyChange(this, propertyId);
                }

                @Override
                public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry == null) {
                        propertyChangeRegistry = new PropertyChangeRegistry();
                    }
                    propertyChangeRegistry.add(callback);

                }

                @Override
                public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                    if (propertyChangeRegistry != null) {
                        propertyChangeRegistry.remove(callback);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(height);
                    dest.writeString(uri);
                    dest.writeString(url);
                    dest.writeInt(width);
                    dest.writeTypedList(url_list);
                }

                public static class UrlListBean implements Serializable, Observable, Parcelable {
                    /**
                     * url : http://p1-tt.bytecdn.cn/video1609/tos-cn-i-0000/754ae6e5897d4aa4af89503fba0b8c58
                     */

                    private String url;
                    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

                    public UrlListBean() {
                    }

                    protected UrlListBean(Parcel in) {
                        url = in.readString();
                    }

                    public static final Creator<UrlListBean> CREATOR = new Creator<UrlListBean>() {
                        @Override
                        public UrlListBean createFromParcel(Parcel in) {
                            return new UrlListBean(in);
                        }

                        @Override
                        public UrlListBean[] newArray(int size) {
                            return new UrlListBean[size];
                        }
                    };

                    @Bindable
                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                        notifyChange(BR.url);
                    }

                    private synchronized void notifyChange(int propertyId) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.notifyChange(this, propertyId);
                    }

                    @Override
                    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry == null) {
                            propertyChangeRegistry = new PropertyChangeRegistry();
                        }
                        propertyChangeRegistry.add(callback);

                    }

                    @Override
                    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
                        if (propertyChangeRegistry != null) {
                            propertyChangeRegistry.remove(callback);
                        }
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(url);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "content='" + content + '\'' +
                    ", code='" + code + '\'' +
                    ", propertyChangeRegistry=" + propertyChangeRegistry +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PRTouTiaoVideoBean{" +
                "message='" + message + '\'' +
                ", total_number=" + total_number +
                ", has_more=" + has_more +
                ", login_status=" + login_status +
                ", show_et_status=" + show_et_status +
                ", post_content_hint='" + post_content_hint + '\'' +
                ", has_more_to_refresh=" + has_more_to_refresh +
                ", action_to_last_stick=" + action_to_last_stick +
                ", feed_flag=" + feed_flag +
                ", tips=" + tips +
                ", hide_topcell_count=" + hide_topcell_count +
                ", data=" + data +
                ", propertyChangeRegistry=" + propertyChangeRegistry +
                '}';
    }
}
