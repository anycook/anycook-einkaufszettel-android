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

package de.anycook.einkaufszettel.model;

import de.anycook.einkaufszettel.util.StringTools;

/**
 * anycook-api json response object ingredient
 *
 * @author Jan Graßegger<jan@anycook.de>
 */
public class Ingredient {

    private String name;
    private String amount;
    private boolean checked;

    // empty constructor needed for api access in LoadRecipeIngredientsTask
    public Ingredient() {
        this("", "");
    }

    public Ingredient(String name, String amount) {
        this(name, amount, true);
    }

    public Ingredient(String name, String amount, boolean checked) {
        setName(name);
        setAmount(amount);
        setChecked(checked);
    }

    public Ingredient(Ingredient ingredient) {
        this.name = ingredient.name;
        this.amount = ingredient.amount;
        this.checked = ingredient.checked;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public void setAmount(String desc) {
        this.amount = desc;
    }

    public void setChecked(boolean stroked) {
        this.checked = stroked;
    }

    public void multiplyAmount(int recipePersons, int newPersons) {
        if (recipePersons == newPersons) {
            return;
        }
        amount = StringTools.multiplyAmount(amount, recipePersons, newPersons);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if  (!amount.isEmpty()) {
            builder.append(amount).append(' ');
        }
        builder.append(name);
        return builder.toString();
    }
}
