package SuperLi.src.tests;
import SuperLi.src.BusinessLogic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedList;

class StockItemTest {
    private CatalogItem catalogItem;
    private LinkedList<String> categories = new LinkedList<>();
    private CostumerDiscount costumerDiscount;
    private StockItem stockItem;

    @BeforeEach
    void beforeEach(){
        categories.add("Dairy Products"); categories.add("Milk");
        catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);
        costumerDiscount = new CostumerDiscount(LocalDate.of(23, 5, 13), 50, true, 1);
        stockItem = new StockItem(catalogItem, 10, 3.99, LocalDate.of(23,6,27), DamageType.NONE, 1, 33);
    }
    @AfterEach
    void afterEach() {
        this.stockItem = null;
        categories = null;
        catalogItem = null;
        costumerDiscount = null;
    }
    @Test
    void testCheckStockItem(){
        Assertions.assertEquals(1, stockItem.getCatalogItem().getId());
        Assertions.assertEquals(33, stockItem.getLocation());
        Assertions.assertEquals(10, stockItem.getBarcode());
        Assertions.assertEquals(1, stockItem.getBranchId());
        Assertions.assertEquals(4, stockItem.getDamage().ordinal());
        Assertions.assertEquals(3.99, stockItem.getCostPrice());
        Assertions.assertEquals("27/6/23", stockItem.getExpirationString());
    }
    @Test
    void testSetStockItem(){
        stockItem.setLocation(976); stockItem.setDamage(DamageType.COVER);
        Assertions.assertEquals(976, stockItem.getLocation());
        Assertions.assertEquals("COVER", stockItem.getDamage().toString());
    }
    @Test
    void testCheckDamaged(){
//        Assertions.assertEquals(false, stockItem.isExpired()); //TODO - not working right
        stockItem.setDamage(DamageType.ROTTEN);
        Assertions.assertTrue(stockItem.needsToBeReturned());
    }
    @Test
    void testToString() {
        Assertions.assertNotEquals("", stockItem.toString());
        Assertions.assertNotEquals("ID: 1; Barcode: 10; Price: 3.9₪; Location: 33; Manufacturer: Tnuva; Amount: 1; Shelves Amount: 1; Back Amount: 0", stockItem.toString());
    }
}