package com.weare.plantree;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

class OnBoarding_Screen_Adapter extends PagerAdapter {

    Context context;

    int images[] = {
            R.drawable.seedbag,
            R.drawable.seedbag,
            R.drawable.seedbag
    };

    int headings[] = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three,
    };

    int description[] = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three,
            R.string.desc_fourth
    };

    // Constructor should match the class name
    public OnBoarding_Screen_Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Math.min(images.length, Math.min(headings.length, description.length)); // Return the minimum length to avoid out-of-bound exceptions
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_screen_view, container, false);

        ImageView slidetitleimage = view.findViewById(R.id.titleImage);
        TextView slideHeading = view.findViewById(R.id.texttitle);
        TextView slideDescription = view.findViewById(R.id.textdescription); // Corrected variable name

        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}