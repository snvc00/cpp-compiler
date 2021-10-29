package com.compiler.frontend.semantic;

import com.compiler.token.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SemanticAnalyzer {
    private final ArrayList<Token> tokens;
    private final HashMap<String, String> declaredVariables;
    public boolean error;

    public SemanticAnalyzer(ArrayList<Token> tokens, HashMap<String, String> declaredVariables) {
        this.tokens = tokens;
        this.declaredVariables = declaredVariables;
        this.error = false;
    }

    public void analyze() {
        HashMap<String, List<String>> compatibleTypes = getCompatibleTypes();

        // Check for undeclared variables and  operands data type mismatch
        for (int i = 0; i < tokens.size(); ++i) {
            Token currentToken = tokens.get(i);
            if (Objects.equals(currentToken.type, "OPERATOR")) {
                Token leftOperand = (i - 1) >= 0 ? tokens.get(i - 1) : null;
                Token rightOperand = (i + 1) < tokens.size() ? tokens.get(i + 1) : null;

                if (leftOperand == null || rightOperand == null) {
                    reportError(currentToken);
                    System.out.println("Error reported in null operands");
                }
                else {
                    if (isLiteralAssignment((String)currentToken.value, leftOperand)) {
                        reportError(currentToken);
                    }
                    else {
                        List<String> acceptedTokenTypes = List.of("ID", "INTEGER", "REAL", "STRING_LITERAL");

                        if (!acceptedTokenTypes.contains(leftOperand.type) || !acceptedTokenTypes.contains(rightOperand.type)) {
                            reportError(currentToken);
                        }
                        else {
                            String leftOperandType = Objects.equals(leftOperand.type, "ID") ? declaredVariables.get((String)leftOperand.value) : leftOperand.type;
                            String rightOperandType = Objects.equals(rightOperand.type, "ID") ? declaredVariables.get((String)rightOperand.value) : rightOperand.type;

                            if (leftOperandType == null || rightOperandType == null) {
                                reportError(currentToken);
                            }
                            else {
                                if (!compatibleTypes.get(leftOperandType).contains(rightOperandType)) {
                                    reportError(currentToken);
                                }
                            }
                        }
                    }
                }
            }
            else if (Objects.equals(currentToken.type, "ID")) {
                if (declaredVariables.get((String)currentToken.value) == null) {
                    reportError(currentToken);
                }
            }
        }
    }

    private boolean isLiteralAssignment(String operator, Token leftOperand) {
        if (!Objects.equals(operator, "=")) {
            return false;
        }

        return List.of("INTEGER", "REAL", "STRING_LITERAL").contains(leftOperand.type);
    }

    private HashMap<String, List<String>> getCompatibleTypes() {
        HashMap<String, List<String>> compatibleTypes = new HashMap<>();

        compatibleTypes.put("BOOL", List.of("BOOL"));
        compatibleTypes.put("INT", List.of("INT", "INTEGER"));
        compatibleTypes.put("FLOAT", List.of("FLOAT", "DOUBLE", "REAL"));
        compatibleTypes.put("DOUBLE", List.of("FLOAT", "DOUBLE", "REAL"));
        compatibleTypes.put("STRING", List.of("STRING", "STRING_LITERAL"));
        compatibleTypes.put("STRING_LITERAL", List.of("STRING", "STRING_LITERAL"));
        compatibleTypes.put("INTEGER", List.of("INT", "INTEGER"));
        compatibleTypes.put("REAL", List.of("FLOAT", "DOUBLE", "REAL"));

        return compatibleTypes;
    }

    private void reportError(Token token) {
        System.out.println("\033[1m\033[31mLine " + token.atLine + ": \033[0mSemantic error found in " + token);
        this.error = true;
    }
}
