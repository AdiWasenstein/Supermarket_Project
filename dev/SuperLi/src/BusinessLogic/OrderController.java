package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.OrderDataMapper;
//import org.graalvm.collections.Pair;
import SuperLi.src.BusinessLogic.Pair;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import SuperLi.src.DataAccess.SupplierItemDataMapper;
import SuperLi.src.DataAccess.BranchDataMapper;
import SuperLi.src.DataAccess.SupplierDataMapper;


import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class OrderController {
    private static OrderController instance = null;
    private OrderManagment orderManagment;
    private OrderDataMapper orderDataMapper;
    private SupplierItemDataMapper supplierItemDataMapper;
    private PeriodicReportDataMapper periodicReportDataMapper;
    private BranchDataMapper branchDataMapper;
    private SupplierDataMapper supplierDataMapper;

    private OrderController()
    {
        this.orderDataMapper = OrderDataMapper.getInstance();
        this.periodicReportDataMapper = PeriodicReportDataMapper.getInstance();
        this.supplierItemDataMapper = SupplierItemDataMapper.getInstance();
        this.orderManagment = OrderManagment.getInstance();
        this.branchDataMapper = BranchDataMapper.getInstance();
        this.supplierDataMapper = SupplierDataMapper.getInstance();

    }

    public static OrderController getInstance()
    {
        if (instance == null)
            instance = new OrderController();
        return instance;
    }




    // this method called by stock modoul and with given valid branch number and missing item report will make new order
    // the method will return true if orders were made successfully, false if not.
    public boolean createNewMissingItemsOrder(RequiredStockReport report)
    {
        if (report == null)
            return false;

        // get the data from the report
        Map<Integer, Integer> itemsAmount = report.getReportData();
        int branchNumber = report.getBranchId();

        Optional<Branch> optionalBranch = BranchDataMapper.getInstance().find(Integer.toString(branchNumber));
        if (optionalBranch.isEmpty())
            return false;
        Branch branch = optionalBranch.get();

        // get all suppliers in the system
        LinkedList<Supplier> allSupp = SupplierDataMapper.getInstance().findAll();
        if (allSupp.isEmpty())
            return false;
        LinkedList<Order> orders = new LinkedList<>();
        try
        {
              orders = this.orderManagment.creatMissingOrder(itemsAmount, branchNumber, allSupp);
              if (orders.isEmpty())
                  return false;
        }
        catch (Exception e)
        {
            return false;
        }
        // update data in the system
        for (Order order : orders)
        {
            branch.addOrder(order);
            this.orderDataMapper.insert(order);
        }
        return true;
    }

    // this method receive supplier id and list of catalog numbers and amount and create report accordingly
    public PeriodicReport createNewPeriodicReport(int branchNumber, Supplier supp, Day day, HashMap<Integer,Integer> items)
    {
        PeriodicReport report = this.orderManagment.createPeriodicReport(branchNumber, supp, day, items);
         // check if repord eas created successfully
          if (report == null)
              return null;
          // update report data mapper
        this.periodicReportDataMapper.insert(report);
        // if the day the report need to be sent is the day today, make a new order
        Order orderFromNewReport = this.orderManagment.createOrderOfNewPeriodic(report);
//        if (orderFromNewReport != null)
//            this.orderDataMapper.insert(orderFromNewReport);
        if (orderFromNewReport != null)
            addOrderToSystemData(orderFromNewReport);
        return report;
    }



    public LinkedList<PeriodicReport> getAllPeriodicReports()
    {
        return this.periodicReportDataMapper.findAll();
    }


    //this func gets reportId and HashMap of items according to their market id, and their quantities to update.
    public boolean updateReport (int reportId, HashMap<Integer,Integer> itemsWithUpdatedQuantities)
    {
        if(itemsWithUpdatedQuantities.isEmpty())
            return true;
        Optional<PeriodicReport> resultReport = this.periodicReportDataMapper.find(Integer.toString(reportId));
        if(resultReport.isEmpty())//report with given report id wasn't found.
            return false;
        PeriodicReport report = resultReport.get();
        HashMap<SupplierItem,Integer> itemsToUpdate = new HashMap<>();
        for(Integer marketId : itemsWithUpdatedQuantities.keySet())
        {
            Pair temp = report.setQuantityOfItem(marketId,itemsWithUpdatedQuantities.get(marketId));
            if(temp==null)
                continue;
            itemsToUpdate.put((SupplierItem)temp.getLeft(),(Integer)temp.getRight());
        }
        this.periodicReportDataMapper.updateQueryForItems(itemsToUpdate, reportId);
        return true;
    }

    private boolean canUpdateReport(PeriodicReport report)
    {
        //get the day of week of today
        LocalDate date = LocalDate.now();
        DayOfWeek currentDayOfWeek = date.getDayOfWeek();
        String dayOfWeekString = currentDayOfWeek.toString();
        //get the day to order
        Day dayToOrder = report.getDayToOrder();
        String dayToOrderString = dayToOrder.toString();
        // Get the current time and calculate the delay until the constant hour to make periodic orders.
        long delay = this.delayBetweenTimes();
        //if today is also the day to create an order
        if(dayToOrderString.equalsIgnoreCase(dayOfWeekString))//meaning the day to order is today
        {
            if(delay < 0)//meaning the order was already created, it is possible to update the report for next week.
                return true;
            else
                return false;
        }
        //if today is one day before the order is created
        else if(report.oneDayBeforeOrderDay().toString().equalsIgnoreCase((dayOfWeekString))) {
            //if there is less than 24 hours
            if (delay < 0)
                return false;
            return true;
        }
        else {
            return true;
        }
    }

    //this func gets a branch number and a report id, and returns a list of all the items in the report- represented by the market id they fit to.
    //throws Exception if can't update the report it's id was given.
    public LinkedList<Integer> allItemsInPeriodicReport(int reportId, int branchNumber)throws Exception
    {
        LinkedList<Integer> itemsAccordingToMarketId = new LinkedList<>();
        Optional<PeriodicReport> resultReport = this.periodicReportDataMapper.find(Integer.toString(reportId));
        if(resultReport.isEmpty())//report with given report id wasn't found.
            return null;
        PeriodicReport report = resultReport.get();
        if(report.getBranchNumber() != branchNumber)//report with given id isn't for the branch with the branch id given.
            return null;
        //check if it's possible to update the report (more than 24 hours before creating an order)
        if(!this.canUpdateReport(report))
            throw new Exception("Only allowed to update report if there are more than 24 hours left to creating order.");
        //else - if report with given id and given branch number was found.
        for(SupplierItem supplierItem : report.getSupplierItems())
        {
            itemsAccordingToMarketId.add(supplierItem.GetMarketId());
        }
        return itemsAccordingToMarketId;
    }

    public LinkedList<PeriodicReport> findReportsOfBranch(int branchID)
    {
        if (branchID <= 0)
            return null;
//       Optional<Branch> branchFound = this.branchDataMapper.find(branchID.toString());
//       // check if branch with the given id is found. If not returns null, else get it.
//       if (branchFound.isEmpty())
//           return null;

        LinkedList<PeriodicReport> reportsFound = this.periodicReportDataMapper.findByBranch(branchID);
        return reportsFound;
//        for (PeriodicReport currReport : reportsFound)
//            System.out.println(currReport.toString());
    }



    public void makeOrdersOfToday()
    {
        LinkedList<PeriodicReport> allPeriodicReports = this.getAllPeriodicReports();
        if(allPeriodicReports.isEmpty())
            return;
        LinkedList<Order> newPeriodicOrders = this.orderManagment.createAllPeriodicOrdersOfToday(allPeriodicReports);
        for(int i=0;i<newPeriodicOrders.size();i++)
        {
            this.orderDataMapper.insert(newPeriodicOrders.get(i));
        }
    }

    private long delayBetweenTimes()
    {
        // Get the current time and calculate the delay until the constant hour to make periodic orders.
        Calendar now = Calendar.getInstance();
        Calendar scheduledTime = Calendar.getInstance();
        scheduledTime.set(Calendar.HOUR_OF_DAY, this.orderManagment.getHourToRunEveryDay());
        scheduledTime.set(Calendar.MINUTE, this.orderManagment.getMinuteToRunEveryDay());
        scheduledTime.set(Calendar.SECOND, this.orderManagment.getSecondToRunEveryDay());
        scheduledTime.set(Calendar.MILLISECOND, this.orderManagment.getMilliSecondToRunEveryDay());
        long delay = scheduledTime.getTimeInMillis() - now.getTimeInMillis();
        return delay;
    }
    //this function runs every day at the same constant time.
    public void runEveryDayToMakeOrders() {
        // Create a Timer object
        Timer timer = new Timer();
        // Get the current time and calculate the delay until the constant hour to make periodic orders.
        long delay = this.delayBetweenTimes();
        if (delay < 0) {
            // If the scheduled time has already passed today, schedule for tomorrow instead
            delay += 24 * 60 * 60 * 1000;
        }
        // Schedule the task to run every day at constant hour.
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Call the function you want to run at the scheduled time
                makeOrdersOfToday();
            }
        }, delay, 24 * 60 * 60 * 1000);
    }

    public Branch getBranchByID(int branchID)
    {
        Optional<Branch> res = this.branchDataMapper.find(Integer.toString(branchID));
        if (res.isEmpty())
            return null;
        return res.get();
    }

    public Supplier getSuppByID(int suppID)
    {
        Optional<Supplier> res = this.supplierDataMapper.find(Integer.toString(suppID));
        if (res.isEmpty())
            return null;
        return res.get();
    }

    public LinkedList<Supplier> getAllSuppliers()
    {
        return this.supplierDataMapper.findAll();
    }

    public LinkedList<SupplierItem> getAllItemsOfSupplier(int suppID)
    {
        Supplier supp = getSuppByID(suppID);
        if (supp != null)
            return supp.getAllSuppItem();
        return null;
    }

    private void addOrderToSystemData(Order order)
    {
        if (order == null)
            return;
        Supplier suppOfOrder = order.getOrderSupplier();
        Branch branchOfOrder = this.branchDataMapper.find(Integer.toString(order.branchNumber)).get();
        // adding to the objects in cash
//        suppOfOrder.addOrder(order);
        branchOfOrder.addOrder(order);
        // insert to DB
        this.orderDataMapper.insert(order);

    }



}
