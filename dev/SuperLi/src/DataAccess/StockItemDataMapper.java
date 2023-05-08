package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.StockItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StockItemDataMapper extends ADataMapper<StockItem> {
    Map<Integer, StockItem> stockItemsIdentitiyMap;
    static StockItemDataMapper stockItemDataMapper = null;
    private StockItemDataMapper(){
        stockItemsIdentitiyMap = new HashMap<>();
    }
    public static StockItemDataMapper getInstance(){
        if(stockItemDataMapper == null)
            stockItemDataMapper = new StockItemDataMapper();
        return stockItemDataMapper;
    }
    public String insertQuery(StockItem object) {
        return "";
    }
    public String deleteQuery(StockItem object) {
        return "";
    }
    public String updateQuery(StockItem object) {
        return "";
    }
    public String findQuery(String ...key) {
        return "";
    }
    public String findAllQuery() {
        return "";
    }
    public StockItem findInIdentityMap(String ...key) {
        return null;
    }
    public StockItem insertIdentityMap(ResultSet match) throws SQLException{
        if (match == null)
            return null;
        throw new SQLException();
    }
    public LinkedList<StockItem> findAllFromBranch(int branchId){
        if(branchId == 0)
            return null;
        return new LinkedList<>();
    }
    public int getShelvesIdAmount(int branchId, int catalogId){
        return branchId + catalogId;
    }
    public int getBackIdAmount(int branchId, int catalogId){
        return branchId + catalogId;
    }
}