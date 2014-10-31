package de.anycook.einkaufszettel.store;

/**
* @author Jan Graßegger<jan@anycook.de>
*/
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String name) {
        super(String.format("Item %s does not exist", name));
    }
}
