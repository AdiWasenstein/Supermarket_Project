package BusinessLogic;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class SupplierDeliversRegular extends SupplierDelivers {
    private LinkedList<Day> deliveryDays;

    public SupplierDeliversRegular(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers,
                                   SupplierCard supplierCard, SupplierContract supplierContract, LinkedList<Day> deliveryDays) {
        super(supplierCatagories, supplierManufacturers, supplierCard, supplierContract);
        if (deliveryDays.isEmpty())
            throw new InvalidParameterException("Supplier must supply items in at least one day");
        this.deliveryDays = deliveryDays;
    }
    public LinkedList<Day> getDeliveryDays()
    {
        return this.deliveryDays;
    }
    public String printSupplyTimeData()
    {
        return "Delivers on: " + deliveryDays.toString();
    }

    public int daysTillArrives(Day dayOfOrder)
    {
        int minimalGap = 8;
        for (Day d : this.deliveryDays)
        {
            // if the day is later this week
            if (d.ordinal() >= dayOfOrder.ordinal())
                if (d.ordinal() - dayOfOrder.ordinal() < minimalGap)
                    minimalGap = d.ordinal() - dayOfOrder.ordinal();
            else
                if (dayOfOrder.ordinal() - d.ordinal() < minimalGap)
                    minimalGap = dayOfOrder.ordinal() - d.ordinal();
        }
        return minimalGap;
    }
}
