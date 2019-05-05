package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.utiles.Enum.Atom;

abstract public class Token {

    private Atom code;
    protected int line;

    public Token(Atom code, int line){
        this.code = code;
        this.line = line;
    }

    public Atom getCode() {
        return code;
    }

    public int getLine(){return line;}



    abstract public String toString();
}
