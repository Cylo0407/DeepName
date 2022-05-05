package abbrivatiate_expander.src.jiangyanjie.menuHandles;

import java.util.ArrayList;

import abbrivatiate_expander.src.Step1.Utils;
import abbrivatiate_expander.src.entity.Argument;
import abbrivatiate_expander.src.entity.ClassName;
import abbrivatiate_expander.src.entity.Field;
import abbrivatiate_expander.src.entity.Identifier;
import abbrivatiate_expander.src.entity.MethodName;
import abbrivatiate_expander.src.entity.Parameter;
import abbrivatiate_expander.src.entity.Variable;
import abbrivatiate_expander.src.expansion.AllExpansions;
import abbrivatiate_expander.src.relation.AssignInfo;
import abbrivatiate_expander.src.relation.ClassInfo;
import abbrivatiate_expander.src.relation.CommentInfo;
import abbrivatiate_expander.src.relation.MethodDeclarationInfo;
import abbrivatiate_expander.src.relation.MethodInvocationInfo;
import abbrivatiate_expander.src.relation.RelationBase;

public class HandleOneFile {
    // relationBase that are selected from visitors in Package visitor
    public ArrayList<RelationBase> relationBases = new ArrayList<>();
    public String tempPath;

    public HandleOneFile(String tempPath) {
        this.tempPath = tempPath;
    }

    public void parse() {
        for (RelationBase relationBase : relationBases) {
            if (relationBase instanceof AssignInfo) {
                handleAssign((AssignInfo) relationBase);
            } else if (relationBase instanceof ClassInfo) {
                handleExtend((ClassInfo) relationBase);
            } else if (relationBase instanceof MethodDeclarationInfo) {
                handleMethodDeclaration((MethodDeclarationInfo) relationBase);
            } else if (relationBase instanceof MethodInvocationInfo) {
                handleMethodInvocation((MethodInvocationInfo) relationBase);
            } else if (relationBase instanceof CommentInfo) {
                handleComment((CommentInfo) relationBase);
            }
        }
    }

    private void handleComment(CommentInfo commentInfo) {
        int startLine = commentInfo.startLine;
        int endLine = commentInfo.line;

        boolean currentLine = true;
        // comment in current line
        for (RelationBase relationBase : relationBases) {
            if (relationBase.line == startLine && relationBase != commentInfo) {
                currentLine = false;
                handleRelationBaseAndCommentInfo(relationBase, commentInfo);
            }
        }
        if (!currentLine) {
            return;
        }
        // comment in previous line
        for (RelationBase relationBase : relationBases) {
            if (relationBase.line == (endLine + 1)) {
                handleRelationBaseAndCommentInfo(relationBase, commentInfo);
            }
        }
    }

    private void handleRelationBaseAndCommentInfo(RelationBase relationBase, CommentInfo commentInfo) {
        if (relationBase instanceof AssignInfo) {
            AssignInfo assignInfo = (AssignInfo) relationBase;
            ArrayList<Identifier> left = assignInfo.left;
            ArrayList<Identifier> right = assignInfo.right;
            for (Identifier identifier : left) {
                Utils.putHashSet(AllExpansions.idToComments, identifier.id, commentInfo.content);
            }
            if (right != null) {
                for (Identifier identifier : right) {
                    Utils.putHashSet(AllExpansions.idToComments, identifier.id, commentInfo.content);
                }
            }
        } else if (relationBase instanceof ClassInfo) {
            ClassInfo extendInfo = (ClassInfo) relationBase;
            ClassName className = extendInfo.className;
            Utils.putHashSet(AllExpansions.idToComments, className.id, commentInfo.content);

            ArrayList<ClassName> classNames = extendInfo.expans;
            for (ClassName className1 : classNames) {
                Utils.putHashSet(AllExpansions.idToComments, className1.id, commentInfo.content);
            }
        } else if (relationBase instanceof MethodDeclarationInfo) {
            MethodDeclarationInfo methodDeclarationInfo = (MethodDeclarationInfo) relationBase;
            Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.methodName.id, commentInfo.content);

            for (int j = 0; j < methodDeclarationInfo.parameters.size(); j++) {
                Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.parameters.get(j).id,
                        commentInfo.content);
            }

