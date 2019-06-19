package com.cara2v.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyCustomTextViewAaudiowideRegular extends AppCompatTextView {

    public MyCustomTextViewAaudiowideRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyCustomTextViewAaudiowideRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyCustomTextViewAaudiowideRegular(Context context) {
        super(context);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface mycustomfont = Typeface.createFromAsset(getContext().getAssets(), "font/audiowide_regular.ttf");
            setTypeface(mycustomfont);
        }
    }
}
