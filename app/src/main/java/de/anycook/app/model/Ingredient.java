package de.anycook.app.model;

import android.util.Log;

/**
 * anycook-api json response object ingredient
 *
 * @author Jan Graßegger<jan@anycook.de>
 */
public class Ingredient {
    private String name;
    private String menge;
    private boolean checked;

    // empty constructor needed for api access in LoadRecipeIngredientsTask
    public Ingredient() {
        setName("");
        setAmount("");
        setChecked(true);
    }

    public Ingredient(String name, String amount) {
        setName(name);
        setAmount(amount);
        setChecked(true);
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return menge;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public void setAmount(String desc) {
        this.menge = desc;
    }

    public void setChecked(boolean stroked) {
        this.checked = stroked;
    }

    @Override
    public String toString() {
        return name + "\t" + menge;
    }
}
