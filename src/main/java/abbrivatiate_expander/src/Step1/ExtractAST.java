package abbrivatiate_expander.src.Step1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import abbrivatiate_expander.src.Global.LX;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import abbrivatiate_expander.src.expansion.AllExpansions;
import abbrivatiate_expander.src.jiangyanjie.menuHandles.HandleOneFile;
import abbrivatiate_expander.src.visitor.AssignVistor;
import abbrivatiate_expander.src.visitor.ClassVisitor;
import abbrivatiate_expander.src.visitor.CommentVisitor;
import abbrivatiate_expander.src.visitor.MethodDeclarationVisitor;
import abbrivatiate_expander.src.visitor.MethodInvocationVisitor;

public class ExtractAST {

    public static void main(String[] args) throws IOException {
//		Step0.PreOperation.main(null);

//        parseCode(LX.javaSource, LX.tempFile);
        parseCodeDeclaration(LX.javaSource, LX.tempFile);
    }

    public static void parseCode(String srcPath, String tempPath) {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcPath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("start to parse!");

        // System.err.println(compilationUnit.getElementName());
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setSource(new String(input).toCharArray());
        astParser.setUnitName("");
        astParser.setEnvironment(new String[]{}, new String[]{}, new String[]{}, true);
        CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
        LX.unit = unit;
//		System.out.println(unit);
        HandleOneFile resultOfOneFile = new HandleOneFile(tempPath);
        unit.accept(new ClassVisitor(unit, resultOfOneFile));
        unit.accept(new MethodDeclarationVisitor(unit, resultOfOneFile));
        unit.accept(new MethodInvocationVisitor(unit, resultOfOneFile));
        unit.accept(new AssignVistor(unit, resultOfOneFile));
        // comment
        for (Object object : unit.getCommentList()) {
            Comment comment = (Comment) object;
            String[] temp = {};
            try {
                temp = new String(input).split("\n");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            comment.accept(new CommentVisitor(unit, temp, resultOfOneFile));
        }
        resultOfOneFile.parse();

        AllExpansions.postprocess(tempPath);
        System.err.println("End");
    }

    public static void parseCodeDeclaration(String srcPath, String tempPath) {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcPath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("start to parse!");

        AllExpansions.reInit();

        // System.err.println(compilationUnit.getElementName());
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setSource(new String(input).toCharArray());
        astParser.setUnitName("");
        astParser.setEnvironment(new String[]{}, new String[]{}, new String[]{}, true);
        CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
        LX.unit = unit;
//		System.out.println(unit);
        HandleOneFile resultOfOneFile = new HandleOneFile(tempPath);
        unit.accept(new MethodDeclarationVisitor(unit, resultOfOneFile));
        // comment
        for (Object object : unit.getCommentList()) {
            Comment comment = (Comment) object;
            String[] temp = {};
            try {
                temp = new String(input).split("\n");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            comment.accept(new CommentVisitor(unit, temp, resultOfOneFile));
        }
        resultOfOneFile.parse();

        AllExpansions.postprocess(tempPath);
        System.err.println("End");
    }

}
