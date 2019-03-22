package com.idorasi.compiler.utiles;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.modules.Atoms;

public class SimpleToken extends Token{


    public SimpleToken(Atoms code) {
        super(code, CompilerController.getLine());
    }

    @Override
    public String toString() {
        return  line + ":" + super.getCode();
    }
}
