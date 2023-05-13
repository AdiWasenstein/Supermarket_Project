package SuperLi.src.Presentation;
import SuperLi.src.BusinessLogic.StockManagementFacade;

public class BranchStockManagerMenu extends AMenu{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static BranchStockManagerMenu branchStockManagerMenu = null;
    private BranchStockManagerMenu(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
        this.branchId = branchId;
    }
    public static BranchStockManagerMenu getInstance(int branchId){
        if(branchStockManagerMenu == null)
            branchStockManagerMenu = new BranchStockManagerMenu(branchId);
        else
            branchStockManagerMenu.branchId = branchId;
        return branchStockManagerMenu;
    }
    public void printMenu(){
        System.out.format("Welcome %s's branch manager!\n", stockManagementFacade.getBranchAddress(branchId));
        System.out.println("What would you like to do?");
        System.out.println("1. Create Shortage Order");
        System.out.println("2. Update Periodic Report");
        System.out.println("3. Print Report");
        System.out.println("0. Actually, I would like to exit menu.");
    }
    public void communicate(){
        boolean run = true;
        while (run) {
            printMenu();
            System.out.print("Please enter your option: ");
            int choice = inputNumber();
            switch (choice) {
                case 1 -> createShortageStockOrder();
                case 2 -> updatePeriodicOrder();
                case 3 -> generateReport();
                case 0 -> run = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    public int getReport(){
        System.out.println("1. Stock Item Report");
        System.out.println("2. Required Stock Report");
        System.out.println("3. Damaged Stock Report");
        System.out.print("Please enter your option: "); return inputNumber();
    }
    public void generateReport() {
        ReportViewer reportViewer = ReportViewer.getInstance();
        switch (getReport()){
            case 1 -> reportViewer.generateStockItemReport(branchId);
            case 2 -> reportViewer.generateRequiredStockReport(branchId);
            case 3 -> reportViewer.generateDamagedReport(branchId);
            default -> System.out.println("Invalid option. Returning to main...");
        }
    }
    public void createShortageStockOrder() {
        if (!stockManagementFacade.createShortageOrder(branchId))
            System.out.print("No items required for the branch. ");
        else
            System.out.print("Required stock ordered sent to the suppliers successfully. ");
        System.out.println("Returning to menu...");
    }
    public void updatePeriodicOrder() {
        System.out.println("Please choose a report to update:");
        if(!ReportViewer.getInstance().generatePeriodicReportsAccordingToBrunch(branchId)) {
            System.out.println("There are no periodic reports of branch with given number. Returning to menu...");
            return;
        }
        System.out.print("Please insert the report's ID that you want ot update: "); int reportId = inputNumber();
        if(reportId < 0) {
            System.out.println("Invalid report ID. Returning to menu...");
            return;
        }
        try{
            if(!stockManagementFacade.updatePeriodReport(reportId, branchId)) {
                System.out.println("Cannot update the requested reportId");
                return;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.format("Report %d was updated successfully. Returning to menu...\n", reportId);
    }
}
