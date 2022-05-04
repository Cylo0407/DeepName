package abbrivatiate_expander.src.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import abbrivatiate_expander.src.entity.ClassName;
import abbrivatiate_expander.src.entity.Identifier;
import abbrivatiate_expander.src.entity.MethodName;
import abbrivatiate_expander.src.entity.Variable;

import abbrivatiate_expander.src.Global.LX;

public class SimpleVisitor extends ASTVisitor
{
	public ArrayList<Identifier> identifiers = new ArrayList<>();

	@Override
	public boolean visit(SimpleName node)
	{
		if (node.resolveBinding() == null)
		{
			System.err.println(node.toString());
			return super.visit(node);
		}
		int kind = node.resolveBinding().getKind();
		switch (kind) {
		case IBinding.TYPE: {
			String id = node.resolveBinding().getKey();
			String name = node.resolveBinding().getName().toString();

			LX.IdLineInFile.put(id, LX.unit.getLineNumber(node.getStartPosition()));
//			System.out.println(id+"||"+name+"||"+Global.LX.unit.getLineNumber(node.getStartPosition()));
			ClassName className = new ClassName(id, name);

			identifiers.add(className);


			break;
		}
		case IBinding.METHOD: {
			String id = node.resolveBinding().getKey();
			String name = node.resolveBinding().getName().toString();
			LX.IdLineInFile.put(id, LX.unit.getLineNumber(node.getStartPosition()));
			if (node.resolveTypeBinding() == null)
			{
				System.err.println(node.toString());
				return super.visit(node);
			}
			String typeid = node.resolveTypeBinding().getKey();
			String typename = node.resolveTypeBinding().getName().toString();
			ClassName className = new ClassName(typeid, typename);

			MethodName methodName = new MethodName(id, name, className);
			identifiers.add(methodName);

			break;
		}
		case IBinding.VARIABLE: {
			String id = node.resolveBinding().getKey();
			String name = node.resolveBinding().getName().toString();
			LX.IdLineInFile.put(id, LX.unit.getLineNumber(node.getStartPosition()));
			if (node.resolveTypeBinding() == null)
			{
				System.err.println(node.toString());
				return super.visit(node);
			}
			Variable variable = new Variable(id, name,
					new ClassName(node.resolveTypeBinding().getKey(), node.resolveTypeBinding().getName().toString()));
			identifiers.add(variable);

			break;
		}
		default:
			break;
		}
		return super.visit(node);
	}
}
