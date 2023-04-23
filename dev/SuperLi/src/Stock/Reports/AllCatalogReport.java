package SuperLi.src.Stock.Reports;

import SuperLi.src.CatalogItem;

public class AllCatalogReport extends ACatalogReport{
    public String[] get_data(CatalogItem item){
        String id = String.valueOf(item.getId());
        String name = item.getName();
        String category = item.getCategory().toString();
        String manufacturer = item.getManufacturer();
        String sell_price = item.getSellPrice() + "ILS";
        String shelves_location = String.valueOf(item.getShelvesLocation());
        String back_location = String.valueOf(item.getBackLocation());
        String shelves_amount = String.valueOf(item.getShelvesAmount());
        String back_amount = String.valueOf(item.getBackAmount());
        String total_amount = String.valueOf(item.getTotalAmount());
        String min_cap = String.valueOf(item.getMinCapacity());
        String discount = item.getCostumerDiscount() != null ? item.getCostumerDiscount().toString() : "";
        return new String[]{id, name, category, manufacturer, sell_price,
                shelves_location + " & " + back_location, shelves_amount + " & " + back_amount, total_amount + " & " + min_cap,
                discount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Price",
                "Locations", "Amounts", "Tot & Min",
                "CostumerDiscount Details"};
    }
}
