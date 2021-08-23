package com.compiler;

import com.compiler.lexical.LexicalAnalyzer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        String input = "";

        System.out.println("Lexical analyzer, .exit to end");
        while (!input.equals(".exit")) {
            System.out.print("Input: ");
            input = scanner.nextLine();

            if (!input.equals(".exit"))
                lexicalAnalyzer.analyze(input);
        }
    }
}
