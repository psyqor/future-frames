/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.nz.psyqor.androidthings.futureframes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.things.pio.PeripheralManagerService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_display);

        mPager = (AutoScrollViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        configurePager();
    }

    public void configurePager(){
        mPager.setInterval(SCROLL_INTERVAL);
        mPager.setScrollDurationFactor(SCROLL_DURATION);
        mPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE); // Supposed to scroll at start/end but is buggy
        mPager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        mPager.stopAutoScroll();
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
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
