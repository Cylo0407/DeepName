package abbrivatiate_expander.src.Global;

import abbrivatiate_expander.src.Step0.PreOperation;
import abbrivatiate_expander.src.Step1.ExtractAST;
import abbrivatiate_expander.src.Step2.HandleCSV;

import java.io.IOException;

public class GameSub {
    public static void main(String[] args) throws IOException {

        PreOperation.main(null);
        ExtractAST.main(null);
        HandleCSV.main(null);
    }
}
