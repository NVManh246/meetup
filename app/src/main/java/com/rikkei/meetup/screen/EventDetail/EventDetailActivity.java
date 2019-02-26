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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.ultis.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rikkei.meetup.screen.profile.EventStatusActivity.STATUS_GOING;
import static com.rikkei.meetup.screen.profile.EventStatusActivity.STATUS_WENT;
import static com.rikkei.meetup.ultis.StringUtils.DATE_FORMAT;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.View {

    private static final String EXTRA_EVENT = "event";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_event) ImageView mImageEvent;
    @BindView(R.id.text_name_event) TextView mTextNameEvent;
    @BindView(R.id.text_date_event) TextView mTextDate;
    @BindView(R.id.text_location_event) TextView mTextLocation;
    @BindView(R.id.text_joiner) TextView mTextJoinerCount;
    @BindView(R.id.text_description_event) TextView mTextDescription;
    @BindView(R.id.button_going) Button mButtonGoing;
    @BindView(R.id.button_went) Button mButtonWent;

    private EventDetailContract.Presenter mPresenter;
    private Event mEvent;
    private String mToken;

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
        ButterKnife.bind(this);
        setupToolbar();
        initView();
        mPresenter = new EventDetailPresenter(this);
        mToken = StringUtils.getToken(this);
    }

    @OnClick(R.id.button_going)
    public void onGoingClick() {
        mPresenter.updateStatusEvent(mToken, STATUS_GOING, mEvent.getId());
    }

    @OnClick(R.id.button_went)
    public void onWentClick() {
        mPresenter.updateStatusEvent(mToken, STATUS_WENT, mEvent.getId());
    }

    @OnClick(R.id.image_add_location)
    public void onAddLocationClick() {
        mPresenter.followVenue(mToken, mEvent.getVenue().getId());
    }

    private void initView() {
        if(mEvent.getPhoto() != null) {
            Glide.with(this).load(mEvent.getPhoto()).into(mImageEvent);
        }
        mTextNameEvent.setText(mEvent.getName());
        mTextDate.setText(mEvent.getScheduleStartDate() + " to " + mEvent.getScheduleEndDate());
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
        try {
            setEnableButton();
        } catch (ParseException e) {
            System.out.println(e.toString());
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

    private void setEnableButton() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date currentDate = Calendar.getInstance().getTime();
        Date startDate = dateFormat.parse(mEvent.getScheduleStartDate());

        if(currentDate.before(startDate)) {
            mButtonGoing.setEnabled(true);
            mButtonWent.setEnabled(false);
        } else {
            mButtonGoing.setEnabled(false);
            mButtonWent.setEnabled(true);
        }
    }

    @Override
    public void showUpdateSuccess() {
        Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUpdateError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFollowSuccess() {
        Toast.makeText(this, R.string.follow_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFollowError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorServer() {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }
}
