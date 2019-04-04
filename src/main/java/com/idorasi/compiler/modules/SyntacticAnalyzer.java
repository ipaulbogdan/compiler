package com.idorasi.compiler.modules;

import com.idorasi.compiler.utiles.Atom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.idorasi.compiler.utiles.Atom.*;

public class SyntacticAnalyzer {

    private List<Atom> tokens = new ArrayList<>();
    private Iterator<Atom> atomIterator;

    public SyntacticAnalyzer(List<Atom> tokens) {
        this.tokens = tokens;
        atomIterator = this.tokens.iterator();
        if(atomIterator.hasNext()){
            atomIterator.next();
        }
    }


    private boolean unit(){
        for(;;){

            if(declStruct()){}
            else if(declFunct()){}
            else if(declVar()){}
            else break;

        }
        return consume(END);

    }

    private boolean consume(Atom atom) {
        return false;
    }

    private boolean declStruct() {
        return false;
    }

    private boolean declFunct() {
        return false;
    }

    private boolean declVar() {
        return false;
    }
}




