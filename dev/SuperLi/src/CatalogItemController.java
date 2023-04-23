package SuperLi.src;

import SuperLi.src.Stock.CostumerDiscount;

import java.util.ArrayList;

public class CatalogItemController {
    static CatalogItemController instance = null;
    CatalogItem catalogItem;
    // Singleton Design:
    private CatalogItemController(CatalogItem catalogItem){
        this.catalogItem = catalogItem;
    }
    // Changing the catalog item it controls
    private void setCatalogItem(CatalogItem catalogItem){
        this.catalogItem = catalogItem;
    }
    public static CatalogItemController getInstance(CatalogItem catalogItem){
        if(instance == null)
            instance = new CatalogItemController(catalogItem);
        else
            instance.setCatalogItem(catalogItem);
        return instance;
    }
    // Catalog Item's values
    public int getId(){ return catalogItem.getId();}
    public String getName(){ return catalogItem.getName();}
    public String getManufacturer(){return catalogItem.getManufacturer();}
    public Category tCategory(){return catalogItem.getCategory();}
    public double getSellPrice() {return catalogItem.getSellPrice();}
    public void setSellPrice(double price) {catalogItem.setSellPrice(price);}
    public int getMinCapacity() {return catalogItem.getMinCapacity();}
    public void setMinCapacity(int capacity) {catalogItem.setMinCapacity(capacity);}
    public int getShelvesLocation() {return catalogItem.getShelvesLocation();}
    public int getBackLocation() {return catalogItem.getBackLocation();}
    public CostumerDiscount getCostumerDiscount() {return catalogItem.getCostumerDiscount();}
    public void setCostumerDiscount(CostumerDiscount discount) {catalogItem.setCostumerDiscount(discount);}
    public double getDiscountedPrice(int amount) {return catalogItem.getDiscountedPrice(amount);}
    public double getDiscountedPrice() {return catalogItem.getDiscountedPrice();}
    public boolean isFromCategory(Category category) {return catalogItem.isFromCategory(category);}
    public boolean isFromCategory(ArrayList<String> categoriesStrList) {return catalogItem.isFromCategory(categoriesStrList);}

    /**
     * Return branches amounts of an item in specific branch
     * @param branchId the branch where we want to get the amounts in
     * @return Pair of the amounts <shelvesAmount, backAmount>
     */
    public Pair<Integer, Integer> getBranchAmounts(int branchId){
        //TO DO - Aggregate from Database
        int shelves = 0; int back = 0;
        return new Pair<>(shelves, back);
    }
    public String toString() {return catalogItem.toString();}
}