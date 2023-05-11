package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Branch;
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
    public String deleteQuery(Branch branch){
        return "";
    }
    public String updateQuery(Branch branch){return String.format("UPDATE Branches SET Address = '%s' WHERE Id = %d", branch.getAddress(), branch.getId());}
	public String findQuery(String ...key){
        return String.format("SELECT * FROM Branches WHERE Id = '%s'", key[0]);
    }
    public String findAllQuery(){
        return "SELECT * FROM Branches";
    }
    public Branch findInIdentityMap(String ...key){
        return branchIdentityMap.get(Integer.valueOf(key[0]));
    }
    public Branch insertIdentityMap(ResultSet match) throws SQLException{
        if (match == null)
            return null;
        Branch branch = branchIdentityMap.get(match.getInt("Id"));
        if(branch != null)
            return branch;
        branch = new Branch(match.getString("Address"), match.getInt("Id"));
        branchIdentityMap.put(branch.getId(), branch);
        return branch;
    }
}