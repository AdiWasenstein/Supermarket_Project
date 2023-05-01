package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.AReport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ReportViewer {
    static ReportViewer reportViewer = null;
    private ReportViewer() {}
    public static ReportViewer getInstance(){
        if(reportViewer == null)
            reportViewer = new ReportViewer();
        return reportViewer;
    }
    public void viewReport(AReport report){
        String[] headers = report.getHeaders();
        ArrayList<String[]> records = report.initializeRecords();
        int[] widths = getColumnsWidths(headers, records);
        printSeperator(widths, headers.length);
        printRecord(headers, widths);
        printSeperator(widths, headers.length);
        for(String[] record : records)
            printRecord(record, widths);
        printSeperator(widths, headers.length);
    }
    public int[] getColumnsWidths(String[] headers, ArrayList<String[]> records){
        int[] widths= Arrays.stream(headers).mapToInt(String::length).toArray();
        for(String[] cells : records)
            for(int i = 0; i < headers.length; i++)
                widths[i] = Math.max(widths[i], cells[i].length());
        return widths;
    }
    private void printSeperator(int[] widths, int fieldsCount){
        for(int i = 0; i < fieldsCount; i++){
            String line = String.join("", Collections.nCopies(widths[i] + 2, "-"));
            System.out.print("+" + line + (i == fieldsCount - 1 ? "+" : ""));
        }
        System.out.println();
    }
    private void printRecord(String[] cells, int[] widths){
        for(int i = 0; i < cells.length; i++){
            String cell = cells[i];
            String ver_str = i == cells.length - 1 ? "|" : "";
            System.out.printf("%s %-" + widths[i] + "s %s", "|", cell, ver_str);
        }
        System.out.println();
    }
}