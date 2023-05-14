package SuperLi.src.test.StockTests.SuperLi.src.tests;


import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.OrderDataMapper;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import SuperLi.src.DataAccess.SupplierDataMapper;
import SuperLi.src.Presentation.ReportViewer;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class OrderControllerTest {
      private static OrderController orderController;
      private static Supplier supp1;
      private static HashMap<Integer,Integer> itemsForReport;
      private PeriodicReport report;
      private static SupplierDataMapper supplierDataMapper;
      private StockManagementFacade stockManagementFacade;
      @BeforeEach
      void setUp()
      {
            orderController = OrderController.getInstance();
            supplierDataMapper = SupplierDataMapper.getInstance();
            stockManagementFacade = StockManagementFacade.getInstance();

            supp1 = supplierDataMapper.find(new String[]{"1"}).get();
            HashMap<SupplierItem, Integer> items = new LinkedHashMap<>();
            SupplierItem item1 = supp1.getSupplierItemAccordingToCatalogNumber(1);
            items.put(item1, 10);
            ReportViewer.getInstance().generateAllCatalogReport();
            itemsForReport = new HashMap<>();
            itemsForReport.put(12, 10);
      }

      @Test
      void createPeriodic()
      {
            report = orderController.createNewPeriodicReport(27, 1, supp1, Day.valueOf("friday"), itemsForReport);
            Assertions.assertNotNull(PeriodicReportDataMapper.getInstance().findAll());
            Assertions.assertFalse(PeriodicReportDataMapper.getInstance().find(new String[]{"27"}).isEmpty());
            Assertions.assertNotNull(OrderDataMapper.getInstance().findAll());
      }

      @Test
      public void updateReport()
      {
            HashMap<Integer, Integer> updates = new HashMap<>();
            report = PeriodicReportDataMapper.getInstance().find(new String[]{"27"}).get();
            try {
                   Assertions.assertTrue(stockManagementFacade.updatePeriodReport(27, 1));

            }
            catch (Exception e)
            {
                  Assertions.fail();
            }
            updates.put(1, 100);
            orderController.updateReport(report.getReportId(), updates);
            Assertions.assertNotEquals(100, report.getQuantityOfItem(supp1.getSupplierItemAccordingToCatalogNumber(1)));
      }

      @Test
      void testCreateShortageOrder(){
            Assertions.assertTrue(stockManagementFacade.createShortageOrder(1));
            Assertions.assertFalse(stockManagementFacade.createShortageOrder(999));
      }

      @Test
      void testShortageReport(){
            OrderManagment orderManagment = OrderManagment.getInstance();
            RequiredStockReport requiredStockReport = stockManagementFacade.generateRequiredStockReport(1);
            LinkedList<Supplier> suppliers = SupplierDataMapper.getInstance().findAll();
            try {
                  Assertions.assertNotNull(orderManagment.creatMissingOrder(requiredStockReport.getReportData(), 1, suppliers));
            } catch (Exception e) {
                  throw new RuntimeException(e);
            }
      }
}