package com.wkz.pleasedreading.splash;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Parcel;
import android.os.Parcelable;

import com.wkz.pleasedreading.BR;

import java.io.Serializable;
import java.util.List;

public class PRSplashBean implements Serializable, Observable, Parcelable {

    /**
     * images : [{"startdate":"20181226","fullstartdate":"201812261600","enddate":"20181227","url":"/az/hprichbg/rb/BethesdaSnow_ZH-CN3087618718_1920x1080.jpg","urlbase":"/az/hprichbg/rb/BethesdaSnow_ZH-CN3087618718","copyright":"毕士达喷泉，纽约 (© Mitchell Funk/Getty Images)","copyrightlink":"http://www.bing.com/search?q=%E6%AF%95%E5%A3%AB%E8%BE%BE%E5%96%B7%E6%B3%89&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20181226_BethesdaSnow%22&FORM=HPQUIZ","wp":false,"hsh":"77b7992f8d4d4d35d5d3236d26ca4e2e","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private TooltipsBean tooltips;
    private List<ImagesBean> images;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    public PRSplashBean() {
    }

    protected PRSplashBean(Parcel in) {
        tooltips = in.readParcelable(TooltipsBean.class.getClassLoader());
        images = in.createTypedArrayList(ImagesBean.CREATOR);
    }

    public static final Creator<PRSplashBean> CREATOR = new Creator<PRSplashBean>() {
        @Override
        public PRSplashBean createFromParcel(Parcel in) {
            return new PRSplashBean(in);
        }

        @Override
        public PRSplashBean[] newArray(int size) {
            return new PRSplashBean[size];
        }
    };

    @Bindable
    public TooltipsBean getTooltips() {
        return tooltips;
    }

    public void setTooltips(TooltipsBean tooltips) {
        this.tooltips = tooltips;
        notifyChange(BR.tooltips);
    }

    @Bindable
    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
        notifyChange(BR.images);
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
        dest.writeParcelable(tooltips, flags);
        dest.writeTypedList(images);
    }

    public static class TooltipsBean implements Serializable, Observable, Parcelable {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        private String loading;
        private String previous;
        private String next;
        private String walle;
        private String walls;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

        public TooltipsBean() {
        }

        protected TooltipsBean(Parcel in) {
            loading = in.readString();
            previous = in.readString();
            next = in.readString();
            walle = in.readString();
            walls = in.readString();
        }

        public static final Creator<TooltipsBean> CREATOR = new Creator<TooltipsBean>() {
            @Override
            public TooltipsBean createFromParcel(Parcel in) {
                return new TooltipsBean(in);
            }

            @Override
            public TooltipsBean[] newArray(int size) {
                return new TooltipsBean[size];
            }
        };

        @Bindable
        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
            notifyChange(BR.loading);
        }

        @Bindable
        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
            notifyChange(BR.previous);
        }

        @Bindable
        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
            notifyChange(BR.next);
        }

        @Bindable
        public String getWalle() {
            return walle;
        }

        public void setWalle(String walle) {
            this.walle = walle;
            notifyChange(BR.walle);
        }

        @Bindable
        public String getWalls() {
            return walls;
        }

        public void setWalls(String walls) {
            this.walls = walls;
            notifyChange(BR.walls);
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
            dest.writeString(loading);
            dest.writeString(previous);
            dest.writeString(next);
            dest.writeString(walle);
            dest.writeString(walls);
        }
    }

    public static class ImagesBean implements Serializable, Observable, Parcelable {
        /**
         * startdate : 20181226
         * fullstartdate : 201812261600
         * enddate : 20181227
         * url : /az/hprichbg/rb/BethesdaSnow_ZH-CN3087618718_1920x1080.jpg
         * urlbase : /az/hprichbg/rb/BethesdaSnow_ZH-CN3087618718
         * copyright : 毕士达喷泉，纽约 (© Mitchell Funk/Getty Images)
         * copyrightlink : http://www.bing.com/search?q=%E6%AF%95%E5%A3%AB%E8%BE%BE%E5%96%B7%E6%B3%89&form=hpcapt&mkt=zh-cn
         * title :
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20181226_BethesdaSnow%22&FORM=HPQUIZ
         * wp : false
         * hsh : 77b7992f8d4d4d35d5d3236d26ca4e2e
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String title;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;
        private List<?> hs;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            startdate = in.readString();
            fullstartdate = in.readString();
            enddate = in.readString();
            url = in.readString();
            urlbase = in.readString();
            copyright = in.readString();
            copyrightlink = in.readString();
            title = in.readString();
            quiz = in.readString();
            wp = in.readByte() != 0;
            hsh = in.readString();
            drk = in.readInt();
            top = in.readInt();
            bot = in.readInt();
        }

        public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel in) {
                return new ImagesBean(in);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };

        @Bindable
        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
            notifyChange(BR.startdate);
        }

        @Bindable
        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
            notifyChange(BR.fullstartdate);
        }

        @Bindable
        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
            notifyChange(BR.enddate);
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
        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
            notifyChange(BR.urlbase);
        }

        @Bindable
        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
            notifyChange(BR.copyright);
        }

        @Bindable
        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
            notifyChange(BR.copyrightlink);
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
        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
            notifyChange(BR.quiz);
        }

        @Bindable
        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
            notifyChange(BR.wp);
        }

        @Bindable
        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
            notifyChange(BR.hsh);
        }

        @Bindable
        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
            notifyChange(BR.drk);
        }

        @Bindable
        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
            notifyChange(BR.top);
        }

        @Bindable
        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
            notifyChange(BR.bot);
        }

        @Bindable
        public List<?> getHs() {
            return hs;
        }

        public void setHs(List<?> hs) {
            this.hs = hs;
            notifyChange(BR.hs);
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
            dest.writeString(startdate);
            dest.writeString(fullstartdate);
            dest.writeString(enddate);
            dest.writeString(url);
            dest.writeString(urlbase);
            dest.writeString(copyright);
            dest.writeString(copyrightlink);
            dest.writeString(title);
            dest.writeString(quiz);
            dest.writeByte((byte) (wp ? 1 : 0));
            dest.writeString(hsh);
            dest.writeInt(drk);
            dest.writeInt(top);
            dest.writeInt(bot);
        }
    }
}
