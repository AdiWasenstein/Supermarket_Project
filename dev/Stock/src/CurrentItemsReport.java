package Stock.src;

public class CurrentItemsReport extends AItemReport {
    public String[] get_data(Item item){
        CatalogItem catalog_item = item.get_catalog_item();
        String barcode = String.valueOf(item.get_barcode());
        String name = catalog_item.get_name();
        String id = String.valueOf(catalog_item.get_id());
        String cost_price = String.valueOf(item.get_cost_price());
        String sell_price = String.valueOf(item.get_catalog_item().get_price());
        String discount = item.get_catalog_item().get_discount() == null ? "" : item.get_catalog_item().get_discount().toString();
        String location = String.valueOf(item.get_location());
        String expiration_date = item.get_expiration_str();
        String damage_type = item.get_damage() == DamageType.NONE ? "" : item.get_damage().name();
        return new String[]{barcode, name, id, location, expiration_date, damage_type, cost_price, sell_price, discount};
    }
    public String[] get_header(){
        return new String[]{"Barcode", "ID", "Name", "Location", "Expiration Date", "Damage Type", "Cost Price", "Sell Price", "Discount"};
    }
}
