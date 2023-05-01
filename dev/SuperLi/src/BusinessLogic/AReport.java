package SuperLi.src.BusinessLogic;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

public abstract class AReport {
    public abstract LinkedList<String[]> initializeRecords();
    public abstract String[] getHeaders();
}
