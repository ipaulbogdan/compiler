package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Enum.Atom;

public class IntegerToken extends Token {

    private int intNumber;

    public IntegerToken(Atom code, int intNumber) {
        super(code, CompilerController.getLine());
        this.intNumber = intNumber;
    }

    @Override
    public String toString() {
        return line + ":" + getCode() + " " + intNumber;
    }
}
