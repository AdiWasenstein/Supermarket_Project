package Stock.src;

public class DamagedReport extends AItemReport {
    public String[] get_data(Item item){
        String barcode = String.valueOf(item.get_barcode());
        String id = String.valueOf(item.get_catalog_item().get_id());
        String name = item.get_catalog_item().get_name();
        String category = item.catalog_item.get_category().toString();
        String location = get_location(item);
        DamageType damage = item.get_damage();
        String expiration = item.is_expired() ? String.format("%d days expired", item.date_difference()) : "";
        String damage_type = damage == DamageType.NONE ? "" : damage.name();
        String damage_description = damage_type + (item.is_expired() && damage != DamageType.NONE ? " and " : "") + expiration;
        return new String[]{barcode, id, name, category, location, damage_description};
    }
    public String[] get_header(){
        return new String[]{"Barcode", "Name", "ID", "Category", "Location", "Damage Description"};
    }
}