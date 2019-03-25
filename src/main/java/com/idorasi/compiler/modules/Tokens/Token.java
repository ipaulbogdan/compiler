package com.idorasi.compiler.modules.Tokens;

import com.idorasi.compiler.utiles.Atoms;

abstract public class Token {

    private Atoms code;
    protected int line;

    public Token(Atoms code,int line){
        this.code = code;
        this.line = line;
    }

    public Atoms getCode() {
        return code;
    }



    abstract public String toString();
}
