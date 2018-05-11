package com.digitalscale.utility;

import android.util.Log;

import com.digitalscale.constant.Constant;

import java.text.DecimalFormat;

/**
 * Created by Rahul Padaliya on 6/1/2017.
 */
public class FormulaUtility {

    private static final String TAG = FormulaUtility.class.getSimpleName();

    /**
     * Convert Into Pound,Kg,Oz(Ounce)
     * @param val
     * @param unitType
     * @return
     */
    public static String convertIntoGram(String val,String unitType){

        Double value = Double.valueOf(val);
        if(unitType.equalsIgnoreCase(Constant.LB)){
            // convert pound into gram as 1 pound = 453.592 gram
            value = value * 453.592;
            Log.d(TAG,"Pound Value--->" + value);
            String temp = new DecimalFormat("##.##").format(value);
            Log.d(TAG,"Temp---->" + temp);
            return temp;
        }else if(unitType.equalsIgnoreCase(Constant.KG)) {
            // convert KG into gram as 1 kg = 1000 gram
            value = value * 1000;
            Log.d(TAG,"KG Value--->" + value);
            String temp = new DecimalFormat("##.##").format(value);
            Log.d(TAG,"Temp---->" + temp);
            return temp;
        }else if(unitType.equalsIgnoreCase(Constant.OZ)) {
            // convert Oz(Ounce) into gram as 1 gm = 0.035274 Oz(Ounce)
            value = value * 0.035274;
            Log.d(TAG,"OZ Value--->" + value);
            String temp = new DecimalFormat("##.##").format(value);
            Log.d(TAG,"Temp---->" + temp);
            return temp;
        }
        return "";
    }

