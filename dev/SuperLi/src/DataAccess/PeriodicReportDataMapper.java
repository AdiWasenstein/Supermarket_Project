package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.PeriodicReport;

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

    // TODO implement
    public LinkedList<PeriodicReport> findAll(){return new LinkedList<>();}
    public LinkedList<PeriodicReport> findAllByBranch(String branchID){return new LinkedList<>();}
}
