package com.idorasi.compiler.utiles;

import com.idorasi.compiler.utiles.Tokens.Token;

import java.util.List;

public class Cache {

    private List<Token> tokens;

    public Cache(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Token> restoreCache(){
        return tokens;
    }

}
