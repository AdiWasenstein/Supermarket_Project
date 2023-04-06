package Stock.src;

public class CategoryReport extends ACatalogReport{
    public String[] get_data(CatalogItem catalog_item){
        String id = String.valueOf(catalog_item.get_id());
        String name = catalog_item.get_name();
        String category = catalog_item.get_category().toString();
        String manufacturer = catalog_item.get_manufacturer();
        String sell_price = catalog_item.get_price() + "ILS";
        String amount = String.valueOf(catalog_item.get_total_amount());
        String min = String.valueOf(catalog_item.get_min_capacity());
        String discount = catalog_item.get_discount() == null ? "" : catalog_item.get_discount().toString();
        return new String[]{id, name, category, manufacturer, sell_price, amount, min, discount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Sell Price", "Current Amount", "Min Capacity", "Discount Description"};
    }
}
