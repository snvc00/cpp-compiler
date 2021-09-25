package com.compiler.frontend.syntax;

import com.compiler.frontend.lexical.LexicalAnalyzer;
import com.compiler.token.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class SyntaxAnalyzer {
    LinkedList<Token> tokens;
    HashMap<Token, LinkedList<Token>> validTransitions;
    public boolean error;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.tokens = lexicalAnalyzer.tokens;
        validTransitions = new HashMap<>();
        this.error = false;
    }

    public void analyze() {
        Stack<Token> equivalentPunctuation = new Stack<>();

        // All tokens have a next valid transition

        // Check that all (, [ or { have their ), ] or }

    }
}
