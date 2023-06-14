package tests;

import BusinessLogic.*;
import DataAccess.CatalogItemDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class CatalogItemTest {
    private CatalogItem catalogItem;
    private LinkedList<String> categories = new LinkedList<>();
    private CostumerDiscount costumerDiscount;
    private final CatalogItemDataMapper catalogItemDataMapper = CatalogItemDataMapper.getInstance();
    @BeforeEach
    void beforeEach() {
        categories.add("Dairy Products");
        categories.add("Milk");
        costumerDiscount = new CostumerDiscount(LocalDate.of(2023, 5, 27), 50, true, 1);
        catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);
    }
    @AfterEach
    void AfterEach() {
        categories = null;
        catalogItem = null;
        costumerDiscount = null;
    }
    @Test
    void testCheckCatalog() {
        assertEquals(1, catalogItem.getId());
        assertEquals("Milk 3%", catalogItem.getName());
        assertEquals("Tnuva", catalogItem.getManufacturer());
        assertEquals(7.8, catalogItem.getSellPrice());
        assertEquals(100, catalogItem.getMinCapacity());
        assertEquals(334, catalogItem.getShelvesLocation());
        assertEquals(1377, catalogItem.getBackLocation());
        assertEquals(7, catalogItem.getShelfLife());
        assertEquals(1000, catalogItem.getCategory().getSizeAmount());
        assertEquals(1, catalogItem.getCategory().getMeasureUnit().ordinal());
        assertEquals(categories, catalogItem.getCategory().getCategories());
    }
    @Test
    void testSetCatalog() {
        catalogItem.setMinCapacity(30);
        catalogItem.setSellPrice(1.33);
        catalogItem.setCostumerDiscount(costumerDiscount);
        assertEquals(30, catalogItem.getMinCapacity());
        assertEquals(1.33, catalogItem.getSellPrice());
        assertEquals(costumerDiscount, catalogItem.getCostumerDiscount());
    }
    @Test
    void testCheckCategory() {
        LinkedList<String> names = new LinkedList<>();
        names.add("Bread");
        Category category = new Category(names, new Size(10, MeasureUnit.UNIT));
        Assertions.assertNotEquals(category, catalogItem.getCategory());
        assertEquals(this.categories, catalogItem.getCategory().getCategories());
    }
    @Test
    void testToString() {
        Assertions.assertNotEquals("", catalogItem.toString());
        assertEquals("ID: 1; Dairy Products, Milk, 1000.0 MLs; Milk 3%; Manufacturer: Tnuva; 7.8ILS; Min Capacity: 100; Shelf Life: 7; CostumerDiscount: ", catalogItem.toString());
    }
    @Test
    void testInsertDb(){
        assertNull(catalogItemDataMapper.findInIdentityMap("1"));
        catalogItemDataMapper.insert(catalogItem);
        assertEquals(catalogItem, catalogItemDataMapper.findInIdentityMap("1"));
        assertEquals(catalogItem, catalogItemDataMapper.find("1").get());
    }
    @Test
    void testDeleteDb(){
        catalogItemDataMapper.delete(catalogItem);
        assertNull((catalogItemDataMapper.findInIdentityMap("1")));
        assertFalse(catalogItemDataMapper.find("1").isPresent());
    }
    @Test
    void testFindDB(){
        CatalogItem catalogItem1 = catalogItemDataMapper.find("20").get();
        categories.clear(); categories.add("Breads"); categories.add("Pitas");
        assertNotNull(catalogItem1);
        assertEquals(20, catalogItem1.getId());
        assertEquals("Pita", catalogItem1.getName());
        assertEquals("Angle", catalogItem1.getManufacturer());
        assertEquals(11, catalogItem1.getSellPrice());
        assertEquals(50, catalogItem1.getMinCapacity());
        assertEquals(440, catalogItem1.getShelvesLocation());
        assertEquals(1455, catalogItem1.getBackLocation());
        assertEquals(7, catalogItem1.getShelfLife());
        assertEquals(10, catalogItem1.getCategory().getSizeAmount());
        assertEquals(0, catalogItem1.getCategory().getMeasureUnit().ordinal());
        assertEquals(categories, catalogItem1.getCategory().getCategories());
    }
    @Test
    void testFindAllDB(){
        LinkedList<CatalogItem> catalogItems = catalogItemDataMapper.findAll();
        assertEquals(25       , catalogItems.size());
    }
}