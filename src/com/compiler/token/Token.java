package com.compiler.token;

public class Token {
    String type;
    Object value;
    int atLine;

    public Token(String type, Object value, int atLine) {
        this.type = type;
        this.value = value;
        this.atLine = atLine;
    }

    @Override
    public String toString() {
        return this.value != null ? type + '(' + value.toString() + ')' : type;
    }
}
