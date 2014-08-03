package de.anycook.app.model;

import de.anycook.app.activities.util.StringTools;

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

    public Ingredient(Ingredient ingredient) {
        this.name = ingredient.name;
        this.menge = ingredient.menge;
        this.checked = ingredient.checked;
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

    public void multiplyAmount(int recipePersons, int newPersons) {
        if (recipePersons == newPersons) return;
        menge = StringTools.multiplyAmount(menge, recipePersons, newPersons);
    }

    @Override
    public String toString() {
        return name + "\t" + menge;
    }
}
