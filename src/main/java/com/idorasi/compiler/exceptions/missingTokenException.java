package com.idorasi.compiler.exceptions;

import com.idorasi.compiler.utiles.Tokens.Token;

public class missingTokenException extends RuntimeException {

    private Token tk;
    private String message;

    public missingTokenException(Token tk,String message) {
        this.message = message;
        this.tk = tk;
    }

    public String toString(){
        return "At line:"+tk.getLine()+ " : "+message;
    }
}
