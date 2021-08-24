package com.compiler.lexical;

import java.util.*;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private HashSet<String> keywords;
    private HashSet<String> operators;
    private HashSet<String> punctuations;
    //TODO: private HashMap<Integer, String> symbolTable;

    public LexicalAnalyzer() {
        initializeTokens();
    }

    private void initializeTokens() {
        // Keywords
        keywords = new HashSet<>();
        String[] acceptedKeywords = new String[]{"break", "char", "double", "else", "float", "for", "if", "int", "void", "while"};
        keywords.addAll(Arrays.asList(acceptedKeywords));

        // Operators (arithmetical, assignment, relational, logical)
        operators = new HashSet<>();
        String[] acceptedOperators = new String[] {"+", "-", "*", "/", "%", "=", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "!"};
        operators.addAll(Arrays.asList(acceptedOperators));

        // Punctuations
        punctuations = new HashSet<>();
        String[] acceptedPunctuation = new String[] {"{", "}", "(", ")", "[", "]", ";" };
        punctuations.addAll(Arrays.asList(acceptedPunctuation));
    }

    public void analyze(String input) {
        List<String> lexemes = getLexemes(input);

        //TODO: Optimize for different type of tokens, add matched lexemes to symbol table
        lexemes.forEach(lexeme -> {
            if (keywords.contains(lexeme)) {
                System.out.println(lexeme + " is a keyword of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (operators.contains(lexeme)) {
                System.out.println(lexeme + " is an operator of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (punctuations.contains(lexeme)) {
                System.out.println(lexeme + " is a punctuation of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (isIntegerNumberLiteral(lexeme)) {
                System.out.println(lexeme + " is an integer number literal of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (isRealNumberLiteral(lexeme)) {
                System.out.println(lexeme + " is a real number literal of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (isStringLiteral(lexeme)) {
                System.out.println(lexeme + " is a string literal of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else if (isValidIdentifier(lexeme)) {
                System.out.println(lexeme + " is an identifier of " + lexeme.length() + " character" + (lexeme.length() > 1 ? "s." : '.'));
            }
            else {
                System.out.println(lexeme + " is not defined");
            }
        });
    }

    private LinkedList<String> getLexemes(String sourceCode) {
        LinkedList<String> lexemes = new LinkedList<>();
        StringBuilder lexeme = new StringBuilder();
        char currentChar;

        for (int i = 0; i < sourceCode.length(); ++i) {
            currentChar = sourceCode.charAt(i);

            if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
                if (lexeme.length() > 0) {
                    lexemes.add(lexeme.toString());
                    lexeme.setLength(0);
                }
            }
            else if (currentChar == ';') {
                if (lexeme.length() > 0) {
                    lexemes.add(lexeme.toString());
                    lexeme.setLength(0);
                }
                lexemes.add(String.valueOf(currentChar));
            }
            else {
                lexeme.append(currentChar);
            }
        }

        if (lexeme.length() > 0)
            lexemes.add(lexeme.toString());

        return lexemes;
    }

    private boolean isIntegerNumberLiteral(String lexeme) {
        return Pattern.matches("-?\\d+?", lexeme);
    }

    private boolean isRealNumberLiteral(String lexeme) {
        return Pattern.matches("-?\\d+([.]\\d+)?", lexeme);
    }

    private boolean isStringLiteral(String lexeme) {
        if (lexeme.length() < 2) return false;

        return lexeme.charAt(0) == '"' && lexeme.charAt(lexeme.length() - 1) == '"';
    }

    private boolean isValidIdentifier(String lexeme) {
        return Pattern.matches("[a-z_]+[a-zA-Z0-9_]*", lexeme);
    }
}
