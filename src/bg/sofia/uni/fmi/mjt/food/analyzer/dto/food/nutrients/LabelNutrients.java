package bg.sofia.uni.fmi.mjt.food.analyzer.dto.food.nutrients;

public class LabelNutrients {
    private Nutrient fat;
    private Nutrient carbohydrates;
    private Nutrient fiber;
    private Nutrient protein;
    private Nutrient calories;

    public LabelNutrients(Nutrient fat, Nutrient carbohydrates, Nutrient fiber, Nutrient protein, Nutrient calories) {
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.protein = protein;
        this.calories = calories;
    }

    public Nutrient getFat() {
        // some products don't have nutrients
        return fat == null ? new Nutrient(0.0) : fat;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates == null ? new Nutrient(0.0) : carbohydrates;
    }

    public Nutrient getFiber() {
        return fiber == null ? new Nutrient(0.0) : fiber;
    }

    public Nutrient getProtein() {
        return protein == null ? new Nutrient(0.0) : protein;
    }

    public Nutrient getCalories() {
        return calories == null ? new Nutrient(0.0) : calories;
    }

    @Override
    public String toString() {
        return String.format("\t\t- fat: %s\n\t\t- carbohydrates: %s\n\t\t- fiber: %s\n\t\t- protein: %s\n\t\t- calories: %s\n",
                                getFat(),getCarbohydrates(),getFiber(),getProtein(),getCalories());
    }
}

/*
"labelNutrients":{
    "fat":{"value":15.0},
    "saturatedFat":{"value":9.00},
    "transFat":{"value":0.000},
    "cholesterol":{"value":5.10},
    "sodium":{"value":35.1},
    "carbohydrates":{"value":12.0},
    "fiber":{"value":0.990},
    "sugars":{"value":10.0},
    "protein":{"value":2.00},
    "calcium":{"value":39.9},
    "iron":{"value":0.360},
    "calories":{"value":190}}}
* */

