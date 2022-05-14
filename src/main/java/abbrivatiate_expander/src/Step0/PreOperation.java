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
        preOperation(LX.javaSource, LX.javaTrim, LX.tempFile);
    }

    public static void preOperation(String filePath, String trimPath, String tempPath) {
        File trimFile = new File(trimPath);
        File tempFile = new File(tempPath);
        if (trimFile.exists()) {
            trimFile.delete();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        FileFormater(filePath, trimPath);

        System.out.println(trimPath + "已经生成");
    }

    public static void FileFormater(String filePath, String trimPath) {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
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
        appendFile(unit.toString(), trimPath);
    }

    public static void appendFile(String line, String trimPath) {
        FileWriter fw = null;
        try {
            File f = new File(trimPath);
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
