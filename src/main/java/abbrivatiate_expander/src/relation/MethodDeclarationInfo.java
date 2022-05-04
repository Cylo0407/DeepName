package abbrivatiate_expander.src.relation;

import java.util.ArrayList;

import abbrivatiate_expander.src.entity.Identifier;
import abbrivatiate_expander.src.entity.MethodName;
import abbrivatiate_expander.src.entity.Parameter;

public class MethodDeclarationInfo extends RelationBase {
	public MethodName methodName;
	public ArrayList<Parameter> parameters;
	public ArrayList<Identifier> identifiers;

	public MethodDeclarationInfo(int line, MethodName methodName, ArrayList<Parameter> parameters,
			ArrayList<Identifier> identifiers) {
		super(line);
		this.methodName = methodName;
		this.parameters = parameters;
		this.identifiers = identifiers;
	}

	@Override
	public String toString() {
		return "MethodDeclarationInfo [methodName=" + methodName + ", parameters=" + parameters + ", line=" + line
				+ "]";
	}
}
