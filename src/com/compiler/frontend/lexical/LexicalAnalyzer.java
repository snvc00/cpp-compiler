package com.compiler.frontend.lexical;

import com.compiler.token.Token;

import java.util.*;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private final String[] sourceFile;
    private final HashSet<String> keywords;
    private final HashSet<String> operators;
    private final HashSet<String> punctuations;
    public final String[] acceptedKeywords = new String[]{
            "void",
            "bool",
            "char",
            "int",
            "float",
            "double",
            "for",
            "while",
            "if",
            "else",
            "break",
            "return",
            "true",
            "false"
    };
    public final String[] acceptedOperators = new String[] {"+", "-", "*", "/", "%", "=", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "!"};
    public final String[] acceptedPunctuations = new String[] {"{", "}", "(", ")", "[", "]", ";" };
    public LinkedList<Token> tokens;
    public boolean error;

    public LexicalAnalyzer(LinkedList<String> sourceFile) {
        this.sourceFile = sourceFile.toArray(new String[0]);
        this.tokens = new LinkedList<>();
        this.error = false;

        // Keywords
        keywords = new HashSet<>();
        keywords.addAll(Arrays.asList(this.acceptedKeywords));

        // Operators (arithmetical, assignment, relational, logical)
        operators = new HashSet<>();
        operators.addAll(Arrays.asList(this.acceptedOperators));

        // Punctuations
        punctuations = new HashSet<>();
        punctuations.addAll(Arrays.asList(this.acceptedPunctuations));
    }

    public void analyze() {
        int currentLine;
        for (int i = 0; i < sourceFile.length; i++) {
            String[] lexemes = getLexemes(sourceFile[i]).toArray(new String[0]);

            currentLine = i + 1;
            for (int j = 0; j < lexemes.length; j++) {
                if (keywords.contains(lexemes[j])) {
                    tokens.add(new Token(lexemes[j].toUpperCase(), null, currentLine));
                }
                else if (operators.contains(lexemes[j]) || punctuations.contains(lexemes[j])) {
                    tokens.add(new Token(lexemes[j], null, currentLine));
                }
                else if (isInteger(lexemes[j])) {
                    tokens.add(new Token("NUMBER", Integer.parseInt(lexemes[j]), currentLine));
                }
                else if (isReal(lexemes[j])) {
                    tokens.add(new Token("REAL", Double.parseDouble(lexemes[j]), currentLine));
                }
                else if (isString(lexemes[j])) {
                    tokens.add(new Token("STRING", lexemes[j], currentLine));
                }
                else if (isValidIdentifier(lexemes[j])) {
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
