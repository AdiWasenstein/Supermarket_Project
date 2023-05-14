package SuperLi.src.BusinessLogic;

import java.util.HashMap;
import java.util.Map;

public class RequiredStockReport extends ACatalogReport{
    int branchId;
    public RequiredStockReport(int branchId){this.branchId = branchId;}
    public String[] getRecordData(CatalogItem catalogItem){
        String id = String.valueOf(catalogItem.getId());
        String name = catalogItem.getName();
        String amount = String.valueOf(catalogItem.getMinCapacity() * 2 - catalogItem.getTotalAmount(this.branchId));
        String manufacturer = catalogItem.getManufacturer();
        Category category = catalogItem.getCategory();
        String size = String.format("%.1f %ss", category.getSizeAmount(), category.getMeasureUnit().name());
        return new String[]{id, manufacturer, name, size, amount};
    }
    public String[] getHeaders(){
        return new String[]{"ID", "Manufacturer", "Name", "Size", "Amount To Order"};
    }

    public Map<Integer, Integer> getReportData(){
        Map<Integer, Integer> amountsToOrder = new HashMap<>();
        for(CatalogItem catalogItem : catalogItems)
            amountsToOrder.put(catalogItem.getId(), catalogItem.getMinCapacity() * 2 - catalogItem.getTotalAmount(this.branchId));
        return amountsToOrder;
    }
    public int getBranchId()
    {
        return this.branchId;
    }
}