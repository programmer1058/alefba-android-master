package com.todobom.opennotescanner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class AboutActivity extends Activity {


    Typeface title_font, subtitle_font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        title_font = Typeface.createFromAsset(
//                AboutActivity.this.getAssets(),
//                "fonts/Ordibehesht.TTF");
//        subtitle_font = Typeface.createFromAsset(
//                AboutActivity.this.getAssets(),
//                "fonts/B Nazanin_YasDL.com.ttf");
//
//        ((TextView) findViewById(R.id.title)).setTypeface(title_font);
//        ((TextView) findViewById(R.id.subtitle_1)).setTypeface(subtitle_font);
//        ((TextView) findViewById(R.id.subtitle_2)).setTypeface(subtitle_font);

    }

}
