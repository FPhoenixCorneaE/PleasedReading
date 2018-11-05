package com.wkz.framework.widgets.statuslayout;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wkz.framework.R;
import com.wkz.framework.utils.ResourceUtils;

/**
 * 状态布局管理器
 */
public class FRStatusLayoutManager {

    /**
     * 三种默认布局 ID
     */
    private static final int DEFAULT_LOADING_LAYOUT_ID = R.layout.fr_layout_status_loading;
    private static final int DEFAULT_EMPTY_LAYOUT_ID = R.layout.fr_layout_status_empty;
    private static final int DEFAULT_ERROR_LAYOUT_ID = R.layout.fr_layout_status_error;

    /**
     * 默认布局中可点击的 view ID
     */
    private static final int DEFAULT_EMPTY_BUTTON_ID = R.id.fr_btn_status_retry;
    private static final int DEFAULT_ERROR_BUTTON_ID = R.id.fr_btn_status_retry;

    /**
     * 提示内容
     */
    private static final int DEFAULT_LOADING_CONTENT_ID = R.id.fr_tv_status_loading_content;
    private static final int DEFAULT_EMPTY_CONTENT_ID = R.id.fr_tv_status_empty_content;
    private static final int DEFAULT_ERROR_CONTENT_ID = R.id.fr_tv_status_error_content;

    /**
     * 图片资源
     */
    private static final int DEFAULT_EMPTY_IMG_ID = R.id.fr_iv_status_empty_img;
    private static final int DEFAULT_ERROR_IMG_ID = R.id.fr_iv_status_error_img;

    /**
     * 默认颜色
     */
    private static final int DEFAULT_BUTTON_TEXT_COLOR = R.color.fr_status_btn;
    private static final int DEFAULT_BACKGROUND_COLOR = R.color.fr_status_bg;

    private View contentLayout;

    @LayoutRes
    private int loadingLayoutID;
    private View loadingLayout;
    private String loadingText;

    @IdRes
    private int emptyClickViewId;
    @LayoutRes
    private int emptyLayoutID;
    private View emptyLayout;
    private String emptyText;
    private String emptyClickViewText;
    private int emptyClickViewTextColor;
    private boolean isEmptyClickViewVisible;
    @DrawableRes
    private int emptyImgID;

    @IdRes
    private int errorClickViewId;
    @LayoutRes
    private int errorLayoutID;
    private View errorLayout;
    private String errorText;
    private String errorClickViewText;
    private int errorClickViewTextColor;
    private boolean isErrorClickViewVisible;
    @DrawableRes
    private int errorImgID;

    private int defaultBackgroundColor;

    private OnStatusLayoutClickListener onStatusLayoutClickListener;

    private FRStatusLayoutHelper frStatusLayoutHelper;

    private LayoutInflater inflater;

    private FRStatusLayoutManager(Builder builder) {
        this.contentLayout = builder.contentLayout;

        this.loadingLayoutID = builder.loadingLayoutID;
        this.loadingLayout = builder.loadingLayout;
        this.loadingText = builder.loadingText;

        this.emptyClickViewId = builder.emptyClickViewId;
        this.emptyLayoutID = builder.emptyLayoutID;
        this.emptyLayout = builder.emptyLayout;
        this.emptyText = builder.emptyText;
        this.emptyClickViewText = builder.emptyClickViewText;
        this.emptyClickViewTextColor = builder.emptyClickViewTextColor;
        this.isEmptyClickViewVisible = builder.isEmptyClickViewVisible;
        this.emptyImgID = builder.emptyImgID;

        this.errorClickViewId = builder.errorClickViewId;
        this.errorLayoutID = builder.errorLayoutID;
        this.errorLayout = builder.errorLayout;
        this.errorText = builder.errorText;
        this.errorClickViewText = builder.errorClickViewText;
        this.errorClickViewTextColor = builder.errorClickViewTextColor;
        this.isErrorClickViewVisible = builder.isErrorClickViewVisible;
        this.errorImgID = builder.errorImgID;

        this.defaultBackgroundColor = builder.defaultBackgroundColor;

        this.onStatusLayoutClickListener = builder.onStatusLayoutClickListener;

        this.frStatusLayoutHelper = new FRStatusLayoutHelper(contentLayout);
    }

    private View inflate(@LayoutRes int resource) {
        if (inflater == null) {
            inflater = LayoutInflater.from(contentLayout.getContext());
        }
        return inflater.inflate(resource, null);
    }

