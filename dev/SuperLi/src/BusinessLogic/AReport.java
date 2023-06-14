package BusinessLogic;

import java.util.LinkedList;

public abstract class AReport {
    public abstract LinkedList<String[]> initializeRecords();
    public abstract String[] getHeaders();
}
