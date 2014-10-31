/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger
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

/**
 * anycook-api json response object ingredient
 *
 * @author Jan Graßegger<jan@anycook.de>
 */
public class GroceryItem {

    private String name;
    private String amount;
    private boolean stroked;

    public GroceryItem() {
        this.setName("");
        this.setAmount("");
        this.setStroked(false);
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isStroked() {
        return stroked;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public void setAmount(String desc) {
        this.amount = desc;
    }

    public void setStroked(boolean stroked) {
        this.stroked = stroked;
    }

    @Override
    public String toString() {
        return name + "\t" + amount;
    }
}