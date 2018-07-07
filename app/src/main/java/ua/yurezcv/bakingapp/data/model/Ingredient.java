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

    @Override
    public String toString() {
        return String.valueOf(getName().charAt(0)).toUpperCase()
                + getName().substring(1)
                + ": " + getQuantity() + " " + getMeasure();
    }
}
