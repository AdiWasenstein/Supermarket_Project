package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.PeriodicReport;

import java.util.HashMap;
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
}
