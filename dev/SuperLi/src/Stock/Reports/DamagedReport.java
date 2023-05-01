package SuperLi.src.Stock.Reports;

import SuperLi.src.Stock.DamageType;
import SuperLi.src.Stock.StockItem;

public class DamagedReport extends AStockItemReport {
    public String[] getRecordData(StockItem stockItem){
        String barcode = String.valueOf(stockItem.getBarcode());
        String id = String.valueOf(stockItem.getCatalogItem().getId());
        String name = stockItem.getCatalogItem().getName();
        String category = stockItem.getCatalogItem().getCategory().toString();
        String location = getLocation(stockItem);
        DamageType damage = stockItem.getDamage();
        String expiration = stockItem.is_expired() ? String.format("%d days expired", stockItem.dateDifference()) : "";
        String damageType = damage == DamageType.NONE ? "" : damage.name();
        String damageDescription = damageType + (stockItem.is_expired() && damage != DamageType.NONE ? " and " : "") + expiration;
        return new String[]{barcode, id, name, category, location, damageDescription};
    }
    public String[] getHeaders(){
        return new String[]{"Barcode", "ID", "Name", "Category", "Location", "Damage Description"};
    }
}