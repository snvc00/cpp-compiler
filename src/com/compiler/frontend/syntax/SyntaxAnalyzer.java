package com.compiler.frontend.syntax;

import com.compiler.frontend.lexical.LexicalAnalyzer;
import com.compiler.token.Token;

import java.util.*;

public class SyntaxAnalyzer {
    private final ArrayList<Token> tokens;
    HashMap<String, LinkedList<String>> validTransitions;
    public boolean error;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        tokens = new ArrayList<>(lexicalAnalyzer.tokens);
        error = false;
        validTransitions = new HashMap<>();

        String[] dataTypes = new String[]{"VOID", "BOOL", "CHAR", "INT", "FLOAT", "DOUBLE", "STRING"};
        String[] literals = new String[]{"TRUE", "FALSE", "STRING_LITERAL", "INTEGER", "REAL"};
        String[] operators = new String[]{"%", "&&", "!=", "*", "+", "-", "/", "<", "<=", "=", "==", ">", ">=", "||"};

        for (String dataType : dataTypes) {
            validTransitions.put(dataType, new LinkedList<>(List.of("ID")));
        }

        validTransitions.put("FOR", new LinkedList<>(List.of("(")));
        validTransitions.put("WHILE", new LinkedList<>(List.of("(")));
        validTransitions.put("IF", new LinkedList<>(List.of("(")));
        validTransitions.put("ELSE", new LinkedList<>(List.of("{")));

        LinkedList<String> transitionsForLiterals = new LinkedList<>(List.of(";", ")"));
        transitionsForLiterals.addAll(Arrays.asList(operators));
        transitionsForLiterals.remove(11);
        for (String literal : literals) {
            validTransitions.put(literal, transitionsForLiterals);
        }

        LinkedList<String> transitionsForCurlyBraces = new LinkedList<>(List.of("{", "}", "FOR", "WHILE", "IF", "ID"));
        transitionsForCurlyBraces.addAll(Arrays.asList(dataTypes));
        validTransitions.put("{", transitionsForCurlyBraces);
        validTransitions.put("}", transitionsForCurlyBraces);

        LinkedList<String> transitionsForOpParenthesis = new LinkedList<>(List.of("TRUE", "FALSE", "STRING", "INTEGER", "REAL", "ID", "(", ")"));
        transitionsForOpParenthesis.addAll(Arrays.asList(operators));
        transitionsForOpParenthesis.addAll(Arrays.asList(dataTypes));
        validTransitions.put("(", transitionsForOpParenthesis);

        LinkedList<String> transitionsForClosingParenthesis = new LinkedList<>(List.of(")", "{", ";"));
        transitionsForClosingParenthesis.addAll(Arrays.asList(operators));
        validTransitions.put(")", transitionsForClosingParenthesis);

        validTransitions.put(";", transitionsForCurlyBraces);

        LinkedList<String> transitionsForIds = new LinkedList<>(List.of(";", ")"));
        transitionsForIds.addAll(Arrays.asList(operators));
        validTransitions.put("ID", transitionsForIds);

        LinkedList<String> transitionsForOperators = new LinkedList<>(List.of("(", "ID"));
        transitionsForOperators.addAll(Arrays.asList(literals));
        for (String operator : operators) {
            validTransitions.put(operator, transitionsForOperators);
        }
    }


    public void analyze() {
        Stack<Token> equivalentPunctuation = new Stack<>();
        HashMap<String, String> punctuationsPair = new HashMap<>();
        punctuationsPair.put(")", "(");
        punctuationsPair.put("}", "{");

        // All tokens have a next valid transition
        for (int i = 0; i < tokens.size() - 1; i++) {
            Token currentToken = tokens.get(i), nextToken = tokens.get(i + 1);

            if (currentToken.type.equals("(") || currentToken.type.equals("{")) {
                equivalentPunctuation.push(currentToken);
            }

            if (currentToken.type.equals(")") || currentToken.type.equals("}")) {
                if (equivalentPunctuation.empty() || !punctuationsPair.get(currentToken.type).equals(equivalentPunctuation.peek().type)) {
                    reportError(currentToken);
                }
                else {
                    equivalentPunctuation.pop();
                }
            }

            String tokenKey = (List.of("KEYWORD", "PUNCTUATOR", "OPERATOR").contains(currentToken.type)) ? (String)currentToken.value : currentToken.type;
            String nextTokenKey = (List.of("KEYWORD", "PUNCTUATOR", "OPERATOR").contains(nextToken.type)) ? (String)nextToken.value : nextToken.type;
            if (!validTransitions.get(tokenKey).contains(nextTokenKey)) {
                reportError(currentToken);
            }
        }

        // Check that all (, [ or { have their ), ] or }
        // The common case is one { token in the stack for the unchecked last token }, if is the case do not report and error
        if (!equivalentPunctuation.empty() && !equivalentPunctuation.peek().type.equals("{")) {
            reportError(equivalentPunctuation.peek());
        }

        // Check if last token (no further transition token) is a valid final token
        Token finalToken = tokens.get(tokens.size() - 1);
        if (!finalToken.value.equals(";") && !finalToken.value.equals("}")) {
            reportError(finalToken);
        }
    }

    private void reportError(Token token) {
        System.out.println("\033[1m\033[31mLine " + token.atLine + ": \033[0mSyntax error found in " + token.type);
        this.error = true;
    }
}
