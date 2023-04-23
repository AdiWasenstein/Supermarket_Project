package SuperLi.src.Stock;
import SuperLi.src.*;
import SuperLi.src.Stock.Reports.AllCatalogReport;
import SuperLi.src.Stock.Reports.CategoryReport;

import java.util.*;

public class StockManagementFacade {
    static StockManagementFacade singleStock = null;
    StockManagement stockManagement;
    final static int BACKSTART = 1000;
    final static int BACKEND = 2000;
    private StockManagementFacade(){
        branches = new ArrayList<>();
        branches.add(new Branch("Beer Sheva"));
        branches.add(new Branch("Rishon Lezion"));
        branches.add(new Branch("Netanya"));
        stockManagement = new StockManagement();
    }
    public static StockManagementFacade getInstance(){
        if(singleStock == null)
            singleStock = new StockManagementFacade();
        return singleStock;
    }
    //Catalog Items
    public boolean addCatalogItem(int id, String name, Category category, String manufacturer, double sellPrice, int minCapacity){
        Random rnd = new Random();
        int shelves = rnd.nextInt(BACKSTART); int back = rnd.nextInt(BACKEND);
        return stockManagement.addCatalogItem(id, name, category, manufacturer, sellPrice, minCapacity, shelves, back);
    }
    public boolean removeCatalogItem(int id){
        return stockManagement.removeCatalogItem(id);
    }
    public boolean setPrice(int id, double price) {
        return stockManagement.setSellPrice(id, price);
    }
    public boolean setMinCapacity(int id, int capacity){
        return stockManagement.setMinCapacity(id, capacity);
    }
    public boolean setItemDiscount(int id, CostumerDiscount costumerDiscount){
        CatalogItem catalogItem = this.catalogItemsMap.get(id);
        if(catalogItem == null)
            return false;
        catalogItem.setCostumerDiscount(costumerDiscount);
        return true;
    }
    public void setCategoryDiscount(Category category, CostumerDiscount costumerDiscount){
        for(CatalogItem catalogItem : this.catalogItemsMap.values())
            if(catalogItem.isFromCategory(category))
                catalogItem.setCostumerDiscount(costumerDiscount);
    }
    public void generateCatalogReport(){
        AllCatalogReport rep = new AllCatalogReport();
        for(CatalogItem catalogItem : catalogItemsMap.values())
            rep.add_to_report(catalogItem);
        rep.generate_report();
    }
    public void generateCategoryReport(ArrayList<Category> categories, ArrayList<ArrayList<String>> categoriesStrList) {
        CategoryReport categoryReport = new CategoryReport();
        for (CatalogItem catalogItem : this.catalogItemsMap.values()) {
            Category itemCategory = catalogItem.getCategory();
            if (categories.contains(itemCategory)) {
                categoryReport.add_to_report(catalogItem);
                continue;
            }
            for (ArrayList<String> categoriesStr : categoriesStrList) {
                if (itemCategory.getCategories().containsAll(categoriesStr)) {
                    categoryReport.add_to_report(catalogItem);
                    break;
                }
            }
            categoryReport.generate_report();
        }
    }
}
