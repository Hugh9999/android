package com.tvuniverse.tv.presenter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.BaseCardView;

import com.tvuniverse.tv.R;

public class CustomCardView extends BaseCardView {


    RelativeLayout layoutfade,layoutload;
    TextView desc,title,duration;
    private ImageView mImageView;
    public ImageView goldbadge,thumb;


    public CustomCardView(Context context)
    {
        this(context, null);
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.leanback.R.attr.imageCardViewStyle);
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, androidx.leanback.R.styleable.lbBaseCardView, defStyleAttr, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.portrait_card_layout, this);

        mImageView=(ImageView)findViewById(R.id.main_image);
        thumb=(ImageView)findViewById(R.id.imageThumb);
//        mImageView=(ImageView)findViewById(R.id.main_image);
//        goldbadge=findViewById(R.id.goldbadge);
//        desc=findViewById(R.id.tvDescription);
        title=findViewById(R.id.tvTitle);
//        durat

    }





    public final ImageView getMainImageView() {
        return mImageView;
    }
    public final ImageView getGoldbadge() {
        return goldbadge;
    }

    public void loadVisibility(boolean visibility){
        if(visibility == true){
            layoutload.setVisibility(VISIBLE);
        }else{
            layoutload.setVisibility(GONE);
        }
    }

    public void loadDimention(int width, int height){
        ViewGroup.LayoutParams lp = layoutload.getLayoutParams();
        lp.width = width;
        lp.height = height;
        layoutload.setLayoutParams(lp);
    }


    public void fadeVisibility(boolean visibility){
        if(visibility == true){
            layoutfade.setVisibility(VISIBLE);
        }else{
            layoutfade.setVisibility(GONE);
        }
    }


    /**
     * Set drawable with optional fade-in animation.
     */

    public void setGoldbadge(Drawable drawable)
    {
        if (goldbadge==null)
        {
            return;
        }
    }
    public void setMainImage(Drawable drawable, boolean fade) {
        if (mImageView == null) {
            return;
        }
        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mImageView.animate().cancel();
            mImageView.setAlpha(1f);
            mImageView.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            if (fade) {
                fadeIn(mImageView);
            } else {
                mImageView.animate().cancel();
                mImageView.setAlpha(1f);
            }
        }
    }


    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }

    public void setlayoutDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = layoutfade.getLayoutParams();
        lp.width = width;
        lp.height = height;
        layoutfade.setLayoutParams(lp);
    }


    public Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }
        return mImageView.getDrawable();
    }


    private void fadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(v.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime)).start();
    }

    public void setTitleText(CharSequence text) {
        if (title == null) {
            return;
        }else
            title.setText(text);
    }

    public CharSequence getTitleText() {
        if (title == null) {
            return null;
        }return title.getText();
    }

    public void setDescText(CharSequence text) {
        if (desc == null) {
            return;
        }else
            desc.setText(text);
    }

    public CharSequence getDescText() {
        if (desc == null) {
            return null;
        }return desc.getText();
    }


    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }

    public void setMainImageScaleType(ImageView.ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }


    public boolean isloadVisible() {
        if(layoutload.isShown() == true)
            return true;
        else
            return false;
    }
}