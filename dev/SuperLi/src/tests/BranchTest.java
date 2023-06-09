package SuperLi.src.tests;


import SuperLi.src.BusinessLogic.Branch;
import SuperLi.src.DataAccess.BranchDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BranchTest {
    private Branch branch;
    private final BranchDataMapper branchDataMapper = BranchDataMapper.getInstance();
    @BeforeEach
    void setUp() {
        branch = new Branch("Eilat", 4);
    }

    @AfterEach
    void tearDown() {
        branch = null;
    }
    @Test
    void testCheckBranch(){
        Assertions.assertEquals("Eilat", branch.getAddress());
        Assertions.assertEquals(4, branch.getId());
    }
    @Test
    void testCheckContains(){
        Assertions.assertFalse(branch.containsBarcode(4));
        Assertions.assertNotEquals(true, branch.containsBarcode(10000));
    }
    @Test
    void testFindDB(){
        Branch branch1 = branchDataMapper.find("2").get();
        assertNotNull(branch1);
        Assertions.assertEquals("Beer Sheva", branch1.getAddress());
        Assertions.assertEquals(2, branch1.getId());
    }
    @Test
    void testInsertDb(){
        branchDataMapper.insert(branch);
        assertEquals(branch, branchDataMapper.findInIdentityMap("4"));
        assertEquals(branch, branchDataMapper.find("4").get());
    }
    @Test
    void testFindAllDB(){
        LinkedList<Branch> branches = branchDataMapper.findAll();
        assertEquals(4, branches.size());
    }
}