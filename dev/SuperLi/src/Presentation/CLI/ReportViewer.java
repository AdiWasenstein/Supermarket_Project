package SuperLi.src.Presentation.CLI;

import SuperLi.src.BusinessLogic.AReport;
import SuperLi.src.BusinessLogic.StockManagementFacade;
import SuperLi.src.BusinessLogic.PeriodicReport;
import SuperLi.src.BusinessLogic.OrderController;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

public class ReportViewer {
    static ReportViewer reportViewer = null;
    StockManagementFacade stockManagementFacade;
    private ReportViewer() {
        stockManagementFacade = StockManagementFacade.getInstance();
    }
    public static ReportViewer getInstance(){
        if(reportViewer == null)
            reportViewer = new ReportViewer();
        return reportViewer;
    }
    public boolean generatePeriodicReportsAccordingToBrunch(int branchId)
    {
        LinkedList<PeriodicReport> reportsOfBranch = OrderController.getInstance().findReportsOfBranch(branchId);
        if(reportsOfBranch == null || reportsOfBranch.isEmpty())
            return false;
        System.out.println("Periodic Reports of branch " + branchId);
        for(PeriodicReport report : reportsOfBranch)
        {
            System.out.println("Report id: " + report.getReportId() + "\nDay to order: " + report.getDayToOrder().toString() + "\nSupplier: " + report.getSupplier().getSupplierCard().getSupplierName());
            viewReport(report);
        }
        return true;
    }
    public void generateAllCatalogReport(){
        viewReport(stockManagementFacade.generateAllCatalogReport());
    }
    public void generateCategoryReport(LinkedList<LinkedList<String>> categories){
        viewReport(stockManagementFacade.generateCategoryReport(categories));
    }
    public void generateStockItemReport(int branchId){
        viewReport(stockManagementFacade.generateStockItemsReport(branchId));
    }
    public void generateRequiredStockReport(int branchId){
        viewReport(stockManagementFacade.generateRequiredStockReport(branchId));
    }
    public void generateDamagedReport(int branchId){
        viewReport(stockManagementFacade.generateDamagedStockReport(branchId));
    }
    public void viewReport(AReport report){
        String[] headers = report.getHeaders();
        LinkedList<String[]> records = report.initializeRecords();
        int[] widths = getColumnsWidths(headers, records);
        printSeperator(widths, headers.length);
        printRecord(headers, widths);
        printSeperator(widths, headers.length);
        for(String[] record : records)
            printRecord(record, widths);
        printSeperator(widths, headers.length);
    }
    public int[] getColumnsWidths(String[] headers, LinkedList<String[]> records){
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