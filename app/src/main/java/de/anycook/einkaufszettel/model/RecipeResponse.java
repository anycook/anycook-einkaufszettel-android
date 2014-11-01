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

/**
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeResponse {
    private String name;
    private String description;
    private RecipeImage image;
    private int persons;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return this.image.getSmallImage();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(RecipeImage image) {
        this.image = image;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return name;
    }

    private static class RecipeImage {
        protected String small;

        public String getSmallImage() {
            return this.small;
        }

    }

}


