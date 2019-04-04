package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.utiles.Atom;

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



    abstract public String toString();
}
