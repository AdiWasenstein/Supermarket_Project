package SuperLi.src.test.StockTests.SuperLi.src.tests;


import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.CatalogItemDataMapper;
import SuperLi.src.DataAccess.OrderDataMapper;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import SuperLi.src.DataAccess.SupplierDataMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class OrderControllerTest {
      private OrderController orderController;
      private Supplier supp1, supp2;
      private HashMap<Integer,Integer> itemsForReport;
      private PeriodicReport report;
      private StockManagementFacade stockManagementFacade;

      @BeforeEach
      public void setUp()
      {
            orderController = OrderController.getInstance();

            // creating 2 suppliers
            LinkedList<String> categories = new LinkedList<>();
            categories.add("Dairy Products");
            categories.add("Milk");

            CatalogItem catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                    new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);
            CatalogItemDataMapper.getInstance().insert(catalogItem);

            SupplierItem supplierItem1 = new SupplierItem(40, "Milk 3%", "Tnuva", 22, 1,
                    50, "Dairy Products", 1);

            SupplierItem supplierItem2 = new SupplierItem(540, "Milk 3%", "Tnuva", 14, 1,
                    35, "Dairy Products", 1);

            LinkedList<String > suppCatagories = new LinkedList<>();
            suppCatagories.add("Dairy");

            LinkedList<String> suppManufact = new LinkedList<>();
            suppManufact.add("Tnuva");

            SupplierCard suppCard1 = new SupplierCard("adi", "add", 204, "123", PaymentsWays.direct, "tiltan", "0526207807", "tiltan@gmail.com");
            supp1 = new SupplierNotDelivers(suppCatagories, suppManufact, suppCard1,null);
            SupplierContract contract1 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, supp1);
            supp1.setSupplierContract(contract1);
            supp1.addItem(supplierItem1);

            SupplierCard suppCard2 = new SupplierCard("yoav", "add2", 205, "1234", PaymentsWays.plus30, "tiltan", "0526207807", "tiltan@gmmail.com");
            supp2 = new SupplierNotDelivers(suppCatagories, suppManufact, suppCard2,null);
            SupplierContract contract2 = new SuperLi.src.BusinessLogic.SupplierContract(PaymentsWays.plus30, supp2);
            supp2.setSupplierContract(contract2);
            supp2.addItem(supplierItem2);


            SupplierDataMapper.getInstance().insert(supp1);
            SupplierDataMapper.getInstance().insert(supp2);


            HashMap<SupplierItem, Integer> items = new LinkedHashMap<>();
            items.put(supplierItem1, 10);
            items.put(supplierItem2, 20);

            itemsForReport = new HashMap<>();
            itemsForReport.put(1, 10);

      }

      @Test
      public void createPeriodic()
      {
            LocalDate date = LocalDate.now();
            DayOfWeek currentDayOfWeek = date.getDayOfWeek();
            String dayOfWeekString = currentDayOfWeek.toString();
//            report = orderController.createNewPeriodicReport(24,  supp1,Day.valueOf(dayOfWeekString), itemsForReport);
            Assertions.assertNotNull(PeriodicReportDataMapper.getInstance().findAll());
//            Assertions.assertEquals(orderController.getAllPeriodicReports().size(), PeriodicReport.howManyReports());
            Assertions.assertNotNull(OrderDataMapper.getInstance().findAll());
      }

      @Test
      public void updateReport()
      {
            HashMap<Integer, Integer> updates = new HashMap<>();
            updates.put(1, 20);
            orderController.updateReport(report.getReportId(), updates);
            Assertions.assertEquals(20, report.getQuantityOfItem(supp1.getAllSuppItem().getFirst()));
            updates.put(1, 100);
            orderController.updateReport(report.getReportId(), updates);
            Assertions.assertNotEquals(100, report.getQuantityOfItem(supp1.getAllSuppItem().getFirst()));
      }

      @Test
      void testCreateShortageOrder(){
            Assertions.assertTrue(stockManagementFacade.createShortageOrder(1));
            Assertions.assertFalse(stockManagementFacade.createShortageOrder(999));
      }
}