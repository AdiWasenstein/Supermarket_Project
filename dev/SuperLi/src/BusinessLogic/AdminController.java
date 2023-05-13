package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.BranchDataMapper;
import SuperLi.src.DataAccess.OrderDataMapper;
import SuperLi.src.DataAccess.SupplierDataMapper;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class AdminController {
    private static AdminController instance = new AdminController();
    private SupplierDataMapper supplierDataMapper;
    private OrderDataMapper orderDataMapper;
    private BranchDataMapper branchDataMapper;

    private AdminController()
    {
        this.supplierDataMapper = SupplierDataMapper.getInstance();
        this.orderDataMapper = OrderDataMapper.getInstance();
        this.branchDataMapper = BranchDataMapper.getInstance();
    }

    public static AdminController getInstance()
    {
        return instance;
    }

    //this func checks if there is already a supplier with given id.
    public boolean isSupplierIdExists(int id)//CHANGE!
    {
        Optional<Supplier> optionalSupplier = SupplierDataMapper.getInstance().find(Integer.toString(id));
        if (!optionalSupplier.isEmpty())
            return true;
        return false;
    }


    //this func creates and adds new supplier to system. if impossible, throws InvalidParameterException.
    public void addNewSupplier(String name, String address, int id, String bankAcc, PaymentsWays payment, String contactName, String contactPhone, String contactEmail, LinkedList<String> categories, LinkedList<String> manufacturers, LinkedList<Day> days,int numDaysToDeliver)throws InvalidParameterException
    {

        SupplierCard supCard = new SupplierCard(name, address, id, bankAcc, payment, contactName, contactPhone, contactEmail);
        Supplier sup;
        if(days != null)//meaning the suppliers delivers regular
        {
            sup = new SupplierDeliversRegular(categories,manufacturers,supCard,null,days);
        }
        else if(numDaysToDeliver != -1)//meaning the suppliers delivers ERegular
        {
            sup = new SupplierDeliversERegular(categories,manufacturers,supCard,null,numDaysToDeliver);
        }
        else//meaning the suppliers not delivers
        {
            sup = new SupplierNotDelivers(categories,manufacturers,supCard,null);
        }
        SupplierContract supContract = new SupplierContract(payment,sup);
        sup.setSupplierContract(supContract);
        //ADD TO DB
        this.supplierDataMapper.insert(sup);
    }

    //this func creates and adds new branch to system.
    public void addNewBranch(int id, String address)
    {

//        Branch newB = new Branch(address);
//        SuperLi.src.Presentation.MainMenu.allBranches.add(newB);
        //MAYBE NEED TO THROW AN EXCEPTION.
    }

    //this func returns all orders in system.
    public LinkedList<Order> getAllOrdersInSystem()
    {
        return null;
    }

    //this func returns all suppliers in system.
    public LinkedList<Supplier> getAllSuppliersInSystem()
    {
        return null;
    }

    //this func gets all the orders of specific branch.
    //throws InvalidParameterException if branch with given number doesn't exist.
    public LinkedList<Order> getAllOrdersOfBranch(int branchNumber)throws InvalidParameterException
    {
        return null;
    }

}
