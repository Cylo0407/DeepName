package abbrivatiate_expander.src.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import abbrivatiate_expander.src.entity.ClassName;
import abbrivatiate_expander.src.entity.Identifier;
import abbrivatiate_expander.src.entity.Variable;
import abbrivatiate_expander.src.jiangyanjie.menuHandles.HandleOneFile;
import abbrivatiate_expander.src.relation.AssignInfo;

import abbrivatiate_expander.src.Step1.Utils;

public class AssignVistor extends ASTVisitor {
    // used to get line numbers
    private CompilationUnit compilationUnit;
    // used to collect relation info
    private HandleOneFile globalVariables;

    public AssignVistor(CompilationUnit compilationUnit, HandleOneFile globalVariables) {
        super();
        this.compilationUnit = compilationUnit;
        this.globalVariables = globalVariables;
    }

    @Override
    public boolean visit(Assignment node) {
        Expression left = node.getLeftHandSide();
        Expression right = node.getRightHandSide();

        ArrayList<Identifier> leftIdentifiers = Utils.parseExpression(left, compilationUnit);
        ArrayList<Identifier> rightIdentifiers = Utils.parseExpression(right, compilationUnit);

        int line = compilationUnit.getLineNumber(node.getStartPosition());
//		System.out.println("assign="+node.toString());
//		System.out.println("line="+line);
//		IdentifierPosiNode.nodeli.add(new IdentifierPosiNode(node., lineNumInFile))

        AssignInfo assignInfo = new AssignInfo(line, leftIdentifiers, rightIdentifiers);
//		System.out.println(assignInfo);
        globalVariables.relationBases.add(assignInfo);
        return super.visit(node);
    }

    // field declarations, local variable declarations,
    // ForStatement initializers, and LambdaExpression parameters
    @Override
    public boolean visit(VariableDeclarationFragment node) {
        AssignInfo assignInfo = getAssignInfo(node, compilationUnit);
        if (assignInfo != null) {
            globalVariables.relationBases.add(assignInfo);
        }
        return super.visit(node);
    }

    public static AssignInfo getAssignInfo(VariableDeclarationFragment node, CompilationUnit compilationUnit) {
        if (node.resolveBinding() == null) {
            System.err.println(node.toString());
            return null;
        }
        String id = node.resolveBinding().getKey();
        String name = node.getName().toString();
        ClassName type = new ClassName(node.resolveBinding().getType().getKey(),
                node.resolveBinding().getType().getName().toString());
        Variable variable = new Variable(id, name, type);

        ArrayList<Identifier> leftIdentifiers = new ArrayList<>();
        leftIdentifiers.add(variable);
        ArrayList<Identifier> rightIdentifiers = Utils.parseExpression(node.getInitializer(), compilationUnit);

        int line = compilationUnit.getLineNumber(node.getStartPosition());
        AssignInfo assignInfo = new AssignInfo(line, leftIdentifiers, rightIdentifiers);
        return assignInfo;
    }

    // formal parameter lists and catch clauses
    @Override
    public boolean visit(SingleVariableDeclaration node) {
        if (node.resolveBinding() == null) {
            System.err.println(node.toString());
            return super.visit(node);
        }
        String id = node.resolveBinding().getKey();
        String name = node.getName().toString();
        if (node.getType().resolveBinding() == null) {
            System.err.println(node.toString());
            return super.visit(node);
        }
        ClassName type = new ClassName(node.getType().resolveBinding().getKey(),
                node.getType().resolveBinding().getName());
        Variable variable = new Variable(id, name, type);

        ArrayList<Identifier> leftIdentifiers = new ArrayList<>();
        leftIdentifiers.add(variable);
        ArrayList<Identifier> rightIdentifiers = Utils.parseExpression(node.getInitializer(), compilationUnit);

        int line = compilationUnit.getLineNumber(node.getStartPosition());
        AssignInfo assignInfo = new AssignInfo(line, leftIdentifiers, rightIdentifiers);

        globalVariables.relationBases.add(assignInfo);
        return super.visit(node);
    }
}
