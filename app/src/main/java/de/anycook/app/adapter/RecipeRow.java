package de.anycook.app.adapter;

public class RecipeRow {

    private String recipeImageUrl;
    private String recipeName;
    private String recipeDescription;


    public RecipeRow(String imageUrl, String recipeName, String recipeDescription) {
        this.recipeImageUrl = imageUrl;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
    }

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

    @Override
    public String toString() {
        return "ImageID" + recipeImageUrl + "\t" + recipeName + "\n" + recipeDescription;
    }
}