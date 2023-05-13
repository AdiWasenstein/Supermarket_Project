package SuperLi.src.test.StockTests.SuperLi.src.tests;

import SuperLi.src.BusinessLogic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class SupplierTest {
    private SupplierNotDelivers supp;
    @BeforeEach
    void setUp(){
        LinkedList<String> categories = new LinkedList<>();
        categories.add("Dairy Products");
        categories.add("Milk");

        CatalogItem catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);

        SupplierItem supplierItem = new SupplierItem(40, "Milk 3%", "Tnuva", 22, 1,
                50, "Dairy Products", 1);

        LinkedList<String > suppCatagories = new LinkedList<>();
        suppCatagories.add("Dairy");

        LinkedList<String> suppManufact = new LinkedList<>();
        suppManufact.add("Tnuva");

        SupplierCard suppCard = new SupplierCard("adi", "add", 1, "123", PaymentsWays.direct, "tiltan", "0526207807", "tiltan@gmmail.com");
        supp = new SupplierNotDelivers(suppCatagories, suppManufact, suppCard,null);
        SupplierContract contract = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, supp);
        supp.setSupplierContract(contract);
        supp.addItem(supplierItem);

    }

    @AfterEach
    void tearDown()
    {
        supp = null;
    }



    @Test
    void addTests()
    {
        supp.addNewContact("dan", "0524560846", "dans@gmail.com");
        Assertions.assertTrue(supp.isContactExist("0526207807", supp.getContacts()));
        Assertions.assertTrue(supp.isContactExist("0524560846", supp.getContacts()));

        supp.AddNewCategory("Soap");
        LinkedList<String> catList = new LinkedList<>();
        catList.add("Dairy");
        catList.add("Soap");
        Assertions.assertArrayEquals(supp.getSupplierCatagories().toArray(), catList.toArray());

        supp.AddNewManufacturer("Lalin");
        LinkedList<String> manList = new LinkedList<>();
        manList.add("Tnuva");
        manList.add("Lalin");
        Assertions.assertArrayEquals(supp.getSupplierManufacturers().toArray(), manList.toArray());
    }

    @Test
    void canSupplyMarketItem()
    {
        Assertions.assertTrue(supp.canSupplyMarketItem(new Pair<>(1, 10)));
        Assertions.assertFalse(supp.canSupplyMarketItem(new Pair<>(1, 60)));
        Assertions.assertFalse(supp.canSupplyMarketItem(new Pair<>(2, 1)));
    }

    @Test
    void setTests()
    {
        PaymentsWays paymentsWays = PaymentsWays.plus30;
        supp.setPayment(paymentsWays);
        Assertions.assertEquals(paymentsWays.ordinal(), supp.getPaymentWay().ordinal());

        supp.setBankAccount("1234");
        Assertions.assertEquals("1234", supp.getBankAccount());

        supp.setAddress("Givaa st.");
        Assertions.assertEquals("Givaa st.", supp.getAddress());

    }

    @Test
    void toStringTest()
    {
        String suppString = "Supplier's id: 1 , supplier's name: adi \n";
        Assertions.assertEquals(supp.toString(), suppString);

    }

    @Test
    void daysTillArrives()
    {
        Assertions.assertEquals(0, supp.daysTillArrives(Day.friday));
    }

    @Test
    void itemGettersTest()
    {
        SupplierItem item = supp.getAllSuppItem().getFirst();
        Assertions.assertEquals(40, item.getCatalogNumber());
        Assertions.assertEquals("Milk 3%", item.getItemName());
        Assertions.assertEquals(22, item.getUnitPrice());
        Assertions.assertEquals(1, item.getUnitWeight());
        Assertions.assertEquals(50, item.getNumberOfUnits());
        Assertions.assertEquals("Dairy Products", item.getCatagory());
        Assertions.assertEquals("Tnuva", item.getManufacturer());

    }

    @Test
    void itemSettersTest()
    {
        SupplierItem item = supp.getAllSuppItem().getFirst();
        item.SetNumberOfUnits(60);
        Assertions.assertEquals(60, item.getNumberOfUnits());
        item.SetUnitPrice(40);
        Assertions.assertEquals(40, item.getUnitPrice());

    }



    //    public static void main(String[] args)
