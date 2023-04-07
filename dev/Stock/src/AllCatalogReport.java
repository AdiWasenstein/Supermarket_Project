package Stock.src;

public class AllCatalogReport extends ACatalogReport{
    public String[] get_data(CatalogItem item){
        String id = String.valueOf(item.get_id());
        String name = item.get_name();
        String category = item.get_category().toString();
        String manufacturer = item.get_manufacturer();
        String sell_price = item.get_price() + "ILS";
        String shelves_location = String.valueOf(item.get_shelves_location());
        String back_location = String.valueOf(item.get_back_location());
        String shelves_amount = String.valueOf(item.get_shelves_amount());
        String back_amount = String.valueOf(item.get_back_amount());
        String total_amount = String.valueOf(item.get_total_amount());
        String min_cap = String.valueOf(item.get_min_capacity());
        String discount = item.get_discount() != null ? item.get_discount().toString() : "";
        return new String[]{id, name, category, manufacturer, sell_price,
                shelves_location + " & " + back_location, shelves_amount + " & " + back_amount, total_amount + " & " + min_cap,
                discount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Price",
                "Locations", "Amounts", "Tot & Min",
                "Discount Details"};
    }
}
