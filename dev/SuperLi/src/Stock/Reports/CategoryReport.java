package SuperLi.src.Stock.Reports;

import SuperLi.src.CatalogItem;

public class CategoryReport extends ACatalogReport{
    public String[] get_data(CatalogItem catalog_item){
        String id = String.valueOf(catalog_item.getId());
        String name = catalog_item.getName();
        String category = catalog_item.getCategory().toString();
        String manufacturer = catalog_item.getManufacturer();
        String sell_price = catalog_item.getPrice() + "ILS";
        String amount = String.valueOf(catalog_item.getTotalAmount());
        String min = String.valueOf(catalog_item.getMinCapacity());
        String discount = catalog_item.getDiscount() == null ? "" : catalog_item.getDiscount().toString();
        return new String[]{id, name, category, manufacturer, sell_price, amount, min, discount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Sell Price", "Current Amount", "Min Capacity", "CostumerDiscount Description"};
    }
}
