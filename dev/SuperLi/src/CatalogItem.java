package SuperLi.src;

import SuperLi.src.Stock.CostumerDiscount;
import java.util.*;
public class CatalogItem {
    int id;
    String name;
    Category category;
    String manufacturer;
    double sellPrice;
    int minCapacity;
    int shelvesAmount;
    int backAmount;
    int shelvesLocation;
    int backLocation;
    CostumerDiscount costumerDiscount;
    public CatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, Category category, int shelvesLocation, int backLocation){
        this.id = id;
        this.shelvesAmount = 0;
        this.backAmount = 0;
        this.name = name;
        this.manufacturer = manufacturer;
        this.sellPrice = sellPrice;
        this.minCapacity = minCapacity;
        this.category = category;
        this.shelvesLocation = shelvesLocation;
        this.backLocation = backLocation;
        this.costumerDiscount = null;
    }
    public int getId(){ return this.id;}
    public String getName(){return this.name;}
    public String getManufacturer(){return this.manufacturer;}
    public Category getCategory(){return this.category;}
    public double getPrice(){return this.sellPrice;}
    public void setPrice(double price){this.sellPrice = price;}
    public int getMinCapacity() { return this.minCapacity;}
    public void setMinCapacity(int capacity){this.minCapacity = capacity;}
    public int getTotalAmount(){return this.shelvesAmount + this.backAmount;}
    public int getShelvesAmount(){ return this.shelvesAmount;}
    public void setShelvesAmount(int amount){this.shelvesAmount = amount;}
    public void incShelves(){setShelvesAmount(this.shelvesAmount + 1);}
    public void decShelves(){setShelvesAmount(this.shelvesAmount - 1);}
    public int getBackAmount(){ return this.backAmount;}
    public void setBackAmount(int amount){this.backAmount = amount;}
    public void incBack(){setBackAmount(this.backAmount + 1);}
    public void decBack(){setBackAmount(this.backAmount - 1);}
    public int getShelvesLocation(){return this.shelvesLocation;}
    public int getBackLocation(){return this.backLocation;}
    public String toString(){
        return String.format("ID: %d; %s; %s; Manufacturer: %s; %.1f₪; ; Min Capacity: %d; CostumerDiscount: %s",
                id, category, name, manufacturer, sellPrice, minCapacity,(costumerDiscount == null ? "" : costumerDiscount.toString()));
    }
    public CostumerDiscount getCostumerDiscount(){return this.costumerDiscount;}
    public void setCostumerDiscount(CostumerDiscount costumerDiscount){this.costumerDiscount = costumerDiscount;}
    public double getDiscountedPrice() {
        if(this.costumerDiscount == null)
            return this.sellPrice;
        return getDiscountedPrice(this.costumerDiscount.getMinCapacity());
    }
    public double getDiscountedPrice(int amount) {
        if(costumerDiscount == null)
            return this.sellPrice;
        return this.costumerDiscount.generateDiscount(this.sellPrice, amount);
    }
    public boolean isFromCategory(Category category){return category.equals(this.getCategory());}
    public boolean isFromCategory(ArrayList<String> categoriesStrList){return category.getCategories().containsAll(categoriesStrList);}
}