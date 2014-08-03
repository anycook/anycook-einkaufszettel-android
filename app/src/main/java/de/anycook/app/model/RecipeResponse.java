package de.anycook.app.model;

/**
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeResponse {
    private String name;
    private String description;
    private RecipeImage image;
    private int persons;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return this.image.getSmallImage();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(RecipeImage image) {
        this.image = image;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return name;
    }

    private static class RecipeImage {
        protected String small;

        public String getSmallImage() {
            return this.small;
        }

    }

}


