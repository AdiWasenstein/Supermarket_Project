package BusinessLogic;

public class StockItemsReport extends AStockItemReport {
    public String[] getRecordData(StockItem stockItem){
        CatalogItem catalogItem = stockItem.getCatalogItem();
        String barcode = String.valueOf(stockItem.getBarcode());
        String name = catalogItem.getName();
        String id = String.valueOf(catalogItem.getId());
        String costPrice = String.valueOf(stockItem.getCostPrice());
        String sellPrice = stockItem.getCatalogItem().getSellPrice() + "ILS";
        String costumerDiscount = stockItem.getCatalogItem().getCostumerDiscount() == null ? "" : stockItem.getCatalogItem().getCostumerDiscount().toString();
        String location = getLocation(stockItem);
        String expirationDate = stockItem.getExpirationString();
        String damageType = stockItem.getDamage() == DamageType.NONE ? "" : stockItem.getDamage().name();
        return new String[]{barcode, id, name, location, expirationDate, damageType, costPrice + " & " + sellPrice, costumerDiscount};
    }
    public String[] getHeaders(){
        return new String[]{"Barcode", "ID", "Name", "Location", "Expiration", "Damage", "Cost & Sell Price", "CostumerDiscount Description"};
    }
}
