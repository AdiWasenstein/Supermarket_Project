package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Branch;
import com.sun.source.tree.BreakTree;

import java.sql.*;
import java.util.*;

public class BranchDataMapper extends ADataMapper<Branch> {
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
    public String insertQuery(Branch branch){
        branchIdentityMap.put(branch.getId(), branch);
        return String.format("INSERT INTO Branches(Id, Address) VALUES (%d, '%s')", branch.getId(), branch.getAddress());
    }
    public String findQuery(String key){
        return String.format("SELECT * FROM Branches WHERE id = '%s'", key);
    }
    public String findAllQuery(){
        return "SELECT * FROM Branches";
    }
    public String deleteQuery(Branch branch){
        branchIdentityMap.remove(branch.getId());
        return String.format("DELETE FROM Branches WHERE id = '%s'", branch.getId());
    }
    public String updateQuery(Branch branch){
        return String.format("UPDATE Branches SET Address = '%s' WHERE ID = '%s'", branch.getAddress(), branch.getId());
    }
	public Optional<Branch> find(String key){
		//TODO - Convert String to Integer
		Integer id;
		if branchIdentityMap.contains(id);
			return Optional.of(branchIdentityMap.get(id));
	}
}
