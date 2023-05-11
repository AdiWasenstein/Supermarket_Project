package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.Supplier;
import SuperLi.src.BusinessLogic.SupplierItem;
import SuperLi.src.BusinessLogic.SupplierContract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class SupplierDataMapper extends ADataMapper<Supplier> {

    Map<Integer, Supplier> supplierIdentityMap;
    static SupplierDataMapper supplierDataMapper = null;
    private SupplierDataMapper()
    {
        supplierIdentityMap = new HashMap<>();
    }
    public static SupplierDataMapper getInstance()
    {
        if(supplierDataMapper == null)
            supplierDataMapper = new SupplierDataMapper();
        return supplierDataMapper;
    }

    //THE NEXT FUNCTIONS TAKE CARE OF SUPPLIER ITEMS

    //insert new Supplier Item
    public void insertSupplierItem(SupplierItem supplierItem, int supplierId)
    {
        SupplierItemDataMapper.getInstance().insert(supplierItem,supplierId);
    }

    //this func is called by stock. whenever a catalog item is deleted, all fitting supplier items have to be deleted too.
    public void deleteMatchingCatalog(int catalogId)
    {
        HashMap<Integer,SupplierItem> supplierItemsFitToCatalogItem = SupplierItemDataMapper.getInstance().findAllFitToCatalogItemInIdMap(catalogId);
        // need to remove from supp item map
        for(Integer supplierId : supplierItemsFitToCatalogItem.keySet())
        {
            SupplierItem sItem = supplierItemsFitToCatalogItem.get(supplierId);
            this.deleteAccordingToSupplierItem(supplierId, sItem);
        }
        //remove supplier items
        SupplierItemDataMapper.getInstance().removeSupplierItemsFitToCatalogItemInIdMap(supplierItemsFitToCatalogItem);
    }

    //I STOPPED HERE
    private void deleteAccordingToSupplierItem(int supplierId, SupplierItem sItem)
    {
        Supplier sup = this.supplierIdentityMap.get(supplierId);
        if(sup == null)
            return;//TO DO
        //remove supplierItem from supplier
        SupplierContract contract = sup.getSupplierContract();
        try
        {
            contract.removeItem(sItem.getCatalogNumber());
        }
        catch (Exception e)
        {

        }
        //remove item units discounts that are related to supplier item
        DiscountDataMapper.getInstance().removeFromIdentityMap(supplierId,sItem.getCatalogNumber());
        //remove supplier item from periodic reports that contain it.
        PeriodicReportDataMapper.getInstance().removeSupplierItemFromPeriodicReports(sItem,supplierId);
    }
    public void deleteSupplierItem(SupplierItem sItem, int supplierId)
    {
        SupplierItemDataMapper.getInstance().delete(sItem, supplierId);

    }





}
