package com.intprices.util;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intprices.R;
import com.intprices.api.model.Product;
import com.squareup.picasso.Picasso;

import static com.intprices.util.TimeLeft.timeLeftFormatted;

public class DataBindingUtil {

    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.no_product)
                .error(R.drawable.no_product)
                .into(view);
    }

    @BindingAdapter("bind:strike")
    public static void oldPriceTextView(TextView view, boolean strike) {
        if (strike) view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @BindingAdapter("bind:onClick")
    public static void oldPriceTextView(final View view, final String url) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                view.getContext().startActivity(i);
            }
        });
    }

    @BindingAdapter("bind:timer")
    public static void timeLeft(final TextView view, final Product product) {
        CountDownTimer timeLeftTimer;
        Object timerTag = view.getTag();
        if(timerTag instanceof String && timerTag.equals("Expired")) return;
        if (product.getEndDate() != null && product.getCurrentDate() != null && timerTag == null) {
            timeLeftTimer = new CountDownTimer(product.getEndDate().getTime() - product.getCurrentDate().getTime(), 1000) {
                public void onTick(long millisUntilFinished) {
                    view.setText(String.format("%s: %s",
                            view.getResources().getString(R.string.item_time_left),
                            timeLeftFormatted(millisUntilFinished, view.getContext())
                    ));
                }

                public void onFinish() {
                    view.setText(view.getResources().getString(R.string.item_time_left_expired));
                    view.setTag("Expired");
                    product.setState("Expired");
                }
            };
            view.setTag(timeLeftTimer);
            timeLeftTimer.start();
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("app:cardBackgroundColor")
    public static void setState(CardView view, Product product) {
        if (product.getState() != null && product.getState().equals("Expired"))
            view.setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.whitegray)
            );
    }
}
