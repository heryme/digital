package com.digitalscale.model;

import com.digitalscale.tools.Constant;

/**
 * Created by Vishal Gadhiya on 4/3/2017.
 */

public class FoodHistory {

    private String status;
    private String message;
    private String foodId;
    private String foodHistoryId;
    private String foodName;
    private String description;
    private String qty;
    private String kcal;
    private String qtyUnit;

    private String foodQty;
    private String foodKcal;
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodHistoryId() {
        return foodHistoryId;
    }

    public void setFoodHistoryId(String foodHistoryId) {
        this.foodHistoryId = foodHistoryId;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        if (qtyUnit.equalsIgnoreCase("pound"))
            this.qtyUnit = Constant.UNIT_LB;
        else
            this.qtyUnit = qtyUnit;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getFoodQty() {
        return foodQty;
    }

    public void setFoodQty(String foodQty) {
        this.foodQty = foodQty;
    }

    public String getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(String foodKcal) {
        this.foodKcal = foodKcal;
    }

    public String getFoodUnit() {
        return foodUnit;
    }

    public void setFoodUnit(String foodUnit) {
        this.foodUnit = foodUnit;
    }
}
