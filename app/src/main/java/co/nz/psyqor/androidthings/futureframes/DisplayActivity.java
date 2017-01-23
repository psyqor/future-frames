

package co.nz.psyqor.androidthings.futureframes;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Displays a repeating slideshow of photos
 */
public class DisplayActivity extends FragmentActivity {
    private static final String TAG = DisplayActivity.class.getSimpleName();

    private Handler mHandler = new Handler();
    private PeripheralManagerService pioService = new PeripheralManagerService();

    private static final int NUM_PAGES = 5;
    private static final int SCROLL_INTERVAL = 3000;
    private static final int SCROLL_DURATION = 5;

    private AutoScrollViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private SlideshowManager slideshowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_display);

        mPager = (AutoScrollViewPager) findViewById(R.id.pager);
        configurePager();

        int currentSlideshowId = 2;
        slideshowManager = new SlideshowManager(this, currentSlideshowId);
        slideshowManager.refresh();
        refreshAdapter();
    }

    public void refreshAdapter(){
        Log.i(TAG, "Refreshing Adapter");
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), slideshowManager.getImagePaths());
        mPager.setAdapter(mPagerAdapter);
        mPager.startAutoScroll();
    }

    public void configurePager(){
        mPager.setInterval(SCROLL_INTERVAL);
        mPager.setScrollDurationFactor(SCROLL_DURATION);
        mPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE); // Supposed to scroll at start/end but is buggy
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        mPager.stopAutoScroll();
        // Clear transfer listeners to prevent memory leak, or
        // else this activity won't be garbage collected.
        slideshowManager.cleanObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        mPager.startAutoScroll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private int numPages;
        private List<String> imagePaths;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<String> imagePaths) {
            super(fm);
            this.numPages = imagePaths.size();
            this.imagePaths = imagePaths;
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(position, imagePaths.get(position));
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }



}
