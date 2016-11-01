package com.intprices.util;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.intprices.R;
import com.intprices.api.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alex Poltavets on 20.10.2016.
 */

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
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                view.getContext().startActivity(i);
            }
        });
    }

    @BindingAdapter("bind:TimeLeft")
    public static void setTimeLeft(TextView view, String timeleft){
        String[]split=new String[]{"P","DT","H","M"};
    }

    @BindingAdapter("bind:shipping")
    public static void shippingTo(Spinner view, List<String> shipToLocations){
        if(shipToLocations!=null)
        view.setAdapter(new ArrayAdapter<String>(view.getContext(),R.layout.spinner_item,R.id.spinnertext,shipToLocations));
    }
}
