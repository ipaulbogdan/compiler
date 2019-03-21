package com.idorasi.compiler.utiles;

import com.idorasi.compiler.modules.Atoms;

public class Token {

    private Atoms code;
    private Union union;
    private int line;

    public Token(Atoms code){
        this.code = code;
        this.union = new Union();
        this.line = 0;
    }

    public Atoms getCode() {
        return code;
    }

    public int getInt(){
        return union.getInteger();
    }

    public String getText(){
        return union.getText();
    }

    public double getReal(){
        return union.getReal();
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line){
        this.line = line;
    }

    public void setInteger(int integer){
        union.setInteger(integer);
    }

    public void setText(String text){
        this.union.setText(text);
    }

    public void setDouble(double real){
        this.union.setReal(real);
    }
}
