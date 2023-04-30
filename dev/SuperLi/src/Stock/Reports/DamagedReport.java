package SuperLi.src.Stock.Reports;

import SuperLi.src.Stock.DamageType;
import SuperLi.src.Stock.StockItem;

public class DamagedReport extends AItemReport {
    public String[] get_data(StockItem item){
        String barcode = String.valueOf(item.getBarcode());
        String id = String.valueOf(item.getCatalogItem().getId());
        String name = item.getCatalogItem().getName();
        String category = item.getCatalogItem().getCategory().toString();
        String location = get_location(item);
        DamageType damage = item.getDamage();
        String expiration = item.is_expired() ? String.format("%d days expired", item.dateDifference()) : "";
        String damage_type = damage == DamageType.NONE ? "" : damage.name();
        String damage_description = damage_type + (item.is_expired() && damage != DamageType.NONE ? " and " : "") + expiration;
        return new String[]{barcode, id, name, category, location, damage_description};
    }
    public String[] get_header(){
        return new String[]{"Barcode", "ID", "Name", "Category", "Location", "Damage Description"};
    }
}