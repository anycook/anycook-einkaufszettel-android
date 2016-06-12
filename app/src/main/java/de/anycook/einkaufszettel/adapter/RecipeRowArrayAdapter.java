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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeRowArrayAdapter
        extends RecyclerView.Adapter<RecipeRowArrayAdapter.RecipeViewHolder> {

    private List<RecipeResponse> recipes;
    private Activity activity;

    public RecipeRowArrayAdapter(Activity activity) {
        this(activity, new ArrayList<RecipeResponse>());
    }

    public RecipeRowArrayAdapter(Activity activity, List<RecipeResponse> recipes) {
        this.recipes = recipes;
        this.activity = activity;
    }

    public List<RecipeResponse> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeResponse> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public RecipeResponse getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View
                view =
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.discover_row, viewGroup, false);

        return new RecipeViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.setRecipeResponse(recipes.get(i));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView textViewName;
        private TextView textViewDescription;
        private ImageView imageView;

        private RecipeResponse recipeResponse;
        private Activity activity;

        public RecipeViewHolder(View view, Activity activity) {
            super(view);
            view.setOnClickListener(this);

            textViewName = (TextView) view.findViewById(R.id.textview_title);
            textViewDescription = (TextView) view.findViewById(R.id.description);

            imageView = (ImageView) view.findViewById(R.id.imageview);

            this.activity = activity;
        }

        public void setRecipeResponse(RecipeResponse recipeResponse) {
            this.recipeResponse = recipeResponse;

            textViewName.setText(recipeResponse.getName());
            textViewDescription.setText(recipeResponse.getDescription());
            new DownloadImageViewTask(imageView).execute(recipeResponse.getImage().getBig());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), RecipeActivity.class);
            Bundle b = new Bundle();
            b.putString("item", recipeResponse.getName());
            intent.putExtras(b);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, v.findViewById(R.id.imageview),
                    // The view which starts the transition
                    activity.getString(R.string.recipe_transition)
            );

            if (options == null) {
                v.getContext().startActivity(intent);
            } else {
                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

        }

    }
}
