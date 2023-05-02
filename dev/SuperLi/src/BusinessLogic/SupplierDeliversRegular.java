package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class SupplierDeliversRegular extends SupplierDelivers {
    private LinkedList<Day> deliveryDays;

    public SupplierDeliversRegular(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers,
                                   SupplierCard supplierCard, SupplierContract supplierContract, LinkedList<Day> deliveryDays) {
        super(supplierCatagories, supplierManufacturers, supplierCard, supplierContract);
        if (deliveryDays.isEmpty())
            throw new InvalidParameterException("SuperLi.src.BusinessLogic.Supplier must supply items in at least one day");
        this.deliveryDays = deliveryDays;
    }

    public LinkedList<Day> getDeliveryDays()
    {
        return this.deliveryDays;
    }

//    ///// need to be tested
//    @Override
//    public Date arrivalTime(Date dateOfOrder) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateOfOrder);
//        // find the day the order was made
//        int dayOfOrder = cal.get(Calendar.DAY_OF_WEEK);
//        // find the closest day that the suppliers delivers at
//        int mindiff = 7;
////        SuperLi.src.BusinessLogic.Day closestDay;
//        for (SuperLi.src.BusinessLogic.Day day : deliveryDays) {
//            // if the day is after
//            if (day.ordinal() >= cal.get(Calendar.DAY_OF_WEEK) - 1){
//                int diff = Math.abs(day.ordinal() + 1 - dayOfOrder); // Compute the absolute difference
//                if (diff < mindiff) { // Update the closest day if the difference is smaller
//                    mindiff = diff;
////                closestDay = day;
//                }
//            }
//
//        }
//        cal = Calendar.getInstance();
//        cal.setTime(dateOfOrder);
//        cal.add(Calendar.DATE, mindiff); // Add the difference between the closest day and dayOfWeek to the date
//        Date closestDate = cal.getTime();
//        return closestDate;
//    }
    public String printSupplyTimeData()
    {
        return "Delivers on: " + deliveryDays.toString();
    }

    // TODO check this method
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
