package de.anycook.app.model;

public class GroceryItem {

    private String name;
    private String menge;
    private boolean stroked;
    private long id;

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
        return menge;
    }

    public boolean isStroked() {
        return stroked;
    }

    public long getId() {
        return id;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public void setAmount(String desc) {
        this.menge = desc;
    }

    public void setStroked(boolean stroked) {
        this.stroked = stroked;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + "\t" + menge;
    }
}