package SuperLi.src.Stock.Reports;

import SuperLi.src.CatalogItem;

public class AllCatalogReport extends ACatalogReport{
    public String[] getRecordData(CatalogItem catalogItem){
        String id = String.valueOf(catalogItem.getId());
        String name = catalogItem.getName();
        String category = catalogItem.getCategory().toString();
        String manufacturer = catalogItem.getManufacturer();
        String sellPrice = catalogItem.getSellPrice() + "ILS";
        String shelvesLocation = String.valueOf(catalogItem.getShelvesLocation());
        String backLocation = String.valueOf(catalogItem.getBackLocation());
        String minCapacity = String.valueOf(catalogItem.getMinCapacity());
        String discount = catalogItem.getCostumerDiscount() != null ? catalogItem.getCostumerDiscount().toString() : "";
        return new String[]{id, name, category, manufacturer, sellPrice,
                shelvesLocation + " & " + backLocation,
                discount};
    }
    public String[] getHeaders(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Price", "Locations", "CostumerDiscount Details"};
    }
}
