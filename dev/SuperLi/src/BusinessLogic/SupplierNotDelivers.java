package SuperLi.src.BusinessLogic;

import java.util.LinkedList;

public class SupplierNotDelivers extends Supplier {


    public SupplierNotDelivers(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers, SupplierCard supplierCard, SupplierContract supplierContract) {
        super(supplierCatagories, supplierManufacturers, supplierCard, supplierContract);
    }

    public boolean needDelivery(){ return true; }

    public String printSupplyTimeData()
    {
        return "Not delivers, can supply everyday";
    }
    public int daysTillArrives(Day dayOfOrder)
    {
        return 0;
    }
}
