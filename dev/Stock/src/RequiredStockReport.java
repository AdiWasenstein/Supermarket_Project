package Stock.src;

public class RequiredStockReport extends ACatalogReport{ // To add ACatalog
    public String[] get_data(CatalogItem catalog_item){
        String id = String.valueOf(catalog_item.get_id());
        String name = catalog_item.get_name();
        String amount = String.valueOf(catalog_item.get_min_capacity() * 2 - catalog_item.get_total_amount());
        String manufacturer = catalog_item.get_manufacturer();
        Category category = catalog_item.get_category();
        String size = String.format("%.1f %ss", category.get_size_amount(), category.get_measureunit().name());
        return new String[]{id, manufacturer, name, size, amount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Manufacturer", "Name", "Size", "Amount To Order"};
    }
}
