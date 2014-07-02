package de.anycook.app.controller.util;

/**
 * Created by cipo7741 on 01.07.14.
 */
public class RecipeResponse {
    private String name;
    private String description;
    private RecipeImage image;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(RecipeImage recipeImage) {
        this.image = recipeImage;
    }

    public String getDescription() {
        return description;
    }


    public String getImage() {
        return this.image.getSmallImage();
    }


    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }


    private static class RecipeImage {
        private String small;

        public String getSmallImage() {
            return this.small;
        }

        public void setSmallImage(String smallImage) {
            this.small = smallImage;
        }
    }


}


