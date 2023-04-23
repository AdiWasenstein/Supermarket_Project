package SuperLi.src;
import SuperLi.src.Stock.DamageType;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchController {
    static BranchController branchController;
    ArrayList<Branch> branches;
    Branch currentBranch;
    private BranchController(){
        branches = new ArrayList<>();
        branches.add(new Branch("Beer Sheva"));
        branches.add(new Branch("Rishon Lezion"));
        branches.add(new Branch("Netanya"));
        currentBranch = branches.get(0);
    }
    public static BranchController getInstance(){
        if (branchController == null)
            branchController = new BranchController();
        return branchController;
    }
    public boolean changeBranch(int id){
        if(!(0 <= id && id <= branches.size()))
            return false;
        currentBranch = branches.get(id - 1);
        return true;
    }
}