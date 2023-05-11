package SuperLi.src.tests;
import SuperLi.src.BusinessLogic.Branch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class BranchTest {
    private Branch branch;
    @BeforeEach
    void setUp() {
        branch = new Branch("Netanya", 1);
    }

    @AfterEach
    void tearDown() {
        branch = null;
    }
    @Test
    void testCheckBranch(){
        Assertions.assertEquals("Netanya", branch.getAddress());
        Assertions.assertEquals(1, branch.getId());
    }
    @Test
    void testCheckContains(){
        Assertions.assertTrue(branch.containsBarcode(1));
        Assertions.assertNotEquals(true, branch.containsBarcode(10000));
    }
}