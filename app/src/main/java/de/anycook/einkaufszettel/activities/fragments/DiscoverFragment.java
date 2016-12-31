/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger, Claudia Sichting
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.RecipeArrayAdapter;
import de.anycook.einkaufszettel.tasks.LoadDiscoverRecipesTask;
import de.anycook.einkaufszettel.util.AnalyticsApplication;

import java.util.Locale;

/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class DiscoverFragment extends Fragment {

    private static final String URL_PATTERN =
            "https://api.anycook.de/discover/%s?recipeNumber=20&offset=%d";
    private Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AnalyticsApplication application =
                (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.discover_list, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        final RecipeArrayAdapter adapter = new RecipeArrayAdapter(getActivity(), tracker);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        final GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final SwipeRefreshLayout refreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.refresh);

        final EndlessScrollListener scrollListener =
                new EndlessScrollListener(adapter, layoutManager, refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                scrollListener.reset();
                loadRecipes(adapter);
                refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setColorSchemeResources(R.color.any_green, R.color.accent_color);
        loadRecipes(adapter);
        recyclerView.addOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String discoverType = getArguments().getString("type");
        tracker.setScreenName("Discover~" + discoverType);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadRecipes(final RecipeArrayAdapter adapter) {
        final int offset = adapter.getItemCount();
        final String url = String.format(Locale.getDefault(), URL_PATTERN,
                                         getArguments().getString("type"), offset);
        final LoadDiscoverRecipesTask loadDiscoverRecipesTask =
                new LoadDiscoverRecipesTask(adapter, getActivity());
        loadDiscoverRecipesTask.execute(url);
    }

    private class EndlessScrollListener extends RecyclerView.OnScrollListener {
        private final int visibleThreshold = 2;
        private final RecipeArrayAdapter adapter;
        private final GridLayoutManager layoutManager;
        private SwipeRefreshLayout refreshLayout;

        private boolean loading = false;
        private int previousTotal = 0;

        EndlessScrollListener(final RecipeArrayAdapter adapter,
                              final GridLayoutManager layoutManager,
                              final SwipeRefreshLayout refreshLayout) {
            this.adapter = adapter;
            this.layoutManager = layoutManager;
            this.refreshLayout = refreshLayout;
        }

        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    refreshLayout.setRefreshing(false);
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                refreshLayout.setRefreshing(true);
                loading = true;
                loadRecipes(adapter);
            }
        }

        void reset() {
            this.previousTotal = 0;
        }
    }
}
