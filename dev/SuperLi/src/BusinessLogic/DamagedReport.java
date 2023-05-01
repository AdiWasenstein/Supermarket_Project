package SuperLi.src.BusinessLogic;

public class DamagedReport extends AStockItemReport {
    public String[] getRecordData(StockItem stockItem){
        String barcode = String.valueOf(stockItem.getBarcode());
        String id = String.valueOf(stockItem.getCatalogItem().getId());
        String name = stockItem.getCatalogItem().getName();
        String category = stockItem.getCatalogItem().getCategory().toString();
        String location = getLocation(stockItem);
        DamageType damage = stockItem.getDamage();
        String expiration = stockItem.isExpired() ? String.format("%d days expired", stockItem.dateDifference()) : "";
        String damageType = damage == DamageType.NONE ? "" : damage.name();
        String damageDescription = damageType + (stockItem.isExpired() && damage != DamageType.NONE ? " and " : "") + expiration;
        return new String[]{barcode, id, name, category, location, damageDescription};
    }
    public String[] getHeaders(){
        return new String[]{"Barcode", "ID", "Name", "Category", "Location", "Damage Description"};
    }
}