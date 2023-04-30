package SuperLi.src.Stock.Reports;

import SuperLi.src.Branch;
import SuperLi.src.CatalogItem;
import SuperLi.src.Category;

public class RequiredStockReport extends ACatalogReport{ // To add ACatalog
    public RequiredStockReport(int branchId){super(branchId);}
    public String[] get_data(CatalogItem catalog_item){
        String id = String.valueOf(catalog_item.getId());
        String name = catalog_item.getName();
        String amount = String.valueOf(catalog_item.getMinCapacity() * 2 - catalog_item.getTotalAmount(this.branchId));
        String manufacturer = catalog_item.getManufacturer();
        Category category = catalog_item.getCategory();
        String size = String.format("%.1f %ss", category.getSizeAmount(), category.getMeasureUnit().name());
        return new String[]{id, manufacturer, name, size, amount};
    }
    public String[] get_header(){
        return new String[]{"ID", "Manufacturer", "Name", "Size", "Amount To Order"};
    }
}
