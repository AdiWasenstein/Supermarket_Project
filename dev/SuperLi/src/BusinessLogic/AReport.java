package SuperLi.src.BusinessLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class AReport {
    public abstract ArrayList<String[]> initializeRecords();
    public abstract String[] getHeaders();
}
