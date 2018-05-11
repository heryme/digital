package com.digitalscale.model;

import com.digitalscale.tools.Constant;

/**
 * Created by Vishal Gadhiya on 3/30/2017.
 */

public class Food {
    private String status;
    private String message;

    private String id;
    private String foodName;
    private String description;
    private String quantity = "0";
    private String calorie;
    private String fat;
    private String alcohol;
    private String fiber;
    private String ash;
    private String water;
    private String protein;
    private String carbohydrate;
    private String weightReading ="0";
    private String weightReadingUnit = Constant.UNIT_GM;

    private String foodKcal;
    private String foodQty;
    private String foodUnit;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getAsh() {
        return ash;
    }

    public void setAsh(String ash) {
        this.ash = ash;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getWeightReading() {
        return weightReading;
    }

    public void setWeightReading(String weightReading) {
        this.weightReading = weightReading;
    }

    public String getWeightReadingUnit() {
        return weightReadingUnit;
    }

    public void setWeightReadingUnit(String weightReadingUnit) {
        this.weightReadingUnit = weightReadingUnit;
    }

    public String getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(String foodKcal) {
        this.foodKcal = foodKcal;
    }

    public String getFoodQty() {
        return foodQty;
    }

    public void setFoodQty(String foodQty) {
        this.foodQty = foodQty;
    }

    public String getFoodUnit() {
        return foodUnit;
    }

    public void setFoodUnit(String foodUnit) {
        if (foodUnit.equals("gram"))
            this.foodUnit = Constant.UNIT_GM;
        else
            this.foodUnit = foodUnit;
    }
}
