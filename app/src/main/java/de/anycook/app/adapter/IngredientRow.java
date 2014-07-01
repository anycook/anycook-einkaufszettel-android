package de.anycook.app.adapter;

public class IngredientRow {

    private String ingredient;
    private String amount;
    private boolean struckOut;

    public IngredientRow(String ingredient, String amount) {
        this.setIngredient(ingredient);
        this.setAmount(amount);
        this.setStruckOut(false);
    }

    public IngredientRow(String ingredient) {
        this.setIngredient(ingredient);
        this.setAmount("");
        this.setStruckOut(false);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String desc) {
        this.amount = desc;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public boolean getStruckOut() {
        return this.struckOut;
    }

    public void setStruckOut(boolean struckOut) {
        this.struckOut = struckOut;
    }

    @Override
    public String toString() {
        return ingredient + "\t" + amount;
    }
}