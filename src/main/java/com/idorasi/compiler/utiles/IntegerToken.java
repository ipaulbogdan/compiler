package com.idorasi.compiler.utiles;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.modules.Atoms;

public class IntegerToken extends Token {

    private int intNumber;

    public IntegerToken(Atoms code,int intNumber) {
        super(code, CompilerController.getLine());
        this.intNumber = intNumber;
    }

    @Override
    public String toString() {
        return line + ":" + getCode() + " " + intNumber;
    }
}
