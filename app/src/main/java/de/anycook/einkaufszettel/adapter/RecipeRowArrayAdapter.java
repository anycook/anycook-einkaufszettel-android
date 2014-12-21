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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.AddIngredientsActivity;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeRowArrayAdapter extends RecyclerView.Adapter<RecipeRowArrayAdapter.RecipeViewHolder> {
    private List<RecipeResponse> recipes;

    public RecipeRowArrayAdapter() {
        this(new ArrayList<RecipeResponse>());
    }

    public RecipeRowArrayAdapter(List<RecipeResponse> recipes) {
        this.recipes = recipes;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discover_row, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.setRecipeResponse(recipes.get(i));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName;
        private ImageView imageView;

        private RecipeResponse recipeResponse;

        public RecipeViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            textViewName = (TextView) view.findViewById(R.id.recipe_row_textview_recipe_name);
            imageView = (ImageView) view.findViewById(R.id.recipe_row_imageview);
        }

        public void setRecipeResponse(RecipeResponse recipeResponse) {
            this.recipeResponse = recipeResponse;

            textViewName.setText(recipeResponse.getName());
            //textViewDescription.setText(recipeResponse.getDescription());
            new DownloadImageViewTask(imageView).execute(recipeResponse.getImage().getBig());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddIngredientsActivity.class);
            Bundle b = new Bundle();
            b.putString("item", recipeResponse.getName());
            intent.putExtras(b);
            v.getContext().startActivity(intent);
        }
    }
}
