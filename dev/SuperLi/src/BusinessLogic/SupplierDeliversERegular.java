package BusinessLogic;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class SupplierDeliversERegular extends SupplierDelivers {
    private int numberOfDaysToDeliver;

    public SupplierDeliversERegular(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers,
                                    SupplierCard supplierCard, SupplierContract supplierContract, int numberOfDaysToDeliver) {
        super(supplierCatagories, supplierManufacturers, supplierCard, supplierContract);
        if (numberOfDaysToDeliver < 0)
            throw new InvalidParameterException("number of days until supplier delivers must be positive");
        this.numberOfDaysToDeliver = numberOfDaysToDeliver;
    }

    public int getNumberOfDaysToDeliver()
    {
        return this.numberOfDaysToDeliver;
    }

//    public Date arrivalTime(Date dateOfOrder)
//    {
//        //TO CHANGE!
//        return dateOfOrder;
//    }
    public String printSupplyTimeData()
    {
       return "Delivery in " + numberOfDaysToDeliver + "days from today.";
    }

    public int daysTillArrives(Day dayOfOrder)
    {
        return numberOfDaysToDeliver;
    }
}
