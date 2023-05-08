package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.SupplierItem;

import java.util.HashMap;
import java.util.Map;

public class SupplierItemDataMapper extends ADataMapper{
    Map<Integer, SupplierItem> supplierItemIdentityMap;
    static SupplierItemDataMapper supplierItemDataMapper = null;
    private SupplierItemDataMapper()
    {
        supplierItemIdentityMap = new HashMap<>();
    }
    public static SupplierItemDataMapper getInstance()
    {
        if(supplierItemDataMapper == null)
            supplierItemDataMapper = new SupplierItemDataMapper();
        return supplierItemDataMapper;
    }
}
