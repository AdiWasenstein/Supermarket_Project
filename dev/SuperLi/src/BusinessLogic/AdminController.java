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
//        for(int i = 0; i< SuperLi.src.Presentation.MainMenu.allSuppliers.size(); i++)
//        {
//            if(SuperLi.src.Presentation.MainMenu.allSuppliers.get(i).getSupplierId() == id)
//                return true;
//        }
        return false;
    }

    //this func creates new supplier card and returns it. else, throws InvalidParameterException.
    private SupplierCard createNewSupplierCard(String name, String address, int id, String bankAcc, PaymentsWays payment, String contactName, String contactPhone, String contactEmail)throws InvalidParameterException//CHANGE!
    {
//        SupplierCard supCard = new SupplierCard(name, address, id, bankAcc, payment, contactName, contactPhone, contactEmail);
        return null;
    }

    //this func creates new supplier contract and returns it.
    private SupplierContract createNewSupplierContract(PaymentsWays payment, Supplier sup)
    {
//        SupplierContract supContract = new SupplierContract(payment,sup);
//        sup.setSupplierContract(supContract);
        return null;
    }

    //this func creates and returns new DeliversRegularSupplier.
    private Supplier createNewDeliversRegularSupplier(LinkedList<String> categories, LinkedList<String> manufacturers, SupplierCard supCard, LinkedList<Day> days)
    {
//        sup = new SuperLi.src.BusinessLogic.SupplierDeliversRegular(categories,manufacturers,supCard,null,days);
        return null;
    }

    //this func creates and returns new DeliversRegularSupplier.
    private Supplier createNewDeliversERegularSupplier(LinkedList<String> categories, LinkedList<String> manufacturers, SupplierCard supCard, int numDaysToDeliver)
    {
//        sup = new SuperLi.src.BusinessLogic.SupplierDeliversERegular(categories,manufacturers,supCard,null,numDaysToDeliver);
        return null;
    }

    //this func creates and returns new SuperLi.src.BusinessLogic.SupplierNotDelivers.
    private Supplier createNewSupplierNotDelivers(LinkedList<String> categories, LinkedList<String> manufacturers, SupplierCard supCard)
    {
//        sup = new SuperLi.src.BusinessLogic.SupplierNotDelivers(categories,manufacturers,supCard,null);
        return null;
    }

    //this func creates and adds new supplier to system. if impossible, throws InvalidParameterException.
    public void addNewSupplier(String name, String address, int id, String bankAcc, PaymentsWays payment, String contactName, String contactPhone, String contactEmail, LinkedList<String> categories, LinkedList<String> manufacturers, SupplierCard supCard, LinkedList<Day> days,int numDaysToDeliver)throws InvalidParameterException
    {
//        SupplierCard supCard = createNewSupplierCard(name, address, id, bankAcc, payment, contactName, contactPhone, contactEmail);
        Supplier sup;
        if(days != null)
        {
            sup = createNewDeliversRegularSupplier(categories,manufacturers,supCard,days);
        }
        else if(numDaysToDeliver != -1)
        {
            sup = createNewDeliversERegularSupplier(categories,manufacturers,supCard,numDaysToDeliver);
        }
        else
        {
            sup = createNewSupplierNotDelivers(categories,manufacturers,supCard);
        }
        SupplierContract supContract = createNewSupplierContract(payment,sup);
        //ADD TO DB
    }

    //this func creates and adds new branch to system.
    public void addNewBranch(String address)
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
