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
 * @author Jan Graßegger <jan@anycook.de>>
 */
public class RecipeResponse implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RecipeResponse createFromParcel(Parcel in) {
            return new RecipeResponse(in);
        }

        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

    private String name;
    private String description;
    private Image image;
    private int persons;
    private Time time;
    private String category;
    private int skill;
    private int calorie;
    private long lastChange;
    private int tasteNum;

    public RecipeResponse() {

    }

    public RecipeResponse(Parcel parcel) {
        this.name = parcel.readString();
        this.description = parcel.readString();

        this.image = new Image();
        image.setBig(parcel.readString());
        image.setSmall(parcel.readString());

        this.persons = parcel.readInt();

        this.time = new Time();
        time.setStd(parcel.readInt());
        time.setMin(parcel.readInt());

        this.category = parcel.readString();
        this.skill = parcel.readInt();
        this.calorie = parcel.readInt();

        this.lastChange = parcel.readLong();
        this.tasteNum = parcel.readInt();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (description != null) {
            return description.trim();
        }

        return null;
    }

    public Image getImage() {
        return this.image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public int getTasteNum() {
        return tasteNum;
    }

    public void setTasteNum(int tasteNum) {
        this.tasteNum = tasteNum;
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
        parcel.writeInt(time.getStd());
        parcel.writeInt(time.getMin());
        parcel.writeString(category);
        parcel.writeInt(skill);
        parcel.writeInt(calorie);
        parcel.writeLong(lastChange);
        parcel.writeInt(tasteNum);
    }

    public static class Image {

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

    public static class Time {

        private int std;
        private int min;

        public int getStd() {
            return std;
        }

        public void setStd(int std) {
            this.std = std;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }
}


