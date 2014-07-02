package de.anycook.app.adapter;

public class Ingredient {

    private String name;
    private String amount;
    private boolean struckOut;
    private long id;

    public Ingredient(String name, String amount) {
        this.setName(name);
        this.setAmount(amount);
        this.setStruckOut(false);
    }

    public Ingredient() {
        this.setName("");
        this.setAmount("");
        this.setStruckOut(false);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String desc) {
        this.amount = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public boolean getStruckOut() {
        return this.struckOut;
    }

    public void setStruckOut(boolean struckOut) {
        this.struckOut = struckOut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + "\t" + amount;
    }
}