    /**
     * Calculate Calories
     * @param food
     * @param calculatedQty
     * @param perQty
     * @return
     */
    public static String calculateCalories(String food,String calculatedQty,String perQty){

        Log.d(TAG, "food >> " + food + "  calculatedQty >> " + calculatedQty + "  perQty >> " + perQty);
        Double mFood = Double.valueOf(food);
        Double mCalculatedQty = Double.valueOf(calculatedQty);
        Double mPerQty = Double.valueOf(perQty);

        Double result = (mFood * mCalculatedQty)/mPerQty;

        String temp = String.valueOf(Math.round(result));
        //String temp = new DecimalFormat("##.##").format(Math.round(result));
        Log.d(TAG,"Calories Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Oz(Ounce) To Gm
     * @param val
     * @return Gm
     */
    public static String gmToOz(String val){
        Double value = Double.valueOf(val);
        //Oz To Gm 1 Oz = 0.035274 Gm
        value = value * 0.035274;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Oz(Ounce) To Gm  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Kg To Gram
     * @param val
     * @return Gram
     */
    public static String kGToGram(String val) {
        Double value = Double.valueOf(val);
        //Kg To Gm 1 Kg = 1000 Gm
        value = value * 1000;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Kg To Gm  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Pound To Gm
     * @param val
     * @return Gm
     */
    public static String lbToGm(String val) {
        Double value = Double.valueOf(val);
        //Pound To Gm 1 Pound = 453.592 Gm
        value = value * 453.592;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Pound To Gm  Temp---->" + temp);
        return temp;
    }
    /**
     * Convert Gm To Pound
     * @param val
     * @return Pound(lb)
     */
    public static String gmToLb(String val){

        Double value = Double.valueOf(val);
        //Gm To Pound 1GM = 0.00220462 Pound(Lb)
        value = value * 0.00220462;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"GM To Pound  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert OZ(Ounce) To Gm
     * @param val
     * @return
     */
    public static String oZToGm(String val){
        Double value = Double.valueOf(val);
        //OZ(Ounce) To GM 1 oZ = 28.3495 gm
        value = value * 28.3495;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"oZ To GM  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Gm To ML
     * @param val
     * @return ML
     */
    public static String gmToMl(String val){
        return val;
    }

    /**
     * Convert ML TO Gm
     * @param val
     * @return GM
     */
    public static String mlToGm(String val) {
        return val;
    }

    /**
     * Convert Gm To KG
     * @param val
     * @return KG
     */
    public static String gmToKg(String val) {
        Double value = Double.valueOf(val);
        //GM To KG 1 GM = 0.001 KG
        value = value * 0.001;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"GM To KG  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Pound To ML
     * @param val
     * @return ML
     */
    public static String lbToMl(String val) {
        Double value = Double.valueOf(val);
        //Pound(lb) To ML 1 Pound  = 453.59 Ml
        value = value * 453.59 ;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Pound To ML  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Ml To Pound
     * @param val
     * @return Pound
     */
    public static String mlToLb(String val) {
        Double value = Double.valueOf(val);
        //Pound(lb) To ML 1 ML  = 0.0022 Pound(Lb)
        value = value * 0.0022;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Ml To Pound(lb)  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Pound To Oz
     * @param val
     * @return Oz
     */
    public  static String lbToOz(String val) {
        Double value = Double.valueOf(val);
        //Pound(lb) To Oz 1 Pound = 16 Oz
        value = value * 16;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Pound To Oz  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Oz(Ounce) To Pound(lb)
     * @param val
     * @return Pound(lb)
     */
    public static String oZToLb(String val) {
        Double value = Double.valueOf(val);
        //Oz(Ounce) To Pound(Lb) 1 OZ = 0.0625 Pound
        value = value * 0.0625;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Oz(Ounce) To Pound(lb)  Temp---->" + temp);
        return temp;
    }

    /**
     * oZ To ML
     * @param val
     * @return ML
     */
    public static String oZToMl(String val){
        Double value = Double.valueOf(val);
        //Oz(Ounce) To ML 1 OZ = 29.5735296875 ML
        value = value * 29.5735296875;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG,"Oz(Ounce) To ML  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert ML TO Oz(Ounce)
     * @param val
     * @return  Oz(Ounce)
     */
    public static String mLToOz(String val) {
        Double value = Double.valueOf(val);
        //ML To OZ 1 OZ = 0.033814 Oz(Ounce)
        value = value * 0.033814;
        String temp = new DecimalFormat("##.##").format(value);
        Log.d(TAG," ML To Oz  Temp---->" + temp);
        return temp;
    }

    /**
     * Convert Old Unit To New Unit
     * @param oldUnit
     * @param newUnit
     * @param value
     * @return
     */
    public static String convertOldToNewUnit(String oldUnit,String newUnit,String value) {

        if(oldUnit.equals(newUnit))
            return value;

        if(oldUnit.equals(Constant.GM) && newUnit.equals(Constant.ML)) {
            return gmToMl(value);
        }

        if(oldUnit.equals(Constant.GM) && newUnit.equals(Constant.OZ)){
            return gmToOz(value);
        }

        if(oldUnit.equals(Constant.GM) && newUnit.equals(Constant.LB)){
            return gmToLb(value);
        }

        if(oldUnit.equals(Constant.GM) && newUnit.equals(Constant.KG)){
            return gmToKg(value);
        }

        if(oldUnit.equals(Constant.ML) && newUnit.equals(Constant.GM)){
            return mlToGm(value);
        }

        if(oldUnit.equals(Constant.ML) && newUnit.equals(Constant.OZ)){
            return mLToOz(value);
        }

        if(oldUnit.equals(Constant.ML) && newUnit.equals(Constant.LB)){
            return mlToLb(value);
        }

        if(oldUnit.equals(Constant.OZ) && newUnit.equals(Constant.GM)){
            return oZToGm(value);
        }

        if(oldUnit.equals(Constant.OZ) && newUnit.equals(Constant.ML)){
            return oZToMl(value);
        }

        if(oldUnit.equals(Constant.OZ) && newUnit.equals(Constant.LB)){
            return oZToLb(value);
        }

        if(oldUnit.equals(Constant.LB) && newUnit.equals(Constant.GM)){
            return lbToGm(value);
        }

        if(oldUnit.equals(Constant.LB) && newUnit.equals(Constant.ML)){
            return lbToMl(value);
        }

        if(oldUnit.equals(Constant.LB) && newUnit.equals(Constant.OZ)){
            return lbToOz(value);
        }
        return "";
    }
}
