package SuperLi.src;

import SuperLi.src.Stock.StockItem;
import java.util.*;

public class StockItemDataMapper implements IDataMapper<StockItem>{
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

    public Optional<StockItem> find(String param){
        return Optional.empty();
    }
    public void insert(StockItem object){
    }
    public void update(StockItem object){
    }
    public void delete(StockItem object){
    }
    public LinkedList<StockItem> findAllFromBranch(int branchId){
        return new LinkedList<>();
    }
    public int getShelvesIdAmount(int branchId, int catalogId){
        return 0;
    }
    public int getBackIdAmount(int branchId, int catalogId){
        return 0;
    }
}