package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;

public class DailySalesObject implements Serializable {
    private String TAG = DailySalesObject.class.getSimpleName();

    private long id;
    private String sellDate;
    private String sellAll;
    private String sellReal;
    private String sellFood;
    private String sellFoodPercent;
    private String sellFoodProduct;
    private String sellBeer;
    private String sellBeerPercent;
    private String sellBeerProduct;
    private String sellCock;
    private String sellCockPercent;
    private String sellCockProduct;
    private String sellLiquor;
    private String sellLiquorPercent;
    private String sellLiquorProduct;
    private String sellDrink;
    private String sellDrinkPercent;
    private String sellDrinkProduct;
    private String sellLunch;
    private String sellLunchPercent;

    public DailySalesObject(){}

    public DailySalesObject(long id, String sellDate, String sellAll, String sellReal,
                            String sellFood, String sellFoodPercent, String sellFoodProduct,
                            String sellBeer, String sellBeerPercent, String sellBeerProduct,
                            String sellCock, String sellCockPercent, String sellCockProduct,
                            String sellLiquor, String sellLiquorPercent, String sellLiquorProduct,
                            String sellDrink, String sellDrinkPercent, String sellDrinkProduct,
                            String sellLunch, String sellLunchPercent) {
        this.id = id;
        this.sellDate = sellDate;
        this.sellAll = sellAll;
        this.sellReal = sellReal;
        this.sellFood = sellFood;
        this.sellFoodPercent = sellFoodPercent;
        this.sellFoodProduct = sellFoodProduct;
        this.sellBeer = sellBeer;
        this.sellBeerPercent = sellBeerPercent;
        this.sellBeerProduct = sellBeerProduct;
        this.sellCock = sellCock;
        this.sellCockPercent = sellCockPercent;
        this.sellCockProduct = sellCockProduct;
        this.sellLiquor = sellLiquor;
        this.sellLiquorPercent = sellLiquorPercent;
        this.sellLiquorProduct = sellLiquorProduct;
        this.sellDrink = sellDrink;
        this.sellDrinkPercent = sellDrinkPercent;
        this.sellDrinkProduct = sellDrinkProduct;
        this.sellLunch = sellLunch;
        this.sellLunchPercent = sellLunchPercent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getSellAll() {
        return sellAll;
    }

    public void setSellAll(String sellAll) {
        this.sellAll = sellAll;
    }

    public String getSellReal() {
        return sellReal;
    }

    public void setSellReal(String sellReal) {
        this.sellReal = sellReal;
    }

    public String getSellFood() {
        return sellFood;
    }

    public void setSellFood(String sellFood) {
        this.sellFood = sellFood;
    }

    public String getSellFoodPercent() {
        return sellFoodPercent;
    }

    public void setSellFoodPercent(String sellFoodPercent) {
        this.sellFoodPercent = sellFoodPercent;
    }

    public String getSellFoodProduct() {
        return sellFoodProduct;
    }

    public void setSellFoodProduct(String sellFoodProduct) {
        this.sellFoodProduct = sellFoodProduct;
    }

    public String getSellBeer() {
        return sellBeer;
    }

    public void setSellBeer(String sellBeer) {
        this.sellBeer = sellBeer;
    }

    public String getSellBeerPercent() {
        return sellBeerPercent;
    }

    public void setSellBeerPercent(String sellBeerPercent) {
        this.sellBeerPercent = sellBeerPercent;
    }

    public String getSellBeerProduct() {
        return sellBeerProduct;
    }

    public void setSellBeerProduct(String sellBeerProduct) {
        this.sellBeerProduct = sellBeerProduct;
    }

    public String getSellCock() {
        return sellCock;
    }

    public void setSellCock(String sellCock) {
        this.sellCock = sellCock;
    }

    public String getSellCockPercent() {
        return sellCockPercent;
    }

    public void setSellCockPercent(String sellCockPercent) {
        this.sellCockPercent = sellCockPercent;
    }

    public String getSellCockProduct() {
        return sellCockProduct;
    }

    public void setSellCockProduct(String sellCockProduct) {
        this.sellCockProduct = sellCockProduct;
    }

    public String getSellLiquor() {
        return sellLiquor;
    }

    public void setSellLiquor(String sellLiquor) {
        this.sellLiquor = sellLiquor;
    }

    public String getSellLiquorPercent() {
        return sellLiquorPercent;
    }

    public void setSellLiquorPercent(String sellLiquorPercent) {
        this.sellLiquorPercent = sellLiquorPercent;
    }

    public String getSellLiquorProduct() {
        return sellLiquorProduct;
    }

    public void setSellLiquorProduct(String sellLiquorProduct) {
        this.sellLiquorProduct = sellLiquorProduct;
    }

    public String getSellDrink() {
        return sellDrink;
    }

    public void setSellDrink(String sellDrink) {
        this.sellDrink = sellDrink;
    }

    public String getSellDrinkPercent() {
        return sellDrinkPercent;
    }

    public void setSellDrinkPercent(String sellDrinkPercent) {
        this.sellDrinkPercent = sellDrinkPercent;
    }

    public String getSellDrinkProduct() {
        return sellDrinkProduct;
    }

    public void setSellDrinkProduct(String sellDrinkProduct) {
        this.sellDrinkProduct = sellDrinkProduct;
    }

    public String getSellLunch() {
        return sellLunch;
    }

    public void setSellLunch(String sellLunch) {
        this.sellLunch = sellLunch;
    }

    public String getSellLunchPercent() {
        return sellLunchPercent;
    }

    public void setSellLunchPercent(String sellLunchPercent) {
        this.sellLunchPercent = sellLunchPercent;
    }

    @Override
    public String toString() {
        return "DailySalesObject{" +
                "id=" + id +
                ", sellDate='" + sellDate + '\'' +
                ", sellAll='" + sellAll + '\'' +
                ", sellReal='" + sellReal + '\'' +
                ", sellFood='" + sellFood + '\'' +
                ", sellFoodPercent='" + sellFoodPercent + '\'' +
                ", sellFoodProduct='" + sellFoodProduct + '\'' +
                ", sellBeer='" + sellBeer + '\'' +
                ", sellBeerPercent='" + sellBeerPercent + '\'' +
                ", sellBeerProduct='" + sellBeerProduct + '\'' +
                ", sellCock='" + sellCock + '\'' +
                ", sellCockPercent='" + sellCockPercent + '\'' +
                ", sellCockProduct='" + sellCockProduct + '\'' +
                ", sellLiquor='" + sellLiquor + '\'' +
                ", sellLiquorPercent='" + sellLiquorPercent + '\'' +
                ", sellLiquorProduct='" + sellLiquorProduct + '\'' +
                ", sellDrink='" + sellDrink + '\'' +
                ", sellDrinkPercent='" + sellDrinkPercent + '\'' +
                ", sellDrinkProduct='" + sellDrinkProduct + '\'' +
                ", sellLunch='" + sellLunch + '\'' +
                ", sellLunchPercent='" + sellLunchPercent + '\'' +
                '}';
    }
}
