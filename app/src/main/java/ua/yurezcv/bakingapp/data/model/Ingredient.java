package ua.yurezcv.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("ingredient")
    private String name;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("measure")
    private String measure;

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String formatForWidget() {
        return String.valueOf(getName().charAt(0)).toUpperCase()
                + getName().substring(1)
                + ": " + getQuantity() + " " + getMeasure();
    }

    public String convertToSingleString() {
        return formatForWidget() + "\n";
    }

    @Override
    public String toString() {
        return String.valueOf(getName().charAt(0)).toUpperCase()
                + getName().substring(1)
                + ": " + getQuantity() + " " + getMeasure() + "\n";
    }
}
