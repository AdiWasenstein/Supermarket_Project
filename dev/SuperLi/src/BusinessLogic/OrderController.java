package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.OrderDataMapper;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import SuperLi.src.DataAccess.BranchDataMapper;
//import org.graalvm.collections.Pair;
import SuperLi.src.BusinessLogic.Pair;


import java.util.LinkedList;
import java.util.Optional;

public class OrderController {
    private static OrderController instance = new OrderController();
    private OrderDataMapper orderDataMapper;
//    private BranchDataMapper branchDataMapper;
    private PeriodicReportDataMapper periodicReportDataMapper;



    private OrderController()
    {
        this.orderDataMapper = OrderDataMapper.getInstance();
//        this.branchDataMapper = BranchDataMapper.getInstance();
        this.periodicReportDataMapper = PeriodicReportDataMapper.getInstance();
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

    // maybe not necessary
    public PeriodicReport getReportById (int reportNumber)
    {
        // need to check that exist
        // need to check the time
        return null;
    }

    public boolean updateReport (int reportNumber, int catalogNumber, int newAmount)
    {
        // check if there is report with that number and item

        // check if amount ok
        return true;
    }

    // TODO maybe void- depends if we just print it or returns it
    // TODO maybe parameter is string- depends on how we want to receive it from user
    // given branch number, returnsqprint all the reports of that branch
    public LinkedList<PeriodicReport> findReportsOfBranch(Integer branchID)
    {
        if (branchID <= 0)
            return null;
//       Optional<Branch> branchFound = this.branchDataMapper.find(branchID.toString());
//       // check if branch with the given id is found. If not returns null, else get it.
//       if (branchFound.isEmpty())
//           return null;
        LinkedList<PeriodicReport> reportsFound = this.periodicReportDataMapper.findAll(branchID.toString());
        return reportsFound;

    }

}
