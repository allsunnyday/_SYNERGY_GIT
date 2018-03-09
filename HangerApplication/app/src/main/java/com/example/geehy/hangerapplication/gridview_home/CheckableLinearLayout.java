package com.example.geehy.hangerapplication.gridview_home;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.example.geehy.hangerapplication.R;

public class CheckableLinearLayout extends LinearLayout implements Checkable {


    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // mIsChecked = false ;
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.check);

        return cb.isChecked();
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.check);

        setChecked(cb.isChecked() ? false : true);
        // setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.check);

        if (cb.isChecked() != checked) {
            cb.setChecked(checked);
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }
}