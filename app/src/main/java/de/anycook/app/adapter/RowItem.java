package de.anycook.app.adapter;

public class RowItem {

    private String ingredient;
    private String amount;
    private boolean checked;

    public RowItem(String ingredient, String amount) {

        this.ingredient = ingredient;
        this.amount = amount;
        this.checked = false;
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

    public void getChecked(boolean flag) { this.checked = flag; }
    public void setChecked(boolean flag) { this.checked = flag; }


    @Override
    public String toString() {
        return ingredient + "\n" + amount;
    }
}