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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeResponse implements Parcelable{
    private String name;
    private String description;
    private RecipeImage image;
    private int persons;

    public RecipeResponse() {

    }

    public RecipeResponse(Parcel parcel) {
        this.name = parcel.readString();
        this.description = parcel.readString();

        this.image = new RecipeImage();
        image.setBig(parcel.readString());
        image.setSmall(parcel.readString());

        this.persons = parcel.readInt();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public RecipeImage getImage() {
        return this.image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(image.big);
        parcel.writeString(image.small);
        parcel.writeInt(persons);
    }

    public static class RecipeImage {
        protected String small;
        protected String big;


        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getBig() {
            return big;
        }

        public void setBig(String big) {
            this.big = big;
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RecipeResponse createFromParcel(Parcel in) {
            return new RecipeResponse(in);
        }

        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

}


