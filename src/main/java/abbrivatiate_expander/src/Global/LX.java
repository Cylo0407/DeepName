package abbrivatiate_expander.src.Global;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class LX {
    public static String projectLocation = "E:/Projects/AbbreviationExpander/data/";
    public static String javaSource = projectLocation + "test.java";
    public static String javaTrim = projectLocation + "trim_" + "test.java";
    public static String tempFile = projectLocation + "temp_test.csv";
    public static String javaDest = projectLocation + "test_Exp.java";

    public static String rawSource;
    public static String[] splitedRawSource;

    public static CompilationUnit unit;

    public static HashMap<String, Integer> IdLineInFile = new HashMap<String, Integer>();

}
