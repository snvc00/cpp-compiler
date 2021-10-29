package com.compiler.frontend.lexical;

import com.compiler.token.Token;

import java.util.*;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private final String[] sourceFile;
    private final HashSet<String> keywords;
    private final HashSet<String> operators;
    private final HashSet<String> punctuations;
    public LinkedList<Token> tokens;
    public HashMap<String, String> declaredVariables;
    public boolean error;

    public LexicalAnalyzer(LinkedList<String> sourceFile) {
        this.sourceFile = sourceFile.toArray(new String[0]);
        this.tokens = new LinkedList<>();
        this.declaredVariables = new HashMap<>();
        this.error = false;

        // Keywords
        keywords = new HashSet<>();
        keywords.addAll(Arrays.asList("void", "bool", "char", "int", "float", "double", "string", "for", "while", "if", "else", "break", "return", "true", "false"));

        // Operators (arithmetical, assignment, relational, logical)
        operators = new HashSet<>();
        operators.addAll(Arrays.asList("+", "-", "*", "/", "%", "=", "<", ">", "<=", ">=", "==", "!=", "&&", "||"));

        // Punctuations
        punctuations = new HashSet<>();
        punctuations.addAll(Arrays.asList("{", "}", "(", ")", "[", "]", ";"));
    }

    public void analyze() {
        int currentLine;
        for (int i = 0; i < sourceFile.length; i++) {
            String[] lexemes = getLexemes(sourceFile[i]).toArray(new String[0]);

            currentLine = i + 1;
            for (int j = 0; j < lexemes.length; j++) {
                if (keywords.contains(lexemes[j])) {
                    tokens.add(new Token("KEYWORD", lexemes[j].toUpperCase(), currentLine));
                }
                else if (operators.contains(lexemes[j])) {
                    tokens.add(new Token("OPERATOR", lexemes[j], currentLine));
                }
                else if (punctuations.contains(lexemes[j])) {
                    tokens.add(new Token("PUNCTUATOR", lexemes[j], currentLine));
                }
                else if (isInteger(lexemes[j])) {
                    tokens.add(new Token("INTEGER", Integer.parseInt(lexemes[j]), currentLine));
                }
                else if (isReal(lexemes[j])) {
                    tokens.add(new Token("REAL", Double.parseDouble(lexemes[j]), currentLine));
                }
                else if (isString(lexemes[j])) {
                    tokens.add(new Token("STRING_LITERAL", lexemes[j], currentLine));
                }
                else if (isValidIdentifier(lexemes[j])) {
                    List<String> acceptedDataTypes = Arrays.asList("BOOL", "CHAR", "INT", "FLOAT", "DOUBLE", "STRING");
                    if (acceptedDataTypes.contains((String)tokens.getLast().value)) {
                        declaredVariables.put(lexemes[j], (String)tokens.getLast().value);
                    }

                    tokens.add(new Token("ID", lexemes[j], currentLine));
                }
                else {
                    System.out.println("\033[1m\033[31mLine " + currentLine + ": \033[0mLexical error found in " + lexemes[j]);
                    this.error = true;
                    return;
                }
            }
        }
    }

    private LinkedList<String> getLexemes(String line) {
        LinkedList<String> lexemes = new LinkedList<>();
        StringBuilder lexeme = new StringBuilder();
        char currentChar;

        for (int i = 0; i < line.length(); ++i) {
            currentChar = line.charAt(i);

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

    private boolean isInteger(String lexeme) {
        return Pattern.matches("-?\\d+?", lexeme);
    }

    private boolean isReal(String lexeme) {
        return Pattern.matches("-?\\d+([.]\\d+)?", lexeme);
    }

    private boolean isString(String lexeme) {
        if (lexeme.length() < 2) return false;

        return lexeme.charAt(0) == '"' && lexeme.charAt(lexeme.length() - 1) == '"';
    }

    private boolean isValidIdentifier(String lexeme) {
        return Pattern.matches("[a-z_]+[a-zA-Z0-9_]*", lexeme);
    }
}
