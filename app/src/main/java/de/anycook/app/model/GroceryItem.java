package de.anycook.app.model;

public class GroceryItem {

    private String name;
    private String amount;
    private boolean stroked;

    public GroceryItem(String name, String amount, boolean stroked) {
        this.setName(name);
        this.setAmount(amount);
        this.setStroked(stroked);
    }

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