//    {
//        SupplierController s = new SupplierController();
////        for(int i=0;i<3;i++)
////        {
////            s.AddNewSupplierToSystem();
////        }
//        LinkedList<String> cat = new LinkedList<>();
//        cat.add("soap");
//        LinkedList<String> man = new LinkedList<>();
//        man.add("tnuva");
//        SuperLi.src.BusinessLogic.Contact c = new SuperLi.src.BusinessLogic.Contact("adi", "0526207807", "a@.com");
//        LinkedList<SuperLi.src.BusinessLogic.Contact> cons = new LinkedList<>();
//        cons.add(c);
//        SuperLi.src.BusinessLogic.SupplierCard cardAdi = new SuperLi.src.BusinessLogic.SupplierCard("adi", "add", 1, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//        SuperLi.src.BusinessLogic.SupplierCard cardTiltan = new SuperLi.src.BusinessLogic.SupplierCard("tiltan", "add", 2, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//        SuperLi.src.BusinessLogic.SupplierCard cardYoav = new SuperLi.src.BusinessLogic.SupplierCard("yoav", "add", 3, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers adi = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardAdi,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, adi);
//        adi.supplierContract = contract;
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers tiltan = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardTiltan,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract2 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, tiltan);
//        tiltan.supplierContract = contract2;
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers yoav = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardYoav,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract1 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, yoav);
//        yoav.supplierContract = contract1;
//
//        s.allSuppliers.add(adi);
//        s.allSuppliers.add(tiltan);
//        s.allSuppliers.add(yoav);
//
//        MarketItem s1 = new MarketItem(1, "milk", "tnuva");
//        MarketItem s2 = new MarketItem(2, "shoco", "tnuva");
//        MarketItem s3 = new MarketItem(3, "cheese", "tnuva");
//        MarketItem s4 = new MarketItem(4, "bread", "tnuva");
//        MarketItem s5 = new MarketItem(5, "bamba", "tnuva");
//        MarketItem s6 = new MarketItem(6, "bisli", "tnuva");
//
//
//        SupplierItem m1 = new SupplierItem(12, "cheese", "tnuva", 7, 10, 10, "soap", 3);
//        SupplierItem m2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
//        SupplierItem m3 = new SupplierItem(16, "bread", "tnuva", 10, 10, 10, "soap", 4);
//        SupplierItem m4 = new SupplierItem(16, "bamba", "tnuva", 10, 10, 10, "soap", 5);
//        SupplierItem m5 = new SupplierItem(16, "bisli", "tnuva", 10, 10, 10, "soap", 6);
//        SupplierItem m6 = new SupplierItem(16, "shoco", "tnuva", 5, 10, 10, "soap", 2);
//
//        SupplierItem n1 = new SupplierItem(12, "cheese", "tnuva", 10, 10, 10, "soap", 3);
//        SupplierItem n2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
//        SupplierItem n3 = new SupplierItem(16, "bread", "tnuva", 800, 10, 10, "soap", 4);
//        SupplierItem n4 = new SupplierItem(16, "bamba", "tnuva", 200, 10, 10, "soap", 5);
//        SupplierItem n5 = new SupplierItem(16, "bisli", "tnuva", 500, 10, 10, "soap", 6);
//        SupplierItem n6 = new SupplierItem(16, "shoco", "tnuva", 2, 10, 10, "soap", 2);
//
////        adi.addItem(n1);
//        adi.addItem(n2);
//        adi.addItem(n6);
////        adi.addItem(m2);
//
////        tiltan.addItem(m1);
//        tiltan.addItem(m2);
//        tiltan.addItem(m6);
//
//
////        tiltan.addItem(m6);
////        tiltan.addItem(m3);
////        tiltan.addItem(m4);
////        tiltan.addItem(m5);
////        tiltan.addItem(m2);
////        tiltan.addItem(n2);//tiltan
//
//
//        yoav.addItem(m1);
//        yoav.addItem(m2);
////        yoav.addItem(m6);
//
//
////        yoav.addItem(m2);
////        yoav.addItem(m6);
////        yoav.addItem(m1);
////        yoav.addItem(m3);
////        yoav.addItem(m5);
//
//
//
////        for(int j=0;j<10;j++)
////            s.addItem();
////        s.addNewContact();
//        SuperLi.src.BusinessLogic.OrderManagment o = new SuperLi.src.BusinessLogic.OrderManagment();
//        LinkedList<Pair<Integer,Integer>> items = new LinkedList<>();
//        Pair<Integer,Integer> p1 = Pair.create(1, 10);
//        Pair<Integer,Integer> p2 = Pair.create(2, 10);
//        Pair<Integer,Integer> p3 = Pair.create(3, 10);
//        Pair<Integer,Integer> p4 = Pair.create(4, 10);
//        Pair<Integer,Integer> p5 = Pair.create(5, 10);
//        Pair<Integer,Integer> p6 = Pair.create(6, 10);
//        items.add(p1);
//        items.add(p2);
//        items.add(p3);
////        items.add(p4);
////        items.add(p5);
////        items.add(p6);
////        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>>> resultList = new LinkedList<>();
//        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,Integer>> resultList = new LinkedList<>();
////        HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combination = new HashMap<>();
//        HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> combination = new HashMap<>();
////        o.combinationsForFullItems(s.allSuppliers,items,resultList,combination);
////        System.out.println(o.findCheappestCombination(resultList));
////        System.out.println(resultList);
//
//
////        s.AddNewSupplierToSystem();
//////        s.addItem();
////        o.combinationsForPartialItems(s.allSuppliers,1,26,resultList,combination);
////        System.out.println(resultList);
////        o.ITryMyBestOK(s.allSuppliers,1,31,combination,resultList);
////        System.out.println(resultList);
//        try {
//            System.out.println(o.startOrderProcess(items, s.allSuppliers));
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//    }


}
