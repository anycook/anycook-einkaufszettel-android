package de.anycook.app.model;

/**
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeResponse {
    private String name;
    protected String description;
    protected RecipeImage image;
    private long id;

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

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }


    private static class RecipeImage {
        protected String small;

        public String getSmallImage() {
            return this.small;
        }

    }


}


