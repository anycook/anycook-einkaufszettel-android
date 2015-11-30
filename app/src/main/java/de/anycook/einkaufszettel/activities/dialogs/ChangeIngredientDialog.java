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

package de.anycook.einkaufszettel.activities.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.model.Ingredient;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class ChangeIngredientDialog {

    private final Activity activity;
    private final Callback callback;

    private Ingredient ingredient;


    public ChangeIngredientDialog(Activity activity, Ingredient ingredient, Callback callback) {
        this.activity = activity;
        this.callback = callback;
        this.ingredient = ingredient;
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Zutat und Menge ändern");

        final LayoutInflater inflater = activity.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.change_ingredient_dialog, null);
        ((TextView) dialogView.findViewById(R.id.edittext_name)).setText(ingredient.getName());
        ((TextView) dialogView.findViewById(R.id.edittext_amount)).setText(ingredient.getAmount());
        builder.setView(dialogView);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newName =
                        ((TextView) dialogView.findViewById(R.id.edittext_name)).getText()
                                .toString();
                final String newAmount =
                        ((TextView) dialogView.findViewById(R.id.edittext_amount)).getText()
                                .toString();
                Ingredient newIngredient = new Ingredient();

                newIngredient.setName(newName);
                newIngredient.setAmount(newAmount);
                newIngredient.setChecked(ingredient.isChecked());

                callback.ingredientChanged(newIngredient);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public interface Callback {

        void ingredientChanged(Ingredient newIngredient);
    }
}
