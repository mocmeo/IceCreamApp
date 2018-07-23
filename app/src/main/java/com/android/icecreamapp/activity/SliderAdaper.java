package com.android.icecreamapp.activity;

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
            "HEADING 1",
            "HEADING 2",
            "HEADING 3"
    };

    public String[] slide_descriptions = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam."
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
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

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
