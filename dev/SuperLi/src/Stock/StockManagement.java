package SuperLi.src.Stock;

import SuperLi.src.*;

import java.util.*;

public class StockManagement {
    ArrayList<Branch> branches;
    Map<Integer, CatalogItem> catalogItemsMap;
    final static int BACKSTART = 1000;
    final static int BACKEND = 2000;
    public StockManagement(){
        branches = new ArrayList<>();
        branches.add(new Branch("Beer Sheva"));
        branches.add(new Branch("Rishon Lezion"));
        branches.add(new Branch("Netanya"));
        catalogItemsMap = new HashMap<>();
    }
    public Map<Integer, CatalogItem> getCatalogItemsMap(){return catalogItemsMap;}
    public boolean containsId(int id){return catalogItemsMap.containsKey(id);}
    public boolean addCatalogItem(int id, String name, Category category, String manufacturer, double sellPrice, int minCapacity, int shelvesLocation, int backLocation){
        if(containsId(id))
            return false;
        catalogItemsMap.put(id, new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation));
        return true;
    }
    public boolean removeCatalogItem(int id){
        return catalogItemsMap.remove(id) != null;
    }
    public double getSellPrice(int id){
        return containsId(id) ? catalogItemsMap.get(id).getSellPrice() : -1;
    }
    public boolean setSellPrice(int id, double price){
        if(!containsId(id))
            return false;
        catalogItemsMap.get(id).setSellPrice(price);
        return true;
    }
    public double getMinCapacity(int id){
        return containsId(id) ? catalogItemsMap.get(id).getMinCapacity() : -1;
    }
    public boolean setMinCapacity(int id, int capacity){
        if(!containsId(id))
            return false;
        catalogItemsMap.get(id).setMinCapacity(capacity);
        return true;
    }
    public CostumerDiscount getCostumerDiscount(int id){
        return containsId(id) ? catalogItemsMap.get(id).getCostumerDiscount() : null;
    }
    public boolean getCostumerDiscount(int id, CostumerDiscount discount){
        if(!containsId(id))
            return false;
        catalogItemsMap.get(id).setCostumerDiscount(discount);
        return true;
    }
    public ArrayList<CatalogItem> getAllFromCategories(ArrayList<Category> categories, ArrayList<ArrayList<String>> categoriesStrList){
        ArrayList<CatalogItem> matchingCatalogItems = new ArrayList<>();
        for (CatalogItem catalogItem : catalogItemsMap.values()) {
            if (categories.contains(catalogItem.getCategory())) {
                matchingCatalogItems.add(catalogItem);
                continue;
            }
            for (ArrayList<String> categoriesStr : categoriesStrList) {
                if (catalogItem.isFromCategory(categoriesStr)) {
                    matchingCatalogItems.add(catalogItem);
                    break;
                }
            }
        }
        return matchingCatalogItems;
    }
    public void setManyCostumerDiscounts(ArrayList<CatalogItem> catalogItems, CostumerDiscount discount){
        for(CatalogItem catalogItem : catalogItems)
            catalogItem.setCostumerDiscount(discount);
    }
    public void setCategoryDiscount(Category category, CostumerDiscount discount){
        ArrayList<Category> categories= new ArrayList<>();
        ArrayList<ArrayList<String>> categoriesStrList = new ArrayList<>();
        categories.add(category);
        setManyCostumerDiscounts(getAllFromCategories(categories, categoriesStrList), discount);
    }
    public void setCategoryDiscount(ArrayList<String> categoriesStr, CostumerDiscount discount){
        ArrayList<Category> categories= new ArrayList<>();
        ArrayList<ArrayList<String>> categoriesStrList = new ArrayList<>();
        categoriesStrList.add(categoriesStr);
        setManyCostumerDiscounts(getAllFromCategories(categories, categoriesStrList), discount);
    }
}
