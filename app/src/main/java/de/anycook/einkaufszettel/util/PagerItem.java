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

package de.anycook.einkaufszettel.util;

import android.support.v4.app.Fragment;
import de.anycook.einkaufszettel.activities.fragments.RecipeDetailFragment;
import de.anycook.einkaufszettel.activities.fragments.RecipeIngredientListFragment;
import de.anycook.einkaufszettel.activities.fragments.RecipeStepListFragment;

/**
 * This class represents a tab to be displayed by {@link android.support.v4.view.ViewPager} and it's associated
 * {@link de.anycook.einkaufszettel.view.SlidingTabLayout}.
 * @author Jan Graßegger<jan@anycook.de>
 */
public class PagerItem {
    private final CharSequence title;
    private final int indicatorColor;
    private final int dividerColor;

    public PagerItem(CharSequence title, int indicatorColor, int dividerColor) {
        this.title = title;
        this.indicatorColor = indicatorColor;
        this.dividerColor = dividerColor;
    }

    /**
     * @return A new {@link android.support.v4.app.Fragment} to be displayed by a {@link android.support.v4.view.ViewPager}
     */
    public Fragment createFragment() {
        if (title.equals("Zutaten")) {
            return new RecipeIngredientListFragment();
        } else if (title.equals("Details")) {
            return new RecipeDetailFragment();
        }

        return new RecipeStepListFragment();
    }

    /**
     * @return the title which represents this tab. In this sample this is used directly by
     * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
     */
    public CharSequence getTitle() {
        return title;
    }

    /**
     * @return the color to be used for indicator on the {@link de.anycook.einkaufszettel.view.SlidingTabLayout}
     */
    public int getIndicatorColor() {
        return indicatorColor;
    }

    /**
     * @return the color to be used for right divider on the {@link de.anycook.einkaufszettel.view.SlidingTabLayout}
     */
    public int getDividerColor() {
        return dividerColor;
    }
}
