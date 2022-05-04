package abbrivatiate_expander.src.Step0;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import abbrivatiate_expander.src.Global.LX;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class PreOperation {
    // 将java文件的格式预处理，删除所有的空行和注释
    public static void main(String[] args) {
        preOperation(LX.javaSource);
    }

    public static void preOperation(String filePath) {
        LX.javaSource = filePath;
        File trimFile = new File(LX.javaTrim);
        File tempFile = new File(LX.tempFile);
        if (trimFile.exists()) {
            trimFile.delete();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        FileFormater(LX.javaSource);

        System.out.println(LX.javaTrim + "已经生成");
    }

    public static void FileFormater(String javaFile) {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFile));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setSource(new String(input).toCharArray());
        astParser.setUnitName("");
        astParser.setEnvironment(new String[]{}, new String[]{}, new String[]{}, true);
        CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
//		System.out.println(unit.toString());
        appendFile(unit.toString());
    }

    public static void appendFile(String line) {
        FileWriter fw = null;
        try {
            File f = new File(LX.javaTrim);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.print(line);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
