package SuperLi.src.tests;

import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.StockItemDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class StockItemTest {
    private CatalogItem catalogItem;
    private LinkedList<String> categories = new LinkedList<>();
    private CostumerDiscount costumerDiscount;
    private StockItem stockItem;
    private final StockItemDataMapper stockItemDataMapper= StockItemDataMapper.getInstance();

    @BeforeEach
    void beforeEach(){
        categories.add("Dairy Products"); categories.add("Milk");
        catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);
        costumerDiscount = new CostumerDiscount(LocalDate.of(23, 5, 13), 50, true, 1);
        stockItem = new StockItem(catalogItem, 1000, 3.99, LocalDate.of(2023,6,27), DamageType.NONE, 1, 33);
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
        Assertions.assertEquals(1000, stockItem.getBarcode());
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
        Assertions.assertFalse(stockItem.isExpired());
        stockItem.setDamage(DamageType.ROTTEN);
        Assertions.assertTrue(stockItem.needsToBeReturned());
    }
    @Test
    void testToString() {
        Assertions.assertNotEquals("", stockItem.toString());
        Assertions.assertNotEquals("ID: 1; Barcode: 10; Price: 3.9₪; Location: 33; Manufacturer: Tnuva; Amount: 1; Shelves Amount: 1; Back Amount: 0", stockItem.toString());
    }
    @Test
    void testInsertDb(){
        assertNull(stockItemDataMapper.findInIdentityMap("10000"));
        stockItemDataMapper.insert(stockItem);
        assertEquals(stockItem, stockItemDataMapper.findInIdentityMap("1000"));
        assertEquals(stockItem, stockItemDataMapper.find("1000").get());
    }
    @Test
    void testDeleteDb(){
        stockItemDataMapper.delete(stockItem);
        assertNull((stockItemDataMapper.findInIdentityMap("1000")));
        assertFalse(stockItemDataMapper.find("1000").isPresent());
    }
    @Test
    void testFindDB(){
        StockItem stockItem1 = stockItemDataMapper.find("1").get();
        assertNotNull(stockItem1);
        Assertions.assertEquals(10, stockItem1.getCatalogItem().getId());
        Assertions.assertEquals(122, stockItem1.getLocation());
        Assertions.assertEquals(1, stockItem1.getBarcode());
        Assertions.assertEquals(1, stockItem1.getBranchId());
        Assertions.assertEquals(0, stockItem1.getDamage().ordinal());
        Assertions.assertEquals(3, stockItem1.getCostPrice());
        Assertions.assertEquals("11/4/23", stockItem1.getExpirationString());
    }
    @Test
    void testFindAllDB(){
        LinkedList<StockItem> stockItems = stockItemDataMapper.findAll();
        assertEquals(98, stockItems.size());
    }
}