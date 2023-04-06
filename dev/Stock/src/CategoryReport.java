package Stock.src;

public class CategoryReport extends ACatalogReport{
    public String[] get_data(CatalogItem catalog_item){
        String id = String.valueOf(catalog_item.get_id());
        String name = catalog_item.get_name();
        String prime = catalog_item.get_category().get_prime_category();
        String sub = catalog_item.get_category().get_sub_category();
        String size = String.format("%.1f %ss", catalog_item.get_category().get_size_amount(), catalog_item.get_category().get_measureunit().name());
        String manufacturer = catalog_item.get_manufacturer();
        String sell_price = catalog_item.get_price() + "₪";
        String amount = String.valueOf(catalog_item.get_total_amount());
        String min = String.valueOf(catalog_item.get_min_capacity());
        String discount = catalog_item.get_discount() == null ? "" : catalog_item.get_discount().toString();
        return new String[]{id, name, prime, sub, size, manufacturer, sell_price, amount, min, discount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Name", "Prime Category", "Sub Category",
                "Size", "Manufacturer", "Sell Price", "Current Amount", "Min Capacity", "Discount"};
    }
}
