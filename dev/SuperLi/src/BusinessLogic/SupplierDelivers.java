package SuperLi.src.BusinessLogic;

import java.util.LinkedList;

public abstract class SupplierDelivers extends Supplier {


    public SupplierDelivers(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers, SupplierCard supplierCard, SupplierContract supplierContract) {
        super(supplierCatagories, supplierManufacturers, supplierCard, supplierContract);

    }

    @Override
    public boolean needDelivery() {
        return false;
    }

    // check if really needed


}
