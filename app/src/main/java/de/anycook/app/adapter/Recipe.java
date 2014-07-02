package de.anycook.app.adapter;

public class Recipe {

    private String recipeImageUrl;
    private String recipeName;
    private String recipeDescription;
    private long id;


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImage) {
        this.recipeImageUrl = recipeImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImageID" + recipeImageUrl + "\t" + recipeName + "\n" + recipeDescription;
    }
}