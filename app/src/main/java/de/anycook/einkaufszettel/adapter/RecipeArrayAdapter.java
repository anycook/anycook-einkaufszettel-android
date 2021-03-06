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

package de.anycook.einkaufszettel.adapter;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeArrayAdapter
        extends RecyclerView.Adapter<RecipeArrayAdapter.RecipeViewHolder> {

    private final Activity activity;
    private final Tracker tracker;

    private List<RecipeResponse> recipes;

    public RecipeArrayAdapter(final Activity activity, final Tracker tracker) {
        this.activity = activity;
        this.tracker = tracker;
        this.clear();
    }

    public void addRecipes(final List<RecipeResponse> recipes) {
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void clear() {
        this.recipes = new LinkedList<>();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.discover_element, viewGroup, false);

        return new RecipeViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder recipeViewHolder, final int i) {
        recipeViewHolder.setRecipeResponse(recipes.get(i));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final Activity activity;

        private final TextView textViewName;
        private final TextView textViewNumFavorites;
        private final ImageView imageView;

        private RecipeResponse recipeResponse;

        RecipeViewHolder(final View view, final Activity activity) {
            super(view);
            view.setOnClickListener(this);

            this.textViewName = (TextView) view.findViewById(R.id.textview_title);
            this.textViewNumFavorites = (TextView) view.findViewById(R.id.number_favorites);
            this.imageView = (ImageView) view.findViewById(R.id.imageview);

            this.activity = activity;
        }

        void setRecipeResponse(final RecipeResponse recipeResponse) {
            this.recipeResponse = recipeResponse;

            textViewName.setText(recipeResponse.getName());
            int tasteNum = recipeResponse.getTasteNum();
            if (tasteNum > 0) {
                textViewNumFavorites.setText(String.format(Locale.getDefault(), "%d", tasteNum));
                textViewNumFavorites.setVisibility(View.VISIBLE);
            }
            new DownloadImageViewTask(imageView).execute(recipeResponse.getImage().getBig());
        }

        @Override
        public void onClick(final View v) {
            final Intent intent = new Intent(v.getContext(), RecipeActivity.class);
            final Bundle b = new Bundle();
            final String recipeName = recipeResponse.getName();
            b.putString("item", recipeName);
            intent.putExtras(b);

            tracker.send(new HitBuilders.EventBuilder()
                                 .setCategory("Action")
                                 .setAction("ClickOnRecipe")
                                 .setLabel(recipeName)
                                 .build());

            activity.startActivity(intent);
        }

    }
}
