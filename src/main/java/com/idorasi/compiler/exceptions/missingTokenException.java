package com.idorasi.compiler.exceptions;

import com.idorasi.compiler.utiles.Tokens.Token;

public class missingTokenException extends RuntimeException {

    private String message;
    private Token tk;

    public missingTokenException(Token tk,String message) {
        this.tk = tk;
        this.message = message;
    }

    public String toString(){
        return "At line:"+tk.getLine()+ " : "+message;
    }
}
