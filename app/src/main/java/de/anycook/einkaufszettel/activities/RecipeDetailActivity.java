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

package de.anycook.einkaufszettel.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.DownloadImageTask;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeDetailActivity extends ActionBarActivity{

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.recipe_detail_view);

        RecipeResponse recipeResponse = getIntent().getParcelableExtra("recipe");

        ImageView imageView = (ImageView)findViewById(R.id.recipe_image);
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
        downloadImageTask.execute(recipeResponse.getImage().getBig());

        TextView recipeTitleView = (TextView)findViewById(R.id.recipe_title_text);
        recipeTitleView.setText(recipeResponse.getName());

        TextView recipeDescriptionView = (TextView)findViewById(R.id.recipe_description_text);
        recipeDescriptionView.setText(recipeResponse.getDescription());
    }

    public void onClick(View view) {
        finish();
    }
}
