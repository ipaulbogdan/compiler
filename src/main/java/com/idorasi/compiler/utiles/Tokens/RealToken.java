package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Enum.Atom;

public class RealToken extends Token{

     private double dNumber;

    public RealToken(Atom code, double dNumber) {
        super(code, CompilerController.getLine());
        this.dNumber = dNumber;
    }

    @Override
    public String toString() {
        return line + ":" + super.getCode() + " " + this.dNumber;
    }
}
