package de.anycook.app.adapter;

public class IngredientRow {

    private String ingredient;
    private String amount;

    public IngredientRow(String ingredient, String amount) {

        this.ingredient = ingredient;
        this.amount = amount;
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

    @Override
    public String toString() {
        return ingredient + "\t" + amount;
    }
}