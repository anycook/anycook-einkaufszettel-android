package de.anycook.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.model.Ingredient;
import de.anycook.app.store.GroceryItemStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class IngredientListRowAdapter extends ArrayAdapter<Ingredient>{

    public IngredientListRowAdapter(Context context) {
        super(context, R.layout.ingredient_list_row, new ArrayList<Ingredient>());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IngredientHolder holder;
        if(convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingredient_list_row, parent, false);
            holder = new IngredientHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.ingredient_list_row_name);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.ingredient_list_row_amount);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.ingredient_list_row_checkbox);
            convertView.setTag(holder);
        }
        else {
            holder = (IngredientHolder) convertView.getTag();
        }
        Ingredient ingredient = getItem(position);
        holder.nameTextView.setText(ingredient.name);
        holder.amountTextView.setText(ingredient.menge);
        holder.checkBox.setChecked(ingredient.checked);

        return convertView;
    }

    public void saveChecked() {
        GroceryItemStore groceryItemStore = new GroceryItemStore(getContext());
        try {
            List<Ingredient> ingredients = new ArrayList<>(getCount());
            for (int i = 0; i < getCount(); i++) {
                Ingredient ingredient = getItem(i);
                if(!ingredient.checked) continue;
                ingredients.add(ingredient);
            }
            groceryItemStore.addIngredientsToGroceryList(ingredients);
        } finally {
            groceryItemStore.close();
        }
    }

    private static class IngredientHolder {
        TextView nameTextView;
        TextView amountTextView;
        CheckBox checkBox;
    }
}
