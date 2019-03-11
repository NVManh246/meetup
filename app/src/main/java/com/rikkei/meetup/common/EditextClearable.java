package com.rikkei.meetup.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rikkei.meetup.R;

public class EditextClearable extends android.support.v7.widget.AppCompatEditText {

    private Drawable mImageClear = getResources().getDrawable(R.drawable.ic_clear_black_24dp);

    public EditextClearable(Context context) {
        super(context);
        init();
    }

    public EditextClearable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditextClearable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mImageClear.setBounds(0, 0,
                mImageClear.getIntrinsicWidth(), mImageClear. getIntrinsicHeight());

        handleClearButton();

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(getCompoundDrawables()[2] == null) {
                    return false;
                }
                if(event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if(event.getX() > getWidth() - getPaddingRight() - mImageClear.getIntrinsicWidth()) {
                    setText("");
                    handleClearButton();
                }
                return false;
            }
        });

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void handleClearButton() {
        if(getText().toString().equals("")) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mImageClear, getCompoundDrawables()[3]);
        }
    }
}
