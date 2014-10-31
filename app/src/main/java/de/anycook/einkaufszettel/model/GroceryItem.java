package de.anycook.einkaufszettel.model;

/**
 * anycook-api json response object ingredient
 *
 * @author Jan Graßegger<jan@anycook.de>
 */
public class GroceryItem {

    private String name;
    private String amount;
    private boolean stroked;

    public GroceryItem() {
        this.setName("");
        this.setAmount("");
        this.setStroked(false);
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isStroked() {
        return stroked;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public void setAmount(String desc) {
        this.amount = desc;
    }

    public void setStroked(boolean stroked) {
        this.stroked = stroked;
    }

    @Override
    public String toString() {
        return name + "\t" + amount;
    }
}