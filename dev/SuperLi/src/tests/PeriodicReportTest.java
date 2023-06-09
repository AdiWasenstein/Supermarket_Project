package SuperLi.src.tests;

import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.PeriodicReportDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeriodicReportTest {
    private Supplier supp;
    private PeriodicReport report;

    @BeforeEach
    void setUp() {
        report = PeriodicReportDataMapper.getInstance().find(new String[]{"27"}).get();
        supp = report.getSupplier();
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
        Assertions.assertEquals(27, report.getReportId());
        Assertions.assertEquals(1, report.getBranchNumber());
        Assertions.assertEquals(Day.friday.ordinal(), report.getDayToOrder().ordinal());

    }
    @Test
    void setQuantity()
    {
        SupplierItem item = supp.getSupplierContract().getSupplierItemByCatalogNum(1);
        report.setQuantityOfItem(12, 200);
        Assertions.assertEquals(200, report.getQuantityOfItem(item));
        // supplier can supply only 550
        report.setQuantityOfItem(1, 800);
        Assertions.assertNotEquals(800, report.getQuantityOfItem(item));

    }
}
