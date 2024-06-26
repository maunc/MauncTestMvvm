package com.us.mauncview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class WaterRipplesView extends View {
    private Paint centerPaint; //中心圆paint
    private int radius = 100; //中心圆半径
    private Paint spreadPaint; //扩散圆paint
    private int centerX;//圆心x
    private int centerY;//圆心y
    private int centerColor;//中心颜色
    private int spreadColor;//扩散颜色
    private int distance = 5; //每次圆递增间距
    private int maxRadius = 80; //最大圆半径
    private int delayMilliseconds = 40;//扩散延迟间隔，越大扩散越慢
    private final List<Integer> spreadRadius = new ArrayList<>();//扩散圆层级数，元素为扩散的距离
    private final List<Integer> alphas = new ArrayList<>();//对应每层圆的透明度

    public WaterRipplesView(Context context) {
        super(context);
    }

    public WaterRipplesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceType")
    public WaterRipplesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterRipplesView, defStyleAttr, 0);
        radius = a.getInt(R.styleable.WaterRipplesView_spreadCenterColor, radius);
        maxRadius = a.getInt(R.styleable.WaterRipplesView_spreadMaxRadius, maxRadius);
        centerColor = a.getColor(R.styleable.WaterRipplesView_spreadCenterColor, ContextCompat.getColor(context, R.color.red));
        spreadColor = a.getColor(R.styleable.WaterRipplesView_spreadColor, ContextCompat.getColor(context, R.color.red));
        distance = a.getInt(R.styleable.WaterRipplesView_spreadDistance, distance);
        delayMilliseconds = a.getInt(R.styleable.WaterRipplesView_spreadDelayMilliseconds, 40);
        a.recycle();
        initConfig();
    }

    private void initConfig() {
        centerPaint = new Paint();
        centerPaint.setColor(centerColor);
        centerPaint.setAntiAlias(true);
        //最开始不透明且扩散距离为0
        alphas.add(255);
        spreadRadius.add(0);
        spreadPaint = new Paint();
        spreadPaint.setAntiAlias(true);
        spreadPaint.setAlpha(255);
        spreadPaint.setColor(spreadColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆心位置
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < spreadRadius.size(); i++) {
            int alpha = alphas.get(i);
            spreadPaint.setAlpha(alpha);
            int width = spreadRadius.get(i);
            //绘制扩散的圆
            canvas.drawCircle(centerX, centerY, radius + width, spreadPaint);
            //每次扩散圆半径递增，圆透明度递减
            if (alpha > 0 && width < 300) {
                alpha = alpha - distance > 0 ? alpha - distance : 1;
                alphas.set(i, alpha);
                spreadRadius.set(i, width + distance);
            }
        }
        //当最外层扩散圆半径达到最大半径时添加新扩散圆
        if (spreadRadius.get(spreadRadius.size() - 1) > maxRadius) {
            spreadRadius.add(0);
            alphas.add(255);
        }
        //超过8个扩散圆，删除最先绘制的圆，即最外层的圆
        if (spreadRadius.size() >= 8) {
            alphas.remove(0);
            spreadRadius.remove(0);
        }
        //中间的圆
        canvas.drawCircle(centerX, centerY, radius, centerPaint);
        //TODO 可以在中间圆绘制文字或者图片

        //延迟更新，达到扩散视觉差效果
        postInvalidateDelayed(delayMilliseconds);
    }
}