            // not constructor
            if (methodDeclarationInfo.methodName.type != null) {
                Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.methodName.type.id,
                        commentInfo.content);
            }

        } else if (relationBase instanceof MethodInvocationInfo) {
            MethodInvocationInfo methodInvocationInfo = (MethodInvocationInfo) relationBase;
            Utils.putHashSet(AllExpansions.idToComments, methodInvocationInfo.methodName.type.id, commentInfo.content);

            for (int j = 0; j < methodInvocationInfo.arguments.size(); j++) {
                Argument argument = methodInvocationInfo.arguments.get(j);
                for (int k = 0; k < argument.identifiers.size(); k++) {
                    Utils.putHashSet(AllExpansions.idToComments, argument.identifiers.get(k).id, commentInfo.content);
                }
            }
        }
    }

    private void handleMethodInvocation(MethodInvocationInfo methodInvocationInfo) {

        Utils.putHashSet(AllExpansions.idToFiles, methodInvocationInfo.methodName.id, tempPath);
        AllExpansions.idToMethodName.put(methodInvocationInfo.methodName.id, methodInvocationInfo.methodName);

        Utils.putHashSet(AllExpansions.idToFiles, methodInvocationInfo.methodName.type.id, tempPath);
        AllExpansions.idToClassName.put(methodInvocationInfo.methodName.type.id, methodInvocationInfo.methodName.type);

        ArrayList<Argument> arguments = methodInvocationInfo.arguments;
        for (Argument argument : arguments) {
            ClassName type = argument.type;

            Utils.putHashSet(AllExpansions.idToFiles, type.id, tempPath);
            AllExpansions.idToClassName.put(type.id, type);

            ArrayList<Identifier> identifiers = argument.identifiers;

            for (Identifier identifier : identifiers) {
                Utils.putHashSet(AllExpansions.idToFiles, identifier.id, tempPath);
                AllExpansions.idToIdentifier.put(identifier.id, identifier);

                if (identifier instanceof Parameter) {
                    Parameter parameter = (Parameter) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof Field) {
                    Field parameter = (Field) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof Variable) {
                    Variable parameter = (Variable) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof MethodName) {
                    MethodName parameter = (MethodName) identifier;
                    if (parameter.type != null) {
                        Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                        AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
                    }
                }
            }
        }

        AllExpansions.methodInvocationInfos.add(methodInvocationInfo);
    }

    private void handleMethodDeclaration(MethodDeclarationInfo methodDeclarationInfo) {
        AllExpansions.methodDeclarationInfos.add(methodDeclarationInfo);
        Utils.putHashSet(AllExpansions.idToFiles, methodDeclarationInfo.methodName.id, tempPath);
        AllExpansions.idToMethodName.put(methodDeclarationInfo.methodName.id, methodDeclarationInfo.methodName);

        if (methodDeclarationInfo.methodName.type != null) {
            Utils.putHashSet(AllExpansions.idToFiles, methodDeclarationInfo.methodName.type.id, tempPath);
            AllExpansions.idToClassName.put(methodDeclarationInfo.methodName.type.id,
                    methodDeclarationInfo.methodName.type);
        }

        ArrayList<Parameter> parameters = methodDeclarationInfo.parameters;
        for (Parameter parameter1 : parameters) {
            Utils.putHashSet(AllExpansions.idToFiles, parameter1.id, tempPath);
            AllExpansions.idToParameter.put(parameter1.id, parameter1);

            Utils.putHashSet(AllExpansions.idToFiles, parameter1.type.id, tempPath);
            AllExpansions.idToClassName.put(parameter1.type.id, parameter1.type);
        }

        ArrayList<Identifier> identifiers = methodDeclarationInfo.identifiers;

        for (Identifier identifier : identifiers) {
            Utils.putHashSet(AllExpansions.idToFiles, identifier.id, tempPath);
            AllExpansions.idToIdentifier.put(identifier.id, identifier);

            if (identifier instanceof Parameter) {
                Parameter parameter = (Parameter) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Field) {
                Field parameter = (Field) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Variable) {
                Variable parameter = (Variable) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof MethodName) {
                MethodName parameter = (MethodName) identifier;
                if (parameter.type != null) {
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
                }
            }
        }

    }

    private void handleExtend(ClassInfo classInfo) {
        AllExpansions.classInfos.add(classInfo);

        ClassName className = classInfo.className;
        Utils.putHashSet(AllExpansions.idToFiles, className.id, tempPath);
        AllExpansions.idToClassName.put(className.id, className);

        ArrayList<ClassName> classNames = classInfo.expans;
        for (ClassName className1 : classNames) {
            Utils.putHashSet(AllExpansions.idToFiles, className1.id, tempPath);
            AllExpansions.idToClassName.put(className1.id, className1);
            Utils.put(AllExpansions.childToParent, className.id, className1.id);
            Utils.put(AllExpansions.parentToChild, className1.id, className.id);
        }

        ArrayList<Field> fields = classInfo.fields;
        for (Field field : fields) {
            Utils.putHashSet(AllExpansions.idToFiles, field.id, tempPath);
            AllExpansions.idToField.put(field.id, field);

            Utils.putHashSet(AllExpansions.idToFiles, field.type.id, tempPath);
            AllExpansions.idToClassName.put(field.type.id, field.type);
        }

        ArrayList<MethodName> methodNames = classInfo.methodNames;
        for (MethodName methodName : methodNames) {
            Utils.putHashSet(AllExpansions.idToFiles, methodName.id, tempPath);
            AllExpansions.idToMethodName.put(methodName.id, methodName);

            if (methodName.type != null) {
                Utils.putHashSet(AllExpansions.idToFiles, methodName.type.id, tempPath);
                AllExpansions.idToClassName.put(methodName.type.id, methodName.type);
            }
        }
        ArrayList<Identifier> identifiers = classInfo.identifiers;

        for (Identifier identifier : identifiers) {
            Utils.putHashSet(AllExpansions.idToFiles, identifier.id, tempPath);
            AllExpansions.idToIdentifier.put(identifier.id, identifier);

            if (identifier instanceof Parameter) {
                Parameter parameter = (Parameter) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Field) {
                Field parameter = (Field) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Variable) {
                Variable parameter = (Variable) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof MethodName) {
                MethodName parameter = (MethodName) identifier;
                if (parameter.type != null) {
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
                }
            }
        }
    }

    private void handleAssign(AssignInfo assignInfo) {
        AllExpansions.assignInfos.add(assignInfo);

        ArrayList<Identifier> left = assignInfo.left;
        ArrayList<Identifier> right = assignInfo.right;
        for (Identifier identifier : left) {
            Utils.putHashSet(AllExpansions.idToFiles, identifier.id, tempPath);
            AllExpansions.idToIdentifier.put(identifier.id, identifier);

            if (identifier instanceof Parameter) {
                Parameter parameter = (Parameter) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Field) {
                Field parameter = (Field) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof Variable) {
                Variable parameter = (Variable) identifier;
                Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

            } else if (identifier instanceof MethodName) {
                MethodName parameter = (MethodName) identifier;
                if (parameter.type != null) {
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
                }
            }
        }
        if (right != null) {
            for (Identifier identifier : right) {
                Utils.putHashSet(AllExpansions.idToFiles, identifier.id, tempPath);
                AllExpansions.idToIdentifier.put(identifier.id, identifier);

                if (identifier instanceof Parameter) {
                    Parameter parameter = (Parameter) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof Field) {
                    Field parameter = (Field) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof Variable) {
                    Variable parameter = (Variable) identifier;
                    Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                    AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

                } else if (identifier instanceof MethodName) {
                    MethodName parameter = (MethodName) identifier;
                    if (parameter.type != null) {
                        Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, tempPath);
                        AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
                    }
                }
            }
        }
    }
}
