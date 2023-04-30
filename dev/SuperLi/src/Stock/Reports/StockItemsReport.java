package SuperLi.src.Stock.Reports;

import SuperLi.src.CatalogItem;
import SuperLi.src.Stock.DamageType;
import SuperLi.src.Stock.StockItem;

public class StockItemsReport extends AItemReport {
    public String[] get_data(StockItem item){
        CatalogItem catalog_item = item.getCatalogItem();
        String barcode = String.valueOf(item.getBarcode());
        String name = catalog_item.getName();
        String id = String.valueOf(catalog_item.getId());
        String cost_price = String.valueOf(item.getCostPrice());
        String sell_price = item.getCatalogItem().getPrice() + "ILS";
        String discount = item.getCatalogItem().getCostumerDiscount() == null ? "" : item.getCatalogItem().getCostumerDiscount().toString();
        String location = get_location(item);
        String expiration_date = item.getExpirationString();
        String damage_type = item.getDamage() == DamageType.NONE ? "" : item.getDamage().name();
        return new String[]{barcode, id, name, location, expiration_date, damage_type, cost_price + " & " + sell_price, discount};
    }
    public String[] get_header(){
        return new String[]{"Barcode", "ID", "Name", "Location", "Expiration", "Damage", "Cost & Sell Price", "CostumerDiscount Description"};
    }
}
