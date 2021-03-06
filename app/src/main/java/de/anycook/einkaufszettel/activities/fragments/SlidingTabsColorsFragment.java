/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2015 Jan Graßegger, Claudia Sichting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.util.PagerItem;
import de.anycook.einkaufszettel.view.SlidingTabLayout;
import de.anycook.einkaufszettel.view.TabColorizer;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic sample which shows how to use {@link SlidingTabLayout} to display a custom {@link
 * ViewPager} title strip which gives continuous feedback to the user when scrolling.
 */
public class SlidingTabsColorsFragment extends Fragment {

    /**
     * List of {@link PagerItem} which represent this sample's tabs.
     */
    private List<PagerItem> tabs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Populate our tab list with tabs. Each item contains a title, indicator color and divider
         * color, which are used by {@link SlidingTabLayout}.
         */
        tabs.add(new PagerItem(
                getString(R.string.ingredients),
                ContextCompat.getColor(getContext(), R.color.any_white_gray),
                Color.GRAY
        ));

        tabs.add(new PagerItem(
                getString(R.string.details),
                ContextCompat.getColor(getContext(), R.color.any_white_gray),
                Color.GRAY
        ));

        tabs.add(new PagerItem(
                getString(R.string.preparation),
                ContextCompat.getColor(getContext(), R.color.any_white_gray),
                Color.GRAY
        ));
    }

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.viewpager_fragment, container, false);
    }

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has
     * finished. Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link
     * SampleFragmentPagerAdapter}. The {@link SlidingTabLayout} is then given the {@link ViewPager}
     * so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        /*
         * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout}
         * above.
         */
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        /*
         * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0
         * and above, but is designed to give continuous feedback to the user when scrolling.
         */
        SlidingTabLayout slidingTabLayout =
                (SlidingTabLayout) getActivity().findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        // Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
        // the tab at the position, and return it's set color
        slidingTabLayout.setCustomTabColorizer(new TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return tabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return tabs.get(position).getDividerColor();
            }

        });
    }

    /**
     * The {@link FragmentPagerAdapter} used to display pages in this sample. The individual pages
     * are instances of ContentFragment which just display three lines of text. Each page is created
     * by the relevant {@link de.anycook.einkaufszettel.util.PagerItem} for the requested position.
     * <p> The important section of this class is the {@link #getPageTitle(int)} method which
     * controls what is displayed in the {@link SlidingTabLayout}.
     */
    private class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
         * <p> Here we return the value returned from {@link PagerItem#createFragment()}.
         */
        @Override
        public Fragment getItem(int i) {
            return tabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}. <p> Here we return the
         * value returned from {@link PagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getTitle();
        }

    }
}