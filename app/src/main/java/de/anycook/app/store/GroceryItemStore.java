package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.util.StringTools;
import de.anycook.app.model.GroceryItem;
import de.anycook.app.model.Ingredient;

import java.io.Closeable;
import java.util.List;

/**
 *
 *
 * @author Jan Graßegger<jan@anycook.de>
 */
public class GroceryItemStore implements Closeable {
    private static final Logger LOGGER = LoggerManager.getLogger();
    private final Context context;
    private SQLiteDatabase database;

    public GroceryItemStore(Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.d("Open database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.d("Close database");
        database.close();
    }

    public void addGroceryItem(String name, String amount) {
        IngredientNameStore ingredientNameStore = new IngredientNameStore(this.context);
        try {
            ingredientNameStore.open();
            if (!ingredientNameStore.ingredientExists(name)) {
                ingredientNameStore.addIngredient(new Ingredient(name, amount));
            } else {
                try {
                    GroceryItem oldGroceryItem = getGroceryItem(name);
                    if (!oldGroceryItem.isStroked()) {
                        amount = StringTools.mergeAmounts(amount, oldGroceryItem.getAmount());
                    }
                } catch (ItemNotFoundException e) {
                    LOGGER.d("Added new Ingredient %s %s.", name, amount);
                }
            }
        } finally {
            ingredientNameStore.close();
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("amount", amount);
        values.put("orderId", getMinOrderId() - 1);

        database.insertWithOnConflict(SQLiteDB.GROCERY_ITEM_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public GroceryItem getGroceryItem(String name) throws ItemNotFoundException {
        String[] columns = new String[]{"name", "amount", "stroke"};
        Cursor cursor = database.query(SQLiteDB.GROCERY_ITEM_TABLE, columns, "name=?", new String[]{name},
                null, null, null);
        if (cursor.moveToNext()) {
            GroceryItem item = new GroceryItem();
            item.setName(cursor.getString(SQLiteDB.TableFields.GROCERY_ITEM_NAME));
            item.setAmount(cursor.getString(SQLiteDB.TableFields.GROCERY_ITEM_AMOUNT));
            item.setStroked(cursor.getInt(SQLiteDB.TableFields.GROCERY_ITEM_STROKE) > 0);
            return item;
        }

        throw new ItemNotFoundException(name);
    }

    public void changeStrokeVisibilityOfGroceryItem(CharSequence groceryItemName) {
        ContentValues values = new ContentValues();
        try {
            values.put("stroke", !getGroceryItem((String) groceryItemName).isStroked());
        } catch (ItemNotFoundException e) {
            LOGGER.d(e, "User wanted to stroke \"%s\" but it's not in the database.", groceryItemName);
        }

        database.update(SQLiteDB.GROCERY_ITEM_TABLE, values,
                String.format("name=\"%s\"", groceryItemName), null);
    }

    public void removeGroceryItem(String groceryItemName) {
        database.delete(SQLiteDB.GROCERY_ITEM_TABLE, "name=?", new String[]{groceryItemName});
    }

    public Cursor getStrokedGroceryItems() {
        return database.query(SQLiteDB.GROCERY_ITEM_TABLE, new String[]{"name"}, "stroke=1", null, null, null, null);
    }

    public Cursor getAllGroceryItemsCursor() {
        String query = String.format("SELECT name AS _id, amount, stroke FROM %s ORDER BY orderId",
                SQLiteDB.GROCERY_ITEM_TABLE);
        return database.rawQuery(query, null);
    }

    public void deleteAllGroceryItems() {
        database.delete(SQLiteDB.GROCERY_ITEM_TABLE, null, null);
    }

    private int getMinOrderId() {
        Cursor cursor = database.query(true, SQLiteDB.GROCERY_ITEM_TABLE, new String[]{"MIN(orderId)"}, null ,
                null, null, null, null, "1");
        if (cursor.getCount() == 0) { return 100000; }
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public void addIngredientsToGroceryList(List<Ingredient> ingredients) {
        int orderId = getMinOrderId() - ingredients.size();
        for (Ingredient ingredient : ingredients) {
            try {
                GroceryItem oldGroceryItem = getGroceryItem(ingredient.getName());
                ingredient.setAmount(StringTools.mergeAmounts(ingredient.getAmount(), oldGroceryItem.getAmount()));
            } catch (ItemNotFoundException e) {
                LOGGER.d("%s is a new ingredient", ingredient.getName());
            }

            ContentValues values = new ContentValues();
            values.put("name", ingredient.getName());
            values.put("amount", ingredient.getAmount());
            values.put("orderId", orderId++);
            database.insertWithOnConflict(SQLiteDB.GROCERY_ITEM_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

}
