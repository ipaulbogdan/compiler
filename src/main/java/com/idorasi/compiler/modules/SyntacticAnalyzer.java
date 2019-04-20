package com.idorasi.compiler.modules;

import com.idorasi.compiler.exceptions.missingTokenException;
import com.idorasi.compiler.utiles.Atom;
import com.idorasi.compiler.utiles.Tokens.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.idorasi.compiler.utiles.Atom.*;

public class SyntacticAnalyzer {

    private List<Token> tokens = new ArrayList<>();
    private List<Token> tokenCache;
    private Iterator<Token> atomIterator;
    private Iterator<Token> cachedIterator = null;

    public SyntacticAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
        atomIterator = this.tokens.iterator();
        unit();
    }


    private boolean consume(Atom code) {
        if(atomIterator.hasNext()){
            if(atomIterator.next().getCode()==code) {
                atomIterator.remove();
                return true;
            }
        }
        return false;
    }

    private boolean unit(){

        for(;;){

            if(declStruct()){

            } else if(declFunct()){

            } else if(declVar()){

            } else break;

        }
        return consume(END);
    }

    private boolean declStruct() {
        cache();
        if(consume(STRUCT)){
            if(consume(ID)){
                if(consume(LACC)){
                   while(declVar()) {

                   }
                   if(consume(RACC)){
                        if(consume(SEMICOLON)){
                            return true;
                        }else throw new missingTokenException(atomIterator.next(),"Missing SEMICOLON after STRUCT statement");
                    } else throw new missingTokenException(atomIterator.next(),"Missing RACC after declVar");
                } else throw  new missingTokenException(atomIterator.next(),"Missing LACC after ID");
            }else throw new missingTokenException(atomIterator.next(),"Missing ID after STRUCT");
        }
        restoreFromCache();
        return false;
    }

    private boolean declVar() {
        cache();

        if(typeBase()){
            if(consume(ID)){
                arrayDecl();
                for(;;){
                    if(consume(COMMA)){
                        if(consume(ID)){
                            arrayDecl();
                        }else throw new missingTokenException(atomIterator.next(),"Missing ID after COMMA");
                    } else break;
                }
                if(consume(SEMICOLON)){
                    return true;

                }throw new missingTokenException(atomIterator.next(),"Missing SEMICOLON after ID");
            }else throw new missingTokenException(atomIterator.next(),"Missing ID after typeBase");
        }

        restoreFromCache();
        return false;
    }

    private void arrayDecl() {
        if(consume(LBRACKET)){
            expr();
            if(consume(RBRACKET)){
            }else throw new missingTokenException(atomIterator.next(),"Missing RBRACKET");
        }
    }

    private boolean typeName(){
        if(typeBase()){
            arrayDecl();

            return true;
        }
        return false;
    }

    private boolean typeBase(){
        cache();

        if(consume(INT) || consume(DOUBLE) || consume(CHAR)){
            return true;
        }else if(consume(STRUCT)){
            if(consume(ID)){
                return true;
            }
        }

        restoreFromCache();
        return false;
    }

    private boolean declFunct() {
        cache();
        if(typeBase()){
            consume(MUL);
        }else if(consume(VOID)){
        }else return false;
        if(consume(ID)){
            if(consume(LPAR)){
                if(functArg()){
                    while(consume(COMMA)){
                        if(functArg()){};
                    }
                }else{
                    if(consume(RPAR)){
                        if(stmCompound()){
                            return true;
                        } else throw new missingTokenException(atomIterator.next(),"Missing stmCompound");
                    }else throw new missingTokenException(atomIterator.next(),"Missing RPAR in declFunct()");
                }
            }
        }

        restoreFromCache();

        return false;
    }

    private boolean stmCompound() {
        cache();
        if(consume(LACC)) {
            for (; ; ) {
                if (declVar() | stm()) {

                } else break;
            }
            if(consume(RACC)){
                return true;
            }else throw new missingTokenException(atomIterator.next(),"Missing RACC in stmCompound");
        }
        restoreFromCache();
        return false;
    }

    private boolean functArg() {
        cache();
        if(typeBase()){
            if(consume(ID)){
                arrayDecl();
                return true;
            }else throw new missingTokenException(atomIterator.next(),"Missing ID in functArg");
        }
        restoreFromCache();
        return false;
    }


    private boolean stm() {
        cache();
        if(stmCompound()){
            return true;
        }else if(consume(IF)){
                if(consume(LPAR)){
                    if(expr()){
                        if(consume(RPAR)){
                            if(stm()){
                                if(consume(ELSE)){
                                    if(stm()){
                                        return true;
                                    }else throw new missingTokenException(atomIterator.next(),"Expecting statement after ELSE");
                                }
                                return true;
                            }else throw new missingTokenException(atomIterator.next(),"Missing statement after IF");
                        }else throw new missingTokenException(atomIterator.next(),"Missing RPAR");
                    }else throw new missingTokenException(atomIterator.next(),"Missing EXPR in IF");
                }else throw new missingTokenException(atomIterator.next(),"Missing LPAR afer IF");
        }else if(consume(WHILE)){
            if(consume(LPAR)){
                if(expr()){
                    if(consume(RPAR)){
                        if(stm()){
                            return true;
                        }else throw new missingTokenException(atomIterator.next(),"Missing Statement after WHILE");
                    }else throw new missingTokenException(atomIterator.next(),"Missing RPAR");
                }else throw new missingTokenException(atomIterator.next(),"Missing EXPR");
            }else throw new missingTokenException(atomIterator.next(),"Missing LPAR");
        }else if(consume(FOR)){
            if(consume(LPAR)){
                expr();
                if(consume(SEMICOLON)){
                    expr();
                    if(consume(SEMICOLON)){
                        expr();
                        if(consume(RPAR)){
                            if(stm()){
                                return true;
                            }else throw new missingTokenException(atomIterator.next(),"Missing Statement after FOR");
                        }else throw new missingTokenException(atomIterator.next(),"Missing RPAR");

                    }else throw new missingTokenException(atomIterator.next(),"Missing SEMICOLON");
                }else throw  new missingTokenException(atomIterator.next(),"Missing SEMICOLON");
            }else throw new missingTokenException(atomIterator.next(),"Missing LPAR");

        }else if(consume(BREAK)){
            if(consume(SEMICOLON)){
                return true;
            }else throw new missingTokenException(atomIterator.next(),"Missing SEMICOLON");
        }else if(consume(RETURN)){
            expr();
            if(consume(SEMICOLON)){
                return true;
            }else throw new missingTokenException(atomIterator.next(),"Missing SEMICOLON after RETURN");
        }else{
            expr();
            if(consume(SEMICOLON)){
                return true;
            }
        }

        restoreFromCache();
        return false;

    }

    private boolean expr(){
        if(exprAssign()){
            return true;
        }
        return false;
    }

    private boolean exprAssign(){
        cache();
        if(exprUnary()){
            if(consume(ASSIGN)){
                if(exprAssign() | exprOr()){
                    return true;
                }
            }else throw new missingTokenException(atomIterator.next(),"Missing ASSIGN");
        }
        restoreFromCache();
        return false;
    }
    private boolean exprOrPrim(){
        if(consume(OR)){
            if(exprAnd()){
                if(exprOrPrim()){
                    return true;
                }
            }
        }
        return true;
    }

    private boolean exprOr() {
        cache();
        if(exprAnd()){
            if(exprOrPrim()){
                return true;
            }
        }
        restoreFromCache();
        return false;
    }

    private boolean exprAndPrim() {
        if(consume(AND)){
            if(exprEq()){
                if(exprAndPrim()){
                    return true;
                }
            }throw new missingTokenException(atomIterator.next(),"Missing operand after AND");
        }

        return true;
    }

    private boolean exprAnd() {
        cache();
        if(exprEq()){
            if(exprAndPrim()){
                return true;

            }
        }
        restoreFromCache();
        return false;
    }

    private boolean exprEqPrim() {
        if(consume(EQUAL) || consume(NOTEQ)){
            if(exprRel()){
                if(exprEqPrim()){
                    return true;
                }
            }else throw new missingTokenException(atomIterator.next(),"Missing operand after eq operand");
        }
        return true;
    }

    private boolean exprEq() {
        cache();
        if(exprRel()){
            if(exprEqPrim()){
                return true;
            }
        }
        restoreFromCache();
        return false;
    }

    private boolean exprRelPrim() {
        if(consume(LESS) || consume(LESSEQ) || consume(GREATER) || consume(GREATEREQ)){
            if(exprAdd()){
                if(exprRelPrim()){
                    return true;
                }
            }else throw new missingTokenException(atomIterator.next(),"Missing operand");
        }

        return true;
    }

    private boolean exprRel() {
        cache();
        if(exprAdd()){
            if(exprRelPrim()){
                return true;
            }
        }
        restoreFromCache();
        return false;
    }

    private boolean exprAddPrim() {
        if(consume(ADD) || consume(SUB)){
            if(exprMul()){
                if(exprAddPrim()){
                    return true;
                }
            }throw new missingTokenException(atomIterator.next(),"Missing operand after + or -");
        }
        return true;
    }

    private boolean exprAdd() {
        cache();
        if(exprMul()){
            if(exprAddPrim()){
                return true;
            }
        }
        restoreFromCache();
        return true;
    }

    private boolean exprMul() {
        return false;
    }

    private boolean exprUnary() {
        return false;
    }


    private void cache(){

        tokenCache = tokens;
        cachedIterator = atomIterator;

    }

    private void restoreFromCache(){

        tokens = tokenCache;
        atomIterator = cachedIterator;

    }
}
