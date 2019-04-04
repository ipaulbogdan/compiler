package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Atom;

public class StringToken extends Token {

    private String text;

    public StringToken(Atom code, String text) {
        super(code, CompilerController.getLine());
        this.text = text;
    }


    @Override
    public String toString() {
        return line + ":" + super.getCode() + " " + text;
    }
}
