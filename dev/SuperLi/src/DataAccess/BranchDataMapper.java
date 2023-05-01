package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Branch;

import java.util.*;

public class BranchDataMapper implements IDataMapper<Branch> {
    Map<Integer, Branch> branchIdentityMap;
    static BranchDataMapper branchDataMapper = null;
    private BranchDataMapper(){
        branchIdentityMap = new HashMap<>();
    }
    public static BranchDataMapper getInstance(){
        if(branchDataMapper == null)
            branchDataMapper = new BranchDataMapper();
        return branchDataMapper;
    }
    public Optional<Branch> find(String param){
        return Optional.empty();
    }
    public LinkedList<Branch> findAll(){return new LinkedList<>();}
    public void insert(Branch object) {}
    public void update(Branch object) {}
    public void delete(Branch object) {}
}
