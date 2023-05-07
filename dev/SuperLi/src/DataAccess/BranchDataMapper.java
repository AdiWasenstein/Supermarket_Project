package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Branch;

import java.util.*;

public class BranchDataMapper implements IDataMapper<Branch> {
    Map<Integer, Branch> branchIdentityMap;
    static BranchDataMapper branchDataMapper = null;
    public String insertQuery(Branch b){
        return String.format("INSERT INTO Branches(Id, Address) VALUES (%d, '%s')", b.getId(), b.getAddress());
    }
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
    public void insert(Branch object){
        Connection conn;
        Statement stmt;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:SuppliersStock.db");
            stmt = conn.createStatement();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Cannot open the database");
            return;
        }
        try {
            stmt.executeUpdate(insertQuery(object));
        }
        catch (SQLException e){
            System.out.println("Cannot insert the item");
            System.out.println(e.getMessage());
        }
        try{
            conn.close();
        }
        catch (SQLException e){
            System.out.println("Failed to close the connection");
        }
    }
    public void update(Branch object) {}
    public void delete(Branch object) {}
}
