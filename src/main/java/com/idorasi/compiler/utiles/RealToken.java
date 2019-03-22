package com.idorasi.compiler.utiles;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.modules.Atoms;

public class RealToken extends Token{

     private double dNumber;

    public RealToken(Atoms code,double dNumber) {
        super(code, CompilerController.getLine());
        this.dNumber = dNumber;
    }

    @Override
    public String toString() {
        return line + ":" + super.getCode() + " " + this.dNumber;
    }
}
