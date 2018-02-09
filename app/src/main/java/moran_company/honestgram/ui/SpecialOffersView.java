package moran_company.honestgram.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.SpecialOfferAdapter;
import moran_company.honestgram.data.Goods;

/**
 * Created by roman on 24.01.2018.
 */

public class SpecialOffersView<T> extends ConstraintLayout implements SpecialOfferAdapter.OnBannerClick ,ViewPager.OnPageChangeListener{

    private SpecialOfferAdapter mAdapter;

    ArrayList<T> mGoods = new ArrayList<>();

    ArrayList<String> urls = new ArrayList<>();

    private Timer mTimer;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;

    public SpecialOffersView(Context context) {
        super(context);
        init();
    }

    public SpecialOffersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecialOffersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null)
            return;
        inflater.inflate(R.layout.special_offer_content,this);
        ButterKnife.bind(this,this);
        mAdapter = new SpecialOfferAdapter(mGoods,this);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);
        circleIndicator.setViewPager(viewPager);
        mAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

    }

    public void setCircleIndicatorVisibility() {

        //TODO circleIndicator.setVisibility();
    }


    public void setGoods(ArrayList<T> mGoods,boolean rounded) {
        this.mGoods = mGoods;
        initGoods(rounded);
    }

    public void setListGoods(ArrayList<Goods> mGoods,boolean rounded){
        this.mGoods = (ArrayList<T>) mGoods;
        initGoods(rounded);
    }

    private void initGoods(boolean rounded) {
       // mGoods.clear();

        if (mGoods.isEmpty())
            getLayoutParams().height = 0;
        else {
            mAdapter.setItems(mGoods);
            mAdapter.setWithRoundedCorners(rounded);
            startTimer();
            //getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        }
    }

    private void startTimer() {
        stopTimer();
        if (mGoods.size() == 1)
            return;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(() -> {
                    int newPosition = viewPager.getCurrentItem() + 1;
                    if (newPosition == mGoods.size())
                        newPosition = 0;
                    viewPager.setCurrentItem(newPosition);
                });
            }
        }, 3000, 3000);
    }

    private void stopTimer() {
        if (mTimer == null)
            return;
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onBannerClick(int position) {
        stopTimer();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
