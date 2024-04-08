package com.us.mytest.ui.activity.show_bitmap;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.goldze.mvvmhabit.widget.ZoomImageView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.us.mytest.R;

public class ShowBitmapActivity extends AppCompatActivity {

    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bitmap);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
                .transparentBar().init();
        ZoomImageView imageView = findViewById(R.id.test_image);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        imageView.setOnClickListener(view -> finishTest());
    }

    private void finishTest() {
        finish();
        overridePendingTransition(R.anim.main_enter, R.anim.test_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishTest();
    }
}