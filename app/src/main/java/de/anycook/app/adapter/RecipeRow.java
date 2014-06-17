package de.anycook.app.adapter;

public class RecipeRow {

    private Integer recipeImage;
    private String recipeName;
    private String recipeDescription;


    public RecipeRow(Integer imageID, String recipeName, String recipeDescription) {
        this.recipeImage = imageID;
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

    public Integer getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Integer recipeImage) {
        this.recipeImage = recipeImage;
    }

    @Override
    public String toString() {
        return "ImageID" + recipeImage + "\t" + recipeName + "\n" + recipeDescription;
    }
}