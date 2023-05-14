package SuperLi.src.test.StockTests.SuperLi.src.tests;
import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.StockManagementFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class StockManagementFacadeTest {
    private final StockManagementFacade stockManagementFacade = StockManagementFacade.getInstance();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final LinkedList<String> categories = new LinkedList<>();
    @BeforeEach
    void beforeEach() {
        categories.add("Dairy Products");
        categories.add("Milk");
    }
    @Test
    void testAddBranch(){
        String address = generateRandomString();
        assertTrue(stockManagementFacade.addBranch(address));
    }
    @Test
    void testGetCatalogItem(){
        CatalogItem catalogItem = stockManagementFacade.getCatalogItem(1);
        assertNull(catalogItem);
        catalogItem = stockManagementFacade.getCatalogItem(10);
        assertEquals(10, catalogItem.getId());
    }
    @Test
    void testGetBranchAddress(){
        assertEquals("", stockManagementFacade.getBranchAddress(100));
        assertEquals("Rishon LeZion", stockManagementFacade.getBranchAddress(1));
    }
    @Test
    void testAddCatalogItem(){
        assertFalse(stockManagementFacade.addCatalogItem(10, "Shoko", "Tnuva", 2.4, 250, categories, 250, 3, 10));
    }
    @Test
    void testDeleteCatalogItem(){
        assertFalse(stockManagementFacade.removeCatalogItem(1));
    }
    @Test
    void testAddStockItem(){
        assertFalse(stockManagementFacade.addStockItem(10, 2, 1, 1.75, LocalDate.of(2023, 7,14), 2, true));
    }
    @Test
    void testDeleteStockItem(){
        assertFalse(stockManagementFacade.removeStockItem(1, 4));
    }
    @Test
    void testReports(){
        LinkedList<LinkedList<String>> categories = new LinkedList<>();
        assertTrue(stockManagementFacade.generateRequiredStockReport(4).getReportData().size() > 0);
        assertNotNull(stockManagementFacade.generateStockItemsReport(1));
        assertEquals(0, stockManagementFacade.generateDamagedStockReport(4).initializeRecords().size());
        assertTrue(stockManagementFacade.generateDamagedStockReport(1).initializeRecords().size() > 0);
        assertTrue(stockManagementFacade.generateAllCatalogReport().initializeRecords().size() > 0);
        assertEquals(0, stockManagementFacade.generateCategoryReport(categories).initializeRecords().size());
    }

    public static String generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(10);
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}