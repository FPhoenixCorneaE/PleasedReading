package com.wkz.videoplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wkz.videoplayer.R;
import com.wkz.videoplayer.inter.listener.OnClarityChangedListener;
import com.wkz.videoplayer.utils.FRVideoPlayerUtils;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/1/29
 *     desc  : 切换清晰度对话框（仿腾讯视频切换清晰度的对话框）
 *     revise:
 * </pre>
 */
public class FRChangeClarityDialog extends Dialog {

    private LinearLayout mLinearLayout;
    private int mCurrentCheckedIndex;

    public FRChangeClarityDialog(Context context) {
        super(context, R.style.FRChangeClarityDialogTheme);
        init(context);
    }


    @Override
    public void onBackPressed() {
        // 按返回键时回调清晰度没有变化
        if (mListener != null) {
            mListener.onClarityNotChanged();
        }
        super.onBackPressed();
    }


    private void init(Context context) {
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClarityNotChanged();
                }
                FRChangeClarityDialog.this.dismiss();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        setContentView(mLinearLayout, params);
        //注意，这里一定要判空
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    /**
     * 设置清晰度等级
     *
     * @param items          清晰度等级items
     * @param defaultChecked 默认选中的清晰度索引
     */
    public void setClarityGrade(List<String> items, int defaultChecked) {
        mCurrentCheckedIndex = defaultChecked;
        for (int i = 0; i < items.size(); i++) {
            TextView itemView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.fr_change_video_clarity, mLinearLayout, false);
            itemView.setTag(i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int checkIndex = (int) v.getTag();
                        if (checkIndex != mCurrentCheckedIndex) {
                            for (int j = 0; j < mLinearLayout.getChildCount(); j++) {
                                mLinearLayout.getChildAt(j).setSelected(checkIndex == j);
                            }
                            mListener.onClarityChanged(checkIndex);
                            mCurrentCheckedIndex = checkIndex;
                        } else {
                            mListener.onClarityNotChanged();
                        }
                    }
                    FRChangeClarityDialog.this.dismiss();
                }
            });
            itemView.setText(items.get(i));
            itemView.setSelected(i == defaultChecked);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    itemView.getLayoutParams();
            params.topMargin = (i == 0) ? 0 : FRVideoPlayerUtils.dp2px(getContext(), 16f);
            mLinearLayout.addView(itemView, params);
        }
    }


    private OnClarityChangedListener mListener;

    public void setOnClarityCheckedListener(OnClarityChangedListener listener) {
        mListener = listener;
    }

}
