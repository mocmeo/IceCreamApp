package com.android.icecreamapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.icecreamapp.R;

public class SliderAdaper extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public int[] slide_images = {
            R.drawable.ic_intro_1,
            R.drawable.ic_intro_2,
            R.drawable.ic_intro_3

    };

    public String[] slide_headings = {
            "ICECREAM EZKEM",
            "ABOUT US",
            "SWEET ICECREAM"
    };

    public String[] slide_descriptions = {
            "Ice cream parlors (or parlours) are restaurants that sell ice cream, gelato, sorbet, and frozen yogurt to consumers.",
            "Ez-Kem represents everything from old-school custard outposts and soft-serve emporiums to new-wave artisan",
            "Next time you're craving a sweet scoop (or two), do yourself a favor. Just go to Ez-Kem parlor!"
    };

    public SliderAdaper(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_intro_layout,container,false);

        ImageView imageViewSlide = view.findViewById(R.id.imageViewSlide);
        TextView textViewHeading = view.findViewById(R.id.textViewHeading);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);

        imageViewSlide.setImageResource(slide_images[position]);
        textViewHeading.setText(slide_headings[position]);
        textViewDescription.setText(slide_descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