    ///////////////////////////////////////////
    /////////////////原有布局////////////////////
    ///////////////////////////////////////////

    /**
     * 显示原有布局
     */
    public void showSuccessLayout() {
        frStatusLayoutHelper.restoreLayout();
    }

    ///////////////////////////////////////////
    ////////////////加载中布局///////////////////
    ///////////////////////////////////////////

    /**
     * 创建加载中布局
     */
    private void createLoadingLayout() {
        if (loadingLayout == null) {
            loadingLayout = inflate(loadingLayoutID);
        }
        if (loadingLayoutID == DEFAULT_LOADING_LAYOUT_ID) {
            loadingLayout.setBackgroundColor(defaultBackgroundColor);
        }
        if (!TextUtils.isEmpty(loadingText)) {
            TextView loadingTextView = loadingLayout.findViewById(DEFAULT_LOADING_CONTENT_ID);
            if (loadingTextView != null) {
                loadingTextView.setText(loadingText);
            }
        }
    }

    /**
     * 获取加载中布局
     *
     * @return 加载中布局
     */
    public View getLoadingLayout() {
        createLoadingLayout();
        return loadingLayout;
    }

    /**
     * 显示加载中布局
     */
    public void showLoadingLayout() {
        createLoadingLayout();
        frStatusLayoutHelper.showStatusLayout(loadingLayout);
    }

    ///////////////////////////////////////////
    ////////////////空数据布局///////////////////
    ///////////////////////////////////////////

