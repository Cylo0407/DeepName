package abbrivatiate_expander.src.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import abbrivatiate_expander.src.entity.Argument;
import abbrivatiate_expander.src.entity.ClassName;
import abbrivatiate_expander.src.entity.Identifier;
import abbrivatiate_expander.src.entity.MethodName;
import abbrivatiate_expander.src.jiangyanjie.menuHandles.HandleOneFile;
import abbrivatiate_expander.src.relation.MethodInvocationInfo;

import abbrivatiate_expander.src.Global.LX;
import abbrivatiate_expander.src.Step1.Utils;

public class MethodInvocationVisitor extends ASTVisitor {
    private CompilationUnit compilationUnit;
    private HandleOneFile globalVariables;

    public MethodInvocationVisitor(CompilationUnit compilationUnit, HandleOneFile globalVariables) {
        super();
        this.compilationUnit = compilationUnit;
        this.globalVariables = globalVariables;

    }

    @Override
    public boolean visit(MethodInvocation node) {
        if (node.resolveMethodBinding() == null) {
            System.err.println(node.toString());
            return super.visit(node);
        }
        String id = node.resolveMethodBinding().getKey();
        String name = node.getName().toString();
        int line = compilationUnit.getLineNumber(node.getStartPosition());
        LX.IdLineInFile.put(id, line);
        if (node.resolveTypeBinding() == null) {
            System.err.println(node.toString());
            return super.visit(node);
        }
        ClassName className = new ClassName(node.resolveTypeBinding().getKey(),
                node.resolveTypeBinding().getName().toString());
        MethodName methodName = new MethodName(id, name, className);

        // arguments
        ArrayList<Argument> arguments = new ArrayList<>();
        try {
            for (Object object : node.arguments()) {
                Expression expression = (Expression) object;
                ArrayList<Identifier> identifiers = Utils.parseExpression(expression, compilationUnit);
                if (expression.resolveTypeBinding() == null) {
                    System.err.println(node.toString());
                    return super.visit(node);
                }
                Argument argument = new Argument(new ClassName(expression.resolveTypeBinding().getKey(),
                        expression.resolveTypeBinding().getName().toString()), identifiers);
                arguments.add(argument);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//		int line = compilationUnit.getLineNumber(node.getStartPosition());
        MethodInvocationInfo methodInvocationInfo = new MethodInvocationInfo(line, methodName, arguments);
        globalVariables.relationBases.add(methodInvocationInfo);
        return super.visit(node);
    }
}
