package com.compiler;

import com.compiler.frontend.lexical.LexicalAnalyzer;
import com.compiler.frontend.syntax.SyntaxAnalyzer;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // Open file and add all the lines of code in a list
        Scanner scanner = new Scanner(new FileReader("C:\\Users\\snvc0\\Documents\\Repositories\\cpp-compiler\\main.cpm"));
        LinkedList<String> sourceFile = new LinkedList<>();
        while (scanner.hasNextLine()) {
            sourceFile.add(scanner.nextLine());
        }

        // Run lexical analysis and check if there was an error
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceFile);
        lexicalAnalyzer.analyze();
        if (lexicalAnalyzer.error) {
            return;
        }
        System.out.print("\033[1m\033[32mLexical analysis completed with no errors\033[0m\nTokens: ");
        lexicalAnalyzer.tokens.forEach(token -> {
            System.out.print(token.toString() + ' ');
        });
        System.out.println("\b");

        // Run syntax analysis and check if there was an error
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
        syntaxAnalyzer.analyze();
        if (syntaxAnalyzer.error) {
            return;
        }
        System.out.print("\033[1m\033[32mSyntax analysis completed with no errors\033[0m\n");
    }
}