    /**
     * 创建空数据布局
     */
    private void createEmptyLayout() {
        if (emptyLayout == null) {
            emptyLayout = inflate(emptyLayoutID);
        }
        if (emptyLayoutID == DEFAULT_EMPTY_LAYOUT_ID) {
            emptyLayout.setBackgroundColor(defaultBackgroundColor);
        }

        // 点击事件回调
        View view = emptyLayout.findViewById(emptyClickViewId);
        if (view != null && onStatusLayoutClickListener != null) {
            // 设置点击按钮点击时事件回调
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStatusLayoutClickListener.onEmptyChildClick(view);
                }
            });
        }

        // 设置默认空数据布局的提示文本
        TextView emptyTextView = emptyLayout.findViewById(DEFAULT_EMPTY_CONTENT_ID);
        if (emptyTextView != null) {
            if (!TextUtils.isEmpty(emptyText)) {
                emptyTextView.setText(emptyText);
            }
        }

        // 设置默认空数据布局的图片
        ImageView emptyImageView = emptyLayout.findViewById(DEFAULT_EMPTY_IMG_ID);
        if (emptyImageView != null) {
            if (emptyImgID > 0) {
                emptyImageView.setVisibility(View.VISIBLE);
                emptyImageView.setImageResource(emptyImgID);
            } else {
                emptyImageView.setVisibility(View.GONE);
            }
        }

        TextView emptyClickViewTextView = emptyLayout.findViewById(DEFAULT_EMPTY_BUTTON_ID);

        if (emptyClickViewTextView != null) {
            // 设置点击按钮的文本和可见性
            if (isEmptyClickViewVisible) {
                emptyClickViewTextView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(emptyClickViewText)) {
                    emptyClickViewTextView.setText(emptyClickViewText);
                }
                emptyClickViewTextView.setTextColor(emptyClickViewTextColor);
            } else {
                emptyClickViewTextView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取空数据布局
     *
     * @return 空数据布局
     */
    public View getEmptyLayout() {
        createEmptyLayout();
        return emptyLayout;
    }

    /**
     * 显示空数据布局
     */
    public void showEmptyLayout() {
        createEmptyLayout();
        frStatusLayoutHelper.showStatusLayout(emptyLayout);
    }

    ///////////////////////////////////////////
    /////////////////出错布局////////////////////
    ///////////////////////////////////////////

    /**
     * 创建出错布局
     */
    private void createErrorLayout() {
        if (errorLayout == null) {
            errorLayout = inflate(errorLayoutID);
        }
        if (errorLayoutID == DEFAULT_ERROR_LAYOUT_ID) {
            errorLayout.setBackgroundColor(defaultBackgroundColor);
        }

        View view = errorLayout.findViewById(errorClickViewId);
        if (view != null && onStatusLayoutClickListener != null) {
            // 设置点击按钮点击时事件回调
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStatusLayoutClickListener.onErrorChildClick(view);
                }
            });
        }

        // 设置默认出错布局的提示文本
        TextView errorTextView = errorLayout.findViewById(DEFAULT_ERROR_CONTENT_ID);
        if (errorTextView != null) {
            if (!TextUtils.isEmpty(errorText)) {
                errorTextView.setText(errorText);
            }
        }

        // 设置默认出错布局的图片
        ImageView errorImageView = errorLayout.findViewById(DEFAULT_ERROR_IMG_ID);
        if (errorImageView != null) {
            if (errorImgID > 0) {
                errorImageView.setVisibility(View.VISIBLE);
                errorImageView.setImageResource(errorImgID);
            } else {
                errorImageView.setVisibility(View.GONE);
            }
        }

        TextView errorClickViewTextView = errorLayout.findViewById(DEFAULT_ERROR_BUTTON_ID);
        if (errorClickViewTextView != null) {
            // 设置点击按钮的文本和可见性
            if (isErrorClickViewVisible) {
                errorClickViewTextView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(errorClickViewText)) {
                    errorClickViewTextView.setText(errorClickViewText);
                }
                errorClickViewTextView.setTextColor(errorClickViewTextColor);
            } else {
                errorClickViewTextView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取出错布局
     *
     * @return 出错布局
     */
    public View getErrorLayout() {
        createErrorLayout();
        return errorLayout;
    }

    /**
     * 显示出错布局
     */
    public void showErrorLayout() {
        createErrorLayout();
        frStatusLayoutHelper.showStatusLayout(errorLayout);
    }

    ///////////////////////////////////////////
    ////////////////自定义布局///////////////////
    ///////////////////////////////////////////

    /**
     * 显示自定义状态布局
     *
     * @param customLayout 自定义布局
     */
    public void showCustomLayout(@NonNull View customLayout) {
        frStatusLayoutHelper.showStatusLayout(customLayout);
    }

    /**
     * 显示自定义状态布局
     *
     * @param customLayoutID 自定义状态布局 ID
     * @return 通过 customLayoutID 生成的 View
     */
    public View showCustomLayout(@LayoutRes int customLayoutID) {
        View customerView = inflate(customLayoutID);
        showCustomLayout(customerView);
        return customerView;
    }

    /**
     * 显示自定义状态布局
     *
     * @param customLayout 自定义布局
     * @param clickViewID  可点击 View ID
     */
    public void showCustomLayout(@NonNull View customLayout, @IdRes int... clickViewID) {
        frStatusLayoutHelper.showStatusLayout(customLayout);
        if (onStatusLayoutClickListener == null) {
            return;
        }

        for (int aClickViewID : clickViewID) {
            View clickView = customLayout.findViewById(aClickViewID);
            if (clickView == null) {
                return;
            }

            // 设置点击按钮点击时事件回调
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStatusLayoutClickListener.onCustomerChildClick(view);
                }
            });
        }
    }

    /**
     * 显示自定义状态布局
     *
     * @param customLayoutID 自定义布局 ID
     * @param clickViewID    点击按钮 ID
     */
    public View showCustomLayout(@LayoutRes int customLayoutID, @IdRes int... clickViewID) {
        View customLayout = inflate(customLayoutID);
        showCustomLayout(customLayout, clickViewID);
        return customLayout;
    }

    public static final class Builder {

        private View contentLayout;

        @LayoutRes
        private int loadingLayoutID;
        private View loadingLayout;
        private String loadingText;

        @IdRes
        private int emptyClickViewId;
        @LayoutRes
        private int emptyLayoutID;
        private View emptyLayout;
        private String emptyText;
        private String emptyClickViewText;
        private int emptyClickViewTextColor;
        private boolean isEmptyClickViewVisible;
        @DrawableRes
        private int emptyImgID;

        @IdRes
        private int errorClickViewId;
        @LayoutRes
        private int errorLayoutID;
        private View errorLayout;
        private String errorText;
        private String errorClickViewText;
        private int errorClickViewTextColor;
        private boolean isErrorClickViewVisible;
        @DrawableRes
        private int errorImgID;

        private int defaultBackgroundColor;

        private OnStatusLayoutClickListener onStatusLayoutClickListener;

        /**
         * 创建状态布局 Build 对象
         *
         * @param contentLayout 原有布局，内容布局
         */
        public Builder(@NonNull View contentLayout) {
            this.contentLayout = contentLayout;
            // 设置默认布局
            this.loadingLayoutID = DEFAULT_LOADING_LAYOUT_ID;
            this.emptyLayoutID = DEFAULT_EMPTY_LAYOUT_ID;
            this.errorLayoutID = DEFAULT_ERROR_LAYOUT_ID;
            // 设置默认点击点击view id
            this.emptyClickViewId = DEFAULT_EMPTY_BUTTON_ID;
            this.errorClickViewId = DEFAULT_ERROR_BUTTON_ID;
            // 设置默认点击按钮属性
            this.isEmptyClickViewVisible = true;
            this.emptyClickViewTextColor = ResourceUtils.getColor(DEFAULT_BUTTON_TEXT_COLOR);
            this.isErrorClickViewVisible = true;
            this.errorClickViewTextColor = ResourceUtils.getColor(DEFAULT_BUTTON_TEXT_COLOR);
            // 设置默认背景色
            this.defaultBackgroundColor = ResourceUtils.getColor(DEFAULT_BACKGROUND_COLOR);
        }

        ///////////////////////////////////////////
        ////////////////加载中布局///////////////////
        ///////////////////////////////////////////

        /**
         * 设置加载中布局
         *
         * @param loadingLayoutID 加载中布局 ID
         * @return 状态布局 Build 对象
         */
        public Builder setLoadingLayout(@LayoutRes int loadingLayoutID) {
            this.loadingLayoutID = loadingLayoutID;
            return this;
        }

        /**
         * 设置加载中布局
         *
         * @param loadingLayout 加载中布局
         * @return 状态布局 Build 对象
         */
        public Builder setLoadingLayout(@NonNull View loadingLayout) {
            this.loadingLayout = loadingLayout;
            return this;
        }

        /**
         * 设置默认加载中布局提示文本
         *
         * @param loadingText 加载中布局提示文本
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultLoadingText(String loadingText) {
            this.loadingText = loadingText;
            return this;
        }

        /**
         * 设置默认加载中布局提示文本
         *
         * @param loadingTextStrID 加载中布局提示文本 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultLoadingText(@StringRes int loadingTextStrID) {
            this.loadingText = ResourceUtils.getString(loadingTextStrID);
            return this;
        }


        ///////////////////////////////////////////
        ////////////////空数据布局///////////////////
        ///////////////////////////////////////////

        /**
         * 设置空数据布局
         *
         * @param emptyLayoutResId 空数据布局 ID
         * @return 状态布局 Build 对象
         */
        public Builder setEmptyLayout(@LayoutRes int emptyLayoutResId) {
            this.emptyLayoutID = emptyLayoutResId;
            return this;
        }

        /**
         * 设置空数据布局
         *
         * @param emptyLayout 空数据布局
         * @return 状态布局 Build 对象
         */
        public Builder setEmptyLayout(@NonNull View emptyLayout) {
            this.emptyLayout = emptyLayout;
            return this;
        }

        /**
         * 设置空数据布局点击按钮 ID
         *
         * @param emptyClickViewResId 空数据布局点击按钮 ID
         * @return 状态布局 Build 对象
         */
        public Builder setEmptyClickViewID(@IdRes int emptyClickViewResId) {
            this.emptyClickViewId = emptyClickViewResId;
            return this;
        }

        /**
         * 设置默认空数据布局点击按钮文本
         *
         * @param emptyClickViewText 点击按钮文本
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyClickViewText(String emptyClickViewText) {
            this.emptyClickViewText = emptyClickViewText;
            return this;
        }

        /**
         * 设置默认空数据布局点击按钮文本
         *
         * @param emptyClickViewTextID 点击按钮文本 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyClickViewText(@StringRes int emptyClickViewTextID) {
            this.emptyClickViewText = ResourceUtils.getString(emptyClickViewTextID);
            return this;
        }

        /**
         * 设置默认空数据布局点击按钮文本颜色
         *
         * @param emptyClickViewTextColor 点击按钮文本颜色
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyClickViewTextColor(int emptyClickViewTextColor) {
            this.emptyClickViewTextColor = emptyClickViewTextColor;
            return this;
        }

        /**
         * 设置默认空数据布局点击按钮是否可见
         *
         * @param isEmptyClickViewVisible true：可见 false：不可见
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyClickViewVisible(boolean isEmptyClickViewVisible) {
            this.isEmptyClickViewVisible = isEmptyClickViewVisible;
            return this;
        }

        /**
         * 设置空数据布局图片
         *
         * @param emptyImgID 空数据布局图片 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyImg(@DrawableRes int emptyImgID) {
            this.emptyImgID = emptyImgID;
            return this;
        }

        ///////////////////////////////////////////
        /////////////////出错布局////////////////////
        ///////////////////////////////////////////

        /**
         * 设置空数据布局提示文本
         *
         * @param emptyText 空数据布局提示文本
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyText(String emptyText) {
            this.emptyText = emptyText;
            return this;
        }

        /**
         * 设置空数据布局提示文本
         *
         * @param emptyTextStrID 空数据布局提示文本 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultEmptyText(@StringRes int emptyTextStrID) {
            this.emptyText = ResourceUtils.getString(emptyTextStrID);
            return this;
        }


        /**
         * 设置出错布局
         *
         * @param errorLayoutResId 出错布局 ID
         * @return 状态布局 Build 对象
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutResId) {
            this.errorLayoutID = errorLayoutResId;
            return this;
        }

        /**
         * 设置出错布局
         *
         * @param errorLayout 出错布局
         * @return 状态布局 Build 对象
         */
        public Builder setErrorLayout(@NonNull View errorLayout) {
            this.errorLayout = errorLayout;
            return this;
        }

        /**
         * 设置出错布局点击按钮 ID
         *
         * @param errorClickViewResId 出错布局点击按钮 ID
         * @return 状态布局 Build 对象
         */
        public Builder setErrorClickViewID(@IdRes int errorClickViewResId) {
            this.errorClickViewId = errorClickViewResId;
            return this;
        }

        /**
         * 设置出错布局提示文本
         *
         * @param errorText 出错布局提示文本
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorText(String errorText) {
            this.errorText = errorText;
            return this;
        }

        /**
         * 设置出错布局提示文本
         *
         * @param errorTextStrID 出错布局提示文本 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorText(@StringRes int errorTextStrID) {
            this.errorText = ResourceUtils.getString(errorTextStrID);
            return this;
        }

        /**
         * 设置默认出错布局点击按钮文本
         *
         * @param errorClickViewText 点击按钮文本
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorClickViewText(String errorClickViewText) {
            this.errorClickViewText = errorClickViewText;
            return this;
        }

        /**
         * 设置默认出错布局点击按钮文本
         *
         * @param errorClickViewTextID 点击按钮文本 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorClickViewText(@StringRes int errorClickViewTextID) {
            this.errorClickViewText = ResourceUtils.getString(errorClickViewTextID);
            return this;
        }

        /**
         * 设置默认出错布局点击按钮文本颜色
         *
         * @param errorClickViewTextColor 点击按钮文本颜色
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorClickViewTextColor(int errorClickViewTextColor) {
            this.errorClickViewTextColor = errorClickViewTextColor;
            return this;
        }

        /**
         * 设置出错布局点击按钮可见行
         *
         * @param isErrorClickViewVisible true：可见 false：不可见
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorClickViewVisible(boolean isErrorClickViewVisible) {
            this.isErrorClickViewVisible = isErrorClickViewVisible;
            return this;
        }

        /**
         * 设置出错布局图片
         *
         * @param errorImgID 出错布局图片 ID
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultErrorImg(@DrawableRes int errorImgID) {
            this.errorImgID = errorImgID;
            return this;
        }

        /**
         * 设置默认布局的背景颜色，包括加载中、空数据和出错布局
         *
         * @param defaultBackgroundColor 默认布局的背景颜色
         * @return 状态布局 Build 对象
         */
        public Builder setDefaultLayoutsBackgroundColor(int defaultBackgroundColor) {
            this.defaultBackgroundColor = defaultBackgroundColor;
            return this;
        }

        /**
         * 设置点击事件监听器
         *
         * @param listener 点击事件监听器
         * @return 状态布局 Build 对象
         */
        public Builder setOnStatusLayoutClickListener(OnStatusLayoutClickListener listener) {
            this.onStatusLayoutClickListener = listener;
            return this;
        }

        /**
         * 创建状态布局管理器
         *
         * @return 状态布局管理器
         */
        @NonNull
        public FRStatusLayoutManager build() {
            return new FRStatusLayoutManager(this);
        }

    }
}
