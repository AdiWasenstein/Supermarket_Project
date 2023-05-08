package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.Branch;
import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.PeriodicReport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PeriodicReportDataMapper {
    Map<Integer, CatalogItem> periodicReportIdentitiyMap;
    static PeriodicReportDataMapper periodicDataMapper = null;
    private PeriodicReportDataMapper(){
        periodicReportIdentitiyMap = new HashMap<>();
    }

    public static PeriodicReportDataMapper getInstance() {
        if (periodicDataMapper == null)
            periodicDataMapper = new PeriodicReportDataMapper();
        return periodicDataMapper;
    }
    // TODO no need for Optional?
    public LinkedList<PeriodicReport> findAll(String param)
    {
        return new LinkedList<>();
    }
}
