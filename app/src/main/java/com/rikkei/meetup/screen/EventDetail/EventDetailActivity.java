package com.rikkei.meetup.screen.EventDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.ultis.StringUtils;

import java.text.ParseException;

public class EventDetailActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT = "event";

    private Toolbar mToolbar;
    private ImageView mImageEvent;
    private TextView mTextNameEvent;
    private TextView mTextDate;
    private TextView mTextLocation;
    private TextView mTextJoinerCount;
    private TextView mTextDescription;

    private Event mEvent;

    public static Intent getEventDetailIntent(Context context, Event event) {
        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT, event);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);
        mEvent = getIntent().getParcelableExtra(EXTRA_EVENT);
        initView();
        setupToolbar();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mImageEvent = findViewById(R.id.image_event);
        mTextNameEvent = findViewById(R.id.text_name_event);
        mTextDate = findViewById(R.id.text_date_event);
        mTextLocation = findViewById(R.id.text_location_event);
        mTextJoinerCount = findViewById(R.id.text_joiner);
        mTextDescription = findViewById(R.id.text_description_event);

        if(mEvent.getPhoto() != null) {
            Glide.with(this).load(mEvent.getPhoto()).into(mImageEvent);
        }
        mTextNameEvent.setText(mEvent.getName());
        try {
            mTextDate.setText(StringUtils.getDateEventField(this, mEvent));
        } catch (ParseException e) {
        }
        mTextLocation.setText(mEvent.getVenue().getName());
        mTextJoinerCount.setText(String.valueOf(mEvent.getGoingCount()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextDescription.setText(Html.fromHtml(
                    mEvent.getDescriptionHtml(),
                    Html.FROM_HTML_MODE_COMPACT)
            );
        } else {
            mTextDescription.setText(Html.fromHtml(mEvent.getDescriptionHtml()));
        }
    }

    private void setupToolbar() {
        mToolbar.setTitle(mEvent.getName());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
