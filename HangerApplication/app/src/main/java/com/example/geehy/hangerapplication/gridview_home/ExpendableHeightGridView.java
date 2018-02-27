package com.example.geehy.hangerapplication.gridview_home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by JHS on 2018-02-26.
 */

public class ExpendableHeightGridView extends GridView {

    boolean expended = false;

    public ExpendableHeightGridView(Context context) {
        super(context);
    }

    public ExpendableHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpendableHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpendableHeightGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isExpended() {
        return expended;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isExpended()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST
            );
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
    }

    public void setExpended(boolean expended){
        this.expended = expended;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev){
//        // Called when a child does not want this parent and its ancestors to intercept touch events.
//        requestDisallowInterceptTouchEvent(true);
//
//        return super.onTouchEvent(ev);
//    }
}


