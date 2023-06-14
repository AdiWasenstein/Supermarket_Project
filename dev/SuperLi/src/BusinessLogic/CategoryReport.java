package BusinessLogic;

public class CategoryReport extends ACatalogReport{
    public String[] getRecordData(CatalogItem catalogItem){
        String id = String.valueOf(catalogItem.getId());
        String name = catalogItem.getName();
        String category = catalogItem.getCategory().toString();
        String manufacturer = catalogItem.getManufacturer();
        String sellPrice = catalogItem.getSellPrice() + "ILS";
        String min = String.valueOf(catalogItem.getMinCapacity());
        String discount = catalogItem.getCostumerDiscount() == null ? "" : catalogItem.getCostumerDiscount().toString();
        return new String[]{id, name, category, manufacturer, sellPrice, min, discount};
    }
    public String[] getHeaders(){
        return new String[]{"ID", "Name", "Category", "Manufacturer", "Sell Price", "Min Capacity", "Costumer Discount Description"};
    }
}
