package com.idorasi.compiler.modules.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Atoms;

public class StringToken extends Token {

    private String text;

    public StringToken(Atoms code,String text) {
        super(code, CompilerController.getLine());
        this.text = text;
    }


    @Override
    public String toString() {
        return line + ":" + super.getCode() + " " + text;
    }
}
