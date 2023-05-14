package SuperLi.src.DataAccess;


import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class PeriodicReportDataMapper extends ADataMapper<PeriodicReport>{
    Map<Integer, PeriodicReport> periodicReportIdentityMap;
    static PeriodicReportDataMapper periodicReportDataMapper = null;
    private PeriodicReportDataMapper(){
        periodicReportIdentityMap = new HashMap<>();
    }
    public static PeriodicReportDataMapper getInstance(){
        if(periodicReportDataMapper == null)
            periodicReportDataMapper = new PeriodicReportDataMapper();
        return periodicReportDataMapper;
    }

    public void removeSupplierItemFromPeriodicReports(SupplierItem supplierItem, int supplierId)
    {
        for(PeriodicReport report : this.periodicReportIdentityMap.values())
        {
            Supplier sup = report.getSupplier();
            if(sup.getSupplierId() == supplierId)
            {
                try
                {
                    report.removeItem(supplierItem);
                }
                catch (Exception e)
                {
                    continue;
                }
            }
        }
    }
    public void insert(PeriodicReport report)
    {
        this.periodicReportIdentityMap.put(report.getReportId(), report);
        insertReport(report);
    }

    protected String insertQuery(PeriodicReport report)
    {
        return "";
    }

    public void insertReport(PeriodicReport report)
    {
        HashMap<SupplierItem,Integer> itemsInReport = report.getItems();
        for(SupplierItem supplierItem : itemsInReport.keySet())
        {
            executeVoidQuery(String.format("INSERT INTO PeriodicReports (branchNumber, reportId, supplierCatalogNumber, numberOfUnits, supplierId, day) VALUES (%d, %d, %d, %d, %d, '%s')"
                    ,report.getBranchNumber(), report.getReportId(),
                    supplierItem.getCatalogNumber(), itemsInReport.get(supplierItem), report.getSupplier().getSupplierId(), report.getDayToOrder().toString()));
        }
    }

    protected String deleteQuery(PeriodicReport report){
        return "";
    }
    protected String updateQuery(Integer...key)
    {
        return String.format("UPDATE PeriodicReports SET numberOfUnits = %d WHERE reportId = %d AND supplierCatalogNumber = %d", key[0], key[1], key[2]);
    }
    public void updateQueryForItems(HashMap<SupplierItem,Integer> itemsToUpdate, int reportId)
    {
        for(SupplierItem item : itemsToUpdate.keySet())
        {
            int numUnits = itemsToUpdate.get(item);
            executeVoidQuery(updateQuery(numUnits,reportId,item.getCatalogNumber()));
        }

    }
    protected String updateQuery(PeriodicReport x){
        return "";
    }

    public String findQuery(String ...key){
        return String.format("SELECT * FROM PeriodicReports WHERE reportId = '%s'", key[0]);
    }

    public String findAllQueryByKey(String ...key){
        return String.format("SELECT * FROM PeriodicReports WHERE reportId = '%s'", key[0]);
    }

    public String findAllQuery(){
        return "SELECT * FROM PeriodicReports";
    }

    public PeriodicReport findInIdentityMap(String ...key){
        return periodicReportIdentityMap.get(Integer.valueOf(key[0]));
    }
    public PeriodicReport insertIdentityMap(ResultSet match) throws SQLException {
        if (match == null)
            return null;
        PeriodicReport report = periodicReportIdentityMap.get(match.getInt("reportId"));
        if(report != null)
            return report;
        int branchID= match.getInt("branchNumber");
        int reportID = match.getInt("reportId");
        Day day = Day.valueOf(match.getString("day"));
        HashMap<SupplierItem,Integer> items = new HashMap<>();
        HashMap<Integer,Integer> itemsTemp = new HashMap<>();
        int suppItemNum = match.getInt("supplierCatalogNumber");
        int numberUnits = match.getInt("numberOfUnits");
        itemsTemp.put(suppItemNum, numberUnits);
        int supId = (match.getInt("supplierId"));
        while (match.next())
        {
            suppItemNum = match.getInt("supplierCatalogNumber");
            numberUnits = match.getInt("numberOfUnits");
            itemsTemp.put(suppItemNum, numberUnits);
        }
        Optional<Supplier> suppOpt = SupplierDataMapper.getInstance().find(String.valueOf(supId));

        if (suppOpt.isEmpty())
            return null;

        Supplier supp = suppOpt.get();
        for (Integer itemNum : itemsTemp.keySet())
        {
            SupplierItem suppItem = SupplierItemDataMapper.getInstance().find(String.valueOf(supp.getSupplierId()), String.valueOf(itemNum)).get();
            items.put(suppItem, itemsTemp.get(itemNum));
        }
        report = new PeriodicReport(branchID, day, supp, items, reportID);
        periodicReportIdentityMap.put(report.getReportId(), report);
        return report;
    }
    public LinkedList<PeriodicReport> findByBranch(int branchNum)
    {
        LinkedList<PeriodicReport> all = findAll();
        LinkedList<PeriodicReport> reportsForBranch = new LinkedList<>();
        for (PeriodicReport report : all)
            if (report.getBranchNumber() == branchNum)
                reportsForBranch.add(report);
        return reportsForBranch;
    }
}
