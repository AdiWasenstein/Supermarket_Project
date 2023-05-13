package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.BranchDataMapper;
import SuperLi.src.DataAccess.OrderDataMapper;
import SuperLi.src.DataAccess.SupplierDataMapper;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Optional;

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

//    //this func creates and adds new branch to system.
//    public void addNewBranch(String address, int id )
//    {
//        if (id < 0 || address.equals(""))
//            return;
//        Branch newB = new Branch(address, id);
//        BranchDataMapper.getInstance().insert(newB);
////        Branch newB = new Branch(address);
////        SuperLi.src.Presentation.MainMenu.allBranches.add(newB);
//        //MAYBE NEED TO THROW AN EXCEPTION.
//    }

    //this func returns all orders in system.
    public LinkedList<Order> getAllOrdersInSystem()
    {
       return OrderDataMapper.getInstance().findAll();
    }

    //this func returns all suppliers in system.
    public LinkedList<Supplier> getAllSuppliersInSystem()
    {
        return SupplierDataMapper.getInstance().findAll();
    }

    //this func gets all the orders of specific branch.
    //throws InvalidParameterException if branch with given number doesn't exist.
    public LinkedList<Order> getAllOrdersOfBranch(int branchNumber)throws InvalidParameterException
    {
        return OrderDataMapper.getInstance().findAllByBranch(branchNumber);
    }

}


