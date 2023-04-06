package Stock.src;

public class CurrentItemsReport extends AItemReport {
    public String[] get_data(Item item){
        CatalogItem catalog_item = item.get_catalog_item();
        String barcode = String.valueOf(item.get_barcode());
        String name = catalog_item.get_name();
        String id = String.valueOf(catalog_item.get_id());
        String cost_price = item.get_cost_price() + "ILS";
        String sell_price = item.get_catalog_item().get_price() + "ILS";
        String discount = item.get_catalog_item().get_discount() == null ? "" : item.get_catalog_item().get_discount().toString();
        String location = get_location(item);
        String expiration_date = item.get_expiration_str();
        String damage_type = item.get_damage() == DamageType.NONE ? "" : item.get_damage().name();
        return new String[]{barcode, id, name, location, expiration_date, damage_type, cost_price, sell_price, discount};
    }
    public String[] get_header(){
        return new String[]{"Barcode", "ID", "Name", "Location", "Expiration", "Damage", "Cost Price", "Sell Price", "Discount Description"};
    }
}
