package com.compiler.token;

public class Token {
    public String type;
    public Object value;
    public int atLine;

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
