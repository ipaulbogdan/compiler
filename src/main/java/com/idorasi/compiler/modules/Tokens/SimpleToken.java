package com.idorasi.compiler.modules.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Atoms;

public class SimpleToken extends Token{


    public SimpleToken(Atoms code) {
        super(code, CompilerController.getLine());
    }

    @Override
    public String toString() {
        return  line + ":" + super.getCode();
    }
}