////    public static void main(String[] args)
////    {
//// SupplierController s = new SupplierController();
//////        for(int i=0;i<3;i++)
//////        {
//////            s.AddNewSupplierToSystem();
//////        }
////        LinkedList<String> cat = new LinkedList<>();
////        cat.add("soap");
////        LinkedList<String> man = new LinkedList<>();
////        man.add("tnuva");
////        SuperLi.src.BusinessLogic.Contact c = new SuperLi.src.BusinessLogic.Contact("adi", "0526207807", "a@.com");
////        LinkedList<SuperLi.src.BusinessLogic.Contact> cons = new LinkedList<>();
////        cons.add(c);
////        SuperLi.src.BusinessLogic.SupplierCard cardAdi = new SuperLi.src.BusinessLogic.SupplierCard("adi", "add", 1, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
////        SuperLi.src.BusinessLogic.SupplierCard cardTiltan = new SuperLi.src.BusinessLogic.SupplierCard("tiltan", "add", 2, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
////        SuperLi.src.BusinessLogic.SupplierCard cardYoav = new SuperLi.src.BusinessLogic.SupplierCard("yoav", "add", 3, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
////
////        SuperLi.src.BusinessLogic.SupplierNotDelivers adi = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardAdi,null);
////        SuperLi.src.BusinessLogic.SupplierContract contract = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, adi);
////        adi.supplierContract = contract;
////
////        SuperLi.src.BusinessLogic.SupplierNotDelivers tiltan = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardTiltan,null);
////        SuperLi.src.BusinessLogic.SupplierContract contract2 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, tiltan);
////        tiltan.supplierContract = contract2;
////
////        SuperLi.src.BusinessLogic.SupplierNotDelivers yoav = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardYoav,null);
////        SuperLi.src.BusinessLogic.SupplierContract contract1 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, yoav);
////        yoav.supplierContract = contract1;
////
////        s.allSuppliers.add(adi);
////        s.allSuppliers.add(tiltan);
////        s.allSuppliers.add(yoav);
////
////        MarketItem s1 = new MarketItem(1, "milk", "tnuva");
////        MarketItem s2 = new MarketItem(2, "shoco", "tnuva");
////        MarketItem s3 = new MarketItem(3, "cheese", "tnuva");
////        MarketItem s4 = new MarketItem(4, "bread", "tnuva");
////        MarketItem s5 = new MarketItem(5, "bamba", "tnuva");
////        MarketItem s6 = new MarketItem(6, "bisli", "tnuva");
////
////
////        SupplierItem m1 = new SupplierItem(12, "cheese", "tnuva", 7, 10, 10, "soap", 3);
////        SupplierItem m2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
////        SupplierItem m3 = new SupplierItem(16, "bread", "tnuva", 10, 10, 10, "soap", 4);
////        SupplierItem m4 = new SupplierItem(16, "bamba", "tnuva", 10, 10, 10, "soap", 5);
////        SupplierItem m5 = new SupplierItem(16, "bisli", "tnuva", 10, 10, 10, "soap", 6);
////        SupplierItem m6 = new SupplierItem(16, "shoco", "tnuva", 5, 10, 10, "soap", 2);
////
////        SupplierItem n1 = new SupplierItem(12, "cheese", "tnuva", 10, 10, 10, "soap", 3);
////        SupplierItem n2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
////        SupplierItem n3 = new SupplierItem(16, "bread", "tnuva", 800, 10, 10, "soap", 4);
////        SupplierItem n4 = new SupplierItem(16, "bamba", "tnuva", 200, 10, 10, "soap", 5);
////        SupplierItem n5 = new SupplierItem(16, "bisli", "tnuva", 500, 10, 10, "soap", 6);
////        SupplierItem n6 = new SupplierItem(16, "shoco", "tnuva", 2, 10, 10, "soap", 2);
////
//////        adi.addItem(n1);
////        adi.addItem(n2);
////        adi.addItem(n6);
//////        adi.addItem(m2);
////
//////        tiltan.addItem(m1);
////        tiltan.addItem(m2);
////        tiltan.addItem(m6);
////
////
//////        tiltan.addItem(m6);
//////        tiltan.addItem(m3);
//////        tiltan.addItem(m4);
//////        tiltan.addItem(m5);
//////        tiltan.addItem(m2);
//////        tiltan.addItem(n2);//tiltan
////
////
////        yoav.addItem(m1);
////        yoav.addItem(m2);
//////        yoav.addItem(m6);
////
////
//////        yoav.addItem(m2);
//////        yoav.addItem(m6);
//////        yoav.addItem(m1);
//////        yoav.addItem(m3);
//////        yoav.addItem(m5);
////
////
////
//////        for(int j=0;j<10;j++)
//////            s.addItem();
//////        s.addNewContact();
////        SuperLi.src.BusinessLogic.OrderManagment o = new SuperLi.src.BusinessLogic.OrderManagment();
////        LinkedList<Pair<Integer,Integer>> items = new LinkedList<>();
////        Pair<Integer,Integer> p1 = Pair.create(1, 10);
////        Pair<Integer,Integer> p2 = Pair.create(2, 10);
////        Pair<Integer,Integer> p3 = Pair.create(3, 10);
////        Pair<Integer,Integer> p4 = Pair.create(4, 10);
////        Pair<Integer,Integer> p5 = Pair.create(5, 10);
////        Pair<Integer,Integer> p6 = Pair.create(6, 10);
////        items.add(p1);
////        items.add(p2);
////        items.add(p3);
//////        items.add(p4);
//////        items.add(p5);
//////        items.add(p6);
//////        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>>> resultList = new LinkedList<>();
////        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,Integer>> resultList = new LinkedList<>();
//////        HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combination = new HashMap<>();
////        HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> combination = new HashMap<>();
//////        o.combinationsForFullItems(s.allSuppliers,items,resultList,combination);
//////        System.out.println(o.findCheappestCombination(resultList));
//////        System.out.println(resultList);
////
////
//////        s.AddNewSupplierToSystem();
////////        s.addItem();
//////        o.combinationsForPartialItems(s.allSuppliers,1,26,resultList,combination);
//////        System.out.println(resultList);
//////        o.ITryMyBestOK(s.allSuppliers,1,31,combination,resultList);
//////        System.out.println(resultList);
////        try {
////            System.out.println(o.startOrderProcess(items, s.allSuppliers));
////        }
////        catch (Exception e)
////        {
////            System.out.println(e.getMessage());
////        }
////    }
//
//
//}
