package SuperLi.src.DataAccess;


import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.PeriodicReport;
import SuperLi.src.BusinessLogic.Supplier;
import SuperLi.src.BusinessLogic.SupplierItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PeriodicReportDataMapper extends ADataMapper{
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

//    // TODO implement
//    public LinkedList<PeriodicReport> findAll(){return new LinkedList<>();}
//    public LinkedList<PeriodicReport> findAllByBranch(String branchID){return new LinkedList<>();}


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

    }
//    public String insertQuery(PeriodicReport report){
//        periodicReportIdentityMap.put(report.getReportId(), report);
//        return String.format("INSERT INTO Branches(reportId, supplierCatalogNumber, numberOfUnits, supplierId) VALUES (%d, %d, %d, %d)", report.getReportId(),
//                report.);
//    }


    public void insertReport(PeriodicReport report)
    {
        HashMap<SupplierItem,Integer> itemsInReport = report.getItems();
        for(SupplierItem supplierItem : itemsInReport.keySet())
        {
            executeVoidQuery(String.format("INSERT INTO PeriodicReports (branchNumber, reportId, supplierCatalogNumber, numberOfUnits, supplierId) VALUES (%d, %d, %d, %d, %d)"
                    ,report.getBranchNumber(), report.getReportId(),
                    supplierItem.GetMarketId(), supplierItem.getNumberOfUnits(), report.getSupplier().getSupplierId()));
        }
    }

    public String deleteQuery(PeriodicReport report){
        periodicReportIdentityMap.remove(report.getReportId());
        return String.format("DELETE FROM PeriodicReports WHERE reportId = %d", report.getReportId());
    }

    // TODO laavor im adi
    public String updateQuery(PeriodicReport report)
    {
        return String.format("UPDATE Branches SET Address = '%s' WHERE Id = %d", branch.getAddress(), branch.getId());
    }

    public String findQuery(String ...key){
        return String.format("SELECT * FROM Branches WHERE Id = '%s'", key[0]);
    }

    public String findAllQuery(){
        return "SELECT * FROM Branches";
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
        report = new PeriodicReport(match.getInt("reportId"), match.getInt("Id"));
        periodicReportIdentityMap.put(report.getReportId(), report);
        return report;
    }
}
