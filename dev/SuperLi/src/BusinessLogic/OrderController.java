package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.OrderDataMapper;
//import org.graalvm.collections.Pair;
import SuperLi.src.BusinessLogic.Pair;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import SuperLi.src.DataAccess.SupplierItemDataMapper;


import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class OrderController {
    private static OrderController instance = new OrderController();
    private OrderDataMapper orderDataMapper;
    private SupplierItemDataMapper supplierItemDataMapper;
    private PeriodicReportDataMapper periodicReportDataMapper;

    private OrderController()
    {
        this.orderDataMapper = OrderDataMapper.getInstance();
        this.periodicReportDataMapper = PeriodicReportDataMapper.getInstance();
        this.supplierItemDataMapper = SupplierItemDataMapper.getInstance();
    }

    public static OrderController getInstance(){return instance;}




    // this methood called by stock modoul and with given valid branch number and missing item report will make new order
    // the methood will return list of order that matches the report
    public LinkedList<Order> createNewMissingItemsOrder() //TODO parameter report, number of branch
    {
        // calls the methood creatOrder in orderManagment
        return null;
    }

    // this methood receive supplier id and list of catalog numbers and amount and create report accordingly
    private PeriodicReport createNewPeriodicReport(int suppId, LinkedList<Pair<Integer, Integer>> items) //TODO parameter
    {
        return null;
    }

    // thi methood will recieve periodic report and return order accordingly
    public Order createNewPeriodicOrder(int suppId, LinkedList<Pair<Integer, Integer>> items) //TODO parameter
    {
        // need to decide if accessing data mapper and finds the object or data managment
        // first calling the report methood
        // than creating new order
        return null;
    }


    public LinkedList<PeriodicReport> getAllPeriodicReports()
    {
        return null;
    }

    public PeriodicReport getReportById (int reportNumber)
    {
        // need to check that exist
        // need to check the time
        return null;
    }

    //this func gets reportId and HashMap of items according to their market id, and their quantities to update.
    public boolean updateReport (int reportId, HashMap<Integer,Integer> itemsWithUpdatedQuantities)throws InvalidParameterException
    {
        // check if there is report with that number
        if(itemsWithUpdatedQuantities.isEmpty())
            return true;
        Optional<PeriodicReport> resultReport = this.periodicReportDataMapper.find(Integer.toString(reportId));
        if(resultReport.isEmpty())//report with given report id wasn't found.
            return false;
        PeriodicReport report = resultReport.get();
        for(Integer marketId : itemsWithUpdatedQuantities.keySet())
        {
            report.setQuantityOfItem(marketId,itemsWithUpdatedQuantities.get(marketId));
        }
        this.periodicReportDataMapper.update(report);
        return true;
    }

    //this func gets a branch number and a report id, and returns a list of all the items in the report- represented by the market id they fit to.
    public LinkedList<Integer> allItemsInPeriodicReport(int reportId, int branchNumber)
    {
        LinkedList<Integer> itemsAccordingToMarketId = new LinkedList<>();
        Optional<PeriodicReport> resultReport = this.periodicReportDataMapper.find(Integer.toString(reportId));
        if(resultReport.isEmpty())//report with given report id wasn't found.
            return null;
        PeriodicReport report = resultReport.get();
        if(report.getBranchNumber() != branchNumber)//report with given id isn't for the branch with the branch id given.
            return null;
        //else - if report with given id and given branch number was found.
        for(SupplierItem supplierItem : report.getSupplierItems())
        {
            itemsAccordingToMarketId.add(supplierItem.GetMarketId());
        }
        return itemsAccordingToMarketId;
    }



}
