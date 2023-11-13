package com.tvuniverse.tv.presenter;

import static android.widget.ImageView.ScaleType.FIT_XY;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.tvuniverse.tv.R;
import com.tvuniverse.tv.model.HomeModel;

public class CardPresenter extends Presenter
    {
        private int mSelectedBackgroundColor = 1;
        private int mDefaultBackgroundColor = -1;
        int ITEM_WIDTH = 420;
        int ITEM_HEIGHT = 275;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup)
        {
            mDefaultBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.transparent_1);
            mSelectedBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.transparent_1);

            CustomCardView cardView = new CustomCardView(viewGroup.getContext()) {
                @Override
                public void setSelected(boolean selected) {
                    super.setSelected(selected);
                }
            };

            cardView.setFocusable(true);
            cardView.setCardType(BaseCardView.CARD_TYPE_MAIN_ONLY);
            cardView.setFocusableInTouchMode(true);
            updateCardBackgroundColor(cardView, false);
            return new ViewHolder(cardView);
        }

        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        CustomCardView cardView = (CustomCardView) viewHolder.view;
        HomeModel data = (HomeModel) item;
        cardView.setCardType(BaseCardView.CARD_TYPE_MAIN_ONLY);
        cardView.setMainImageScaleType(FIT_XY);
//        cardView.setMainImageAdjustViewBounds(false);
        cardView.setTitleText(data.getTitle());


//        if (data.getResults().getItems().get(0).equals("master")) {
//            cardView.setMainImageDimensions(900, 430);
//        }
//        else {
//            cardView.setMainImageDimensions(290, 180);
//        }
        ((ViewHolder) viewHolder).updateCardViewImage(data.getThumbnail(),"");


//        if (data.getIcon()==null || data.getIcon().equals("")) {
//
////            Glide.with(cardView.getContext())
////                    .load(data.getIcon())
////                    .into(CustomCardView.goldbadge);
//                    }
//        else {
////            CustomCardView.goldbadge.setVisibility(View.VISIBLE);
//            Glide.with(cardView.getContext())
//                    .load(data.getResults().getUri())
//                    .into(cardView.goldbadge);
//        }

//        if (data.getLogo()==null || data.getLogo().equals("")) {
//
//        }
//        else {
//            CustomCardView.thumb.setVisibility(View.VISIBLE);
//            Glide.with(cardView.getContext())
//                    .load(data.getLogo())
//                    .into(CustomCardView.thumb);
//        }


//        Log.d("TAG", "onBindViewHolder: "+data.getResults().getItems().get(0).getThumbnailUrl());
//        cardView.setMainImageAdjustViewBounds(false);

    }

        @Override
        public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

//        ImageCardView imageCardView = (ImageCardView) viewHolder.view;
//        imageCardView.setBadgeImage(null);
//        imageCardView.setMainImage(null);


    }

        private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        int show = selected ? View.VISIBLE : View.VISIBLE;
        int playicon = selected ? View.GONE : View.VISIBLE;

    }

        private void updateCardBackgroundColor(CustomCardView imageCardView, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        imageCardView.setBackgroundColor(color);
    }




        static class ViewHolder extends Presenter.ViewHolder {
            private CustomCardView mCardView;

            public ViewHolder(View view) {
                super(view);
                mCardView = (CustomCardView) view;
            }

            public CustomCardView getCardView() {
                return mCardView;
            }

            protected void updateCardViewImage(String url,String type) {
//            if (type.equals("LoadMore"))
//            {
//                Glide.with(mCardView.getContext())
//                        .load(url).error(R.drawable.load_more_banner)
//                        .into(mCardView.getMainImageView());
//            }
//            else {
                Glide.with(mCardView.getContext())
                        .load(url)
                        .into(mCardView.getMainImageView());
//            }

            }



        }
    }
