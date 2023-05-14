package SuperLi.src.test.StockTests.SuperLi.src.tests;

import SuperLi.src.BusinessLogic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PeriodicReportTest {
    private Supplier supp;

    private PeriodicReport report;

    @BeforeEach
    void setUp()
    {
        LinkedList<String> categories = new LinkedList<>();
        categories.add("Dairy Products");
        categories.add("Milk");

//        CatalogItem catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
//                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);

        SupplierItem supplierItem1 = new SupplierItem(40, "Milk 3%", "Tnuva", 22, 1,
                50, "Dairy Products", 1);
        SupplierItem supplierItem2 = new SupplierItem(50, "Koteg 5%", "Tnuva", 16, 1,
                60, "Dairy Products", 2);
        LinkedList<String > suppCatagories = new LinkedList<>();
        suppCatagories.add("Dairy");

        LinkedList<String> suppManufact = new LinkedList<>();
        suppManufact.add("Tnuva");

        SupplierCard suppCard = new SupplierCard("adi", "add", 1, "123", PaymentsWays.direct, "tiltan", "0526207807", "tiltan@gmmail.com");
        supp = new SupplierNotDelivers(suppCatagories, suppCatagories, suppCard,null);
        SupplierContract contract = new SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, supp);
        supp.setSupplierContract(contract);
        supp.addItem(supplierItem1);
        supp.addItem(supplierItem2);

        Branch b = new Branch("lool", 24);

        HashMap<SupplierItem, Integer> items = new LinkedHashMap<>();
        items.put(supplierItem1, 10);
        items.put(supplierItem2, 20);

        report = new PeriodicReport(24, Day.monday, supp, items, 1);
    }

    @AfterEach
    void tearDown()
    {
        supp = null;
        report = null;

    }

    @Test
    void gettersTest()
    {
        Assertions.assertEquals(1, report.getReportId());
        Assertions.assertEquals(24, report.getBranchNumber());
        Assertions.assertEquals(Day.monday.ordinal(), report.getDayToOrder().ordinal());
        Assertions.assertEquals(supp,report.getSupplier());
        Assertions.assertEquals(supp.getAllSuppItem().toString(), report.getSupplierItems().toString());
    }

    @Test
    void setQuantity()
    {
        SupplierItem item = supp.getSupplierContract().getSupplierItemByCatalogNum(1);
        report.setQuantityOfItem(1, 20);
        Assertions.assertEquals(20, report.getQuantityOfItem(item));
        report.setQuantityOfItem(1, 80);
        Assertions.assertNotEquals(80, report.getQuantityOfItem(item));

    }



}
