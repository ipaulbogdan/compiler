package com.idorasi.compiler.utiles.Tokens;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Atom;

public class SimpleToken extends Token{


    public SimpleToken(Atom code) {
        super(code, CompilerController.getLine());
    }

    @Override
    public String toString() {
        return  line + ":" + super.getCode();
    }
}