/*
{
"foodComponents":[],
"foodAttributes":[],
"foodPortions":[],
"fdcId":415269,
"description":"RAFFAELLO, ALMOND COCONUT TREAT",
"publicationDate":"4/1/2019",
"foodNutrients":[
    {"type":"FoodNutrient","nutrient":{"id":1089,"number":"303","name":"Iron, Fe","rank":5400,"unitName":"mg"},"foodNutrientDerivation":{"id":75,"code":"LCCD","description":"Calculated from a daily value percentage per serving size measure"},"id":3649549,"amount":1.20000000},
    {"type":"FoodNutrient","nutrient":{"id":1162,"number":"401","name":"Vitamin C, total ascorbic acid","rank":6300,"unitName":"mg"},"foodNutrientDerivation":{"id":75,"code":"LCCD","description":"Calculated from a daily value percentage per serving size measure"},"id":3649551,"amount":0E-8},
    {"type":"FoodNutrient","nutrient":{"id":1104,"number":"318","name":"Vitamin A, IU","rank":7500,"unitName":"IU"},"foodNutrientDerivation":{"id":75,"code":"LCCD","description":"Calculated from a daily value percentage per serving size measure"},"id":3649550,"amount":0E-8},
    {"type":"FoodNutrient","nutrient":{"id":1087,"number":"301","name":"Calcium, Ca","rank":5300,"unitName":"mg"},"foodNutrientDerivation":{"id":75,"code":"LCCD","description":"Calculated from a daily value percentage per serving size measure"},"id":3649548,"amount":133.00000000},
    {"type":"FoodNutrient","nutrient":{"id":1003,"number":"203","name":"Protein","rank":600,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348026,"amount":6.67000000},
    {"type":"FoodNutrient","nutrient":{"id":1004,"number":"204","name":"Total lipid (fat)","rank":800,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348027,"amount":50.00000000},
    {"type":"FoodNutrient","nutrient":{"id":2000,"number":"269","name":"Sugars, total including NLEA","rank":1510,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348030,"amount":33.33000000},
    {"type":"FoodNutrient","nutrient":{"id":1093,"number":"307","name":"Sodium, Na","rank":5800,"unitName":"mg"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348032,"amount":117.00000000},
    {"type":"FoodNutrient","nutrient":{"id":1258,"number":"606","name":"Fatty acids, total saturated","rank":9700,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348034,"amount":30.00000000},
    {"type":"FoodNutrient","nutrient":{"id":1079,"number":"291","name":"Fiber, total dietary","rank":1200,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348031,"amount":3.30000000},
    {"type":"FoodNutrient","nutrient":{"id":1008,"number":"208","name":"Energy","rank":300,"unitName":"kcal"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348029,"amount":633.00000000},
    {"type":"FoodNutrient","nutrient":{"id":1257,"number":"605","name":"Fatty acids, total trans","rank":15400,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348033,"amount":0E-8},
    {"type":"FoodNutrient","nutrient":{"id":1005,"number":"205","name":"Carbohydrate, by difference","rank":1110,"unitName":"g"},"foodNutrientDerivation":{"id":70,"code":"LCCS","description":"Calculated from value per serving size measure"},"id":5348028,"amount":40.00000000},
    {"type":"FoodNutrient","nutrient":{"id":1253,"number":"601","name":"Cholesterol","rank":15700,"unitName":"mg"},"foodNutrientDerivation":{"id":73,"code":"LCSL","description":"Calculated from a less than value per serving size measure"},"id":6391876,"amount":17.00000000}],
"dataType":"Branded",
"foodClass":"Branded",
"modifiedDate":"7/14/2017",
"availableDate":"7/14/2017",
"brandOwner":"Ferrero U.S.A., Incorporated",
"dataSource":"LI",
"brandedFoodCategory":"Chocolate",
"gtinUpc":"009800146130",
"householdServingFullText":"3 PIECES",
"ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.",
"marketCountry":"United States",
"servingSize":30.00000000,
"servingSizeUnit":"g",
"foodUpdateLog":[
    {"foodAttributes":[],"fdcId":2041155,"description":"RAFFAELLO, ALMOND COCONUT TREAT","publicationDate":"10/28/2021","dataType":"Branded","foodClass":"Branded","modifiedDate":"7/14/2017","availableDate":"7/14/2017","brandOwner":"Ferrero U.S.A., Incorporated","brandName":"RAFFAELLO","dataSource":"LI","brandedFoodCategory":"Chocolate","gtinUpc":"009800146130","ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.","marketCountry":"United States","servingSize":30.00000000,"servingSizeUnit":"g","packageWeight":"2.4 oz/70 g"},
    {"foodAttributes":[],"fdcId":1515707,"description":"RAFFAELLO, ALMOND COCONUT TREAT","publicationDate":"3/19/2021","dataType":"Branded","foodClass":"Branded","modifiedDate":"7/14/2017","availableDate":"7/14/2017","brandOwner":"Ferrero U.S.A., Incorporated","brandName":"RAFFAELLO","dataSource":"LI","brandedFoodCategory":"Chocolate","gtinUpc":"009800146130","ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.","marketCountry":"United States","servingSize":30.00000000,"servingSizeUnit":"g"},
    {"foodAttributes":[],"fdcId":1223648,"description":"RAFFAELLO, ALMOND COCONUT TREAT","publicationDate":"2/26/2021","dataType":"Branded","foodClass":"Branded","modifiedDate":"7/14/2017","availableDate":"7/14/2017","brandOwner":"Ferrero U.S.A., Incorporated","brandName":"RAFFAELLO","subbrandName":"","dataSource":"LI","brandedFoodCategory":"Chocolate","gtinUpc":"009800146130","ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.","marketCountry":"United States","servingSize":30.00000000,"servingSizeUnit":"g","packageWeight":"","notaSignificantSourceOf":""},
    {"foodAttributes":[],"fdcId":415269,"description":"RAFFAELLO, ALMOND COCONUT TREAT","publicationDate":"4/1/2019","dataType":"Branded","foodClass":"Branded","modifiedDate":"7/14/2017","availableDate":"7/14/2017","brandOwner":"Ferrero U.S.A., Incorporated","dataSource":"LI","brandedFoodCategory":"Chocolate","gtinUpc":"009800146130","householdServingFullText":"3 PIECES","ingredients":"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.","marketCountry":"United States","servingSize":30.00000000,"servingSizeUnit":"g"}
 ],
 "labelNutrients":{
    "fat":{"value":15.0},
    "saturatedFat":{"value":9.00},
    "transFat":{"value":0.000},
    "cholesterol":{"value":5.10},
    "sodium":{"value":35.1},
    "carbohydrates":{"value":12.0},
    "fiber":{"value":0.990},
    "sugars":{"value":10.0},
    "protein":{"value":2.00},
    "calcium":{"value":39.9},
    "iron":{"value":0.360},
    "calories":{"value":190}}}


* * */