package com.idorasi.compiler.modules;

import com.idorasi.compiler.exceptions.missingTokenException;
import com.idorasi.compiler.utiles.Enum.Atom;
import com.idorasi.compiler.utiles.Cache;
import com.idorasi.compiler.utiles.Tokens.Token;

import java.util.List;

import static com.idorasi.compiler.utiles.Enum.Atom.*;

public class SyntacticAnalyzer {

    private List<Token> tokens;;

    public SyntacticAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
    }


    public boolean start(){
        try{
            unit();
        }catch (Exception e){
            System.out.println(e);
            return false;
        }

        return true;
    }


    private boolean consume(Atom code) {
        if(tokens.get(0).getCode() == code) {
            tokens.remove(0);
            return true;
        }

        return false;
    }

    private boolean unit() throws missingTokenException{

        for(;;){

            if(declStruct()){

            } else if(declFunct()){

            } else if(declVar()){

            } else break;

        }
        return consume(END);
    }

    private boolean declStruct() {
        Cache cache = new Cache(tokens);
        if(consume(STRUCT)){
            if(consume(ID)){
                if(consume(LACC)){
                   while(declVar()) {

                   }
                   if(consume(RACC)){
                        if(consume(SEMICOLON)){
                            return true;
                        }else throw new missingTokenException(tokens.get(0),"Missing SEMICOLON after STRUCT statement");
                    } else throw new missingTokenException(tokens.get(0),"Missing RACC after declVar");
                } else throw  new missingTokenException(tokens.get(0),"Missing LACC after ID");
            }else throw new missingTokenException(tokens.get(0),"Missing ID after STRUCT");
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean declVar() {
        Cache cache = new Cache(tokens);

        if(typeBase()){
            if(consume(ID)){
                arrayDecl();
                for(;;){
                    if(consume(COMMA)){
                        if(consume(ID)){
                            arrayDecl();
                        }else throw new missingTokenException(tokens.get(0),"Missing ID after COMMA");
                    } else break;
                }
                if(consume(SEMICOLON)){
                    return true;

                }throw new missingTokenException(tokens.get(0),"Missing SEMICOLON after ID");
            }else throw new missingTokenException(tokens.get(0),"Missing ID after typeBase");
        }

        tokens = cache.restoreCache();

        return false;
    }

    private void arrayDecl() {
        if(consume(LBRACKET)){
            expr();
            if(consume(RBRACKET)){
            }else throw new missingTokenException(tokens.get(0),"Missing RBRACKET");
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
        Cache cache = new Cache(tokens);

        if(consume(INT) || consume(DOUBLE) || consume(CHAR)){
            return true;
        }else if(consume(STRUCT)){
            if(consume(ID)){
                return true;
            }
        }

        tokens = cache.restoreCache();
       
        return false;
    }

    private boolean declFunct() {
        Cache cache = new Cache(tokens);
        boolean isFunc= false;
        if(typeBase()){
            consume(MUL);
            isFunc = true;
        }else if(consume(VOID)){
            isFunc = true;
        }else return false;
        if(isFunc) {
            if (consume(ID)) {
                if (consume(LPAR)) {
                    if (functArg()) {
                        while (consume(COMMA)) {
                            if (functArg()) {
                            }
                        }
                    } else {
                        if (consume(RPAR)) {
                            if (stmCompound()) {
                                return true;
                            } else throw new missingTokenException(tokens.get(0), "Missing stmCompound");
                        } else throw new missingTokenException(tokens.get(0), "Missing RPAR in declFunct()");
                    }
                } else throw new missingTokenException(tokens.get(0), "Missing LPAR in declFunct()");
            } throw new missingTokenException(tokens.get(0),"Missing function ID");
        }


        tokens = cache.restoreCache();
        
        return false;
    }

    private boolean stmCompound() {
        Cache cache = new Cache(tokens);
        if(consume(LACC)) {
            for (; ; ) {
                if (declVar() || stm()) {

                } else break;
            }
            if(consume(RACC)){
                return true;
            }else throw new missingTokenException(tokens.get(0),"Missing RACC in stmCompound");
        }
        tokens = cache.restoreCache();
        
        return false;
    }

    private boolean functArg() {
        Cache cache = new Cache(tokens);
        if(typeBase()){
            if(consume(ID)){
                arrayDecl();
                return true;
            }else throw new missingTokenException(tokens.get(0),"Missing ID in functArg");
        }
        tokens = cache.restoreCache();
        
        return false;
    }


    private boolean stm() {
        Cache cache = new Cache(tokens);
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
                                    }else throw new missingTokenException(tokens.get(0),"Expecting statement after ELSE");
                                }
                                return true;
                            }else throw new missingTokenException(tokens.get(0),"Missing statement after IF");
                        }else throw new missingTokenException(tokens.get(0),"Missing RPAR");
                    }else throw new missingTokenException(tokens.get(0),"Missing EXPR in IF");
                }else throw new missingTokenException(tokens.get(0),"Missing LPAR afer IF");
        }else if(consume(WHILE)){
            if(consume(LPAR)){
                if(expr()){
                    if(consume(RPAR)){
                        if(stm()){
                            return true;
                        }else throw new missingTokenException(tokens.get(0),"Missing Statement after WHILE");
                    }else throw new missingTokenException(tokens.get(0),"Missing RPAR");
                }else throw new missingTokenException(tokens.get(0),"Missing EXPR");
            }else throw new missingTokenException(tokens.get(0),"Missing LPAR");
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
                            }else throw new missingTokenException(tokens.get(0),"Missing Statement after FOR");
                        }else throw new missingTokenException(tokens.get(0),"Missing RPAR");

                    }else throw new missingTokenException(tokens.get(0),"Missing SEMICOLON");
                }else throw  new missingTokenException(tokens.get(0),"Missing SEMICOLON");
            }else throw new missingTokenException(tokens.get(0),"Missing LPAR");

        }else if(consume(BREAK)){
            if(consume(SEMICOLON)){
                return true;
            }else throw new missingTokenException(tokens.get(0),"Missing SEMICOLON");
        }else if(consume(RETURN)){
            expr();
            if(consume(SEMICOLON)){
                return true;
            }else throw new missingTokenException(tokens.get(0),"Missing SEMICOLON after RETURN");
        }else{
            expr();
            if(consume(SEMICOLON)){
                return true;
            }
        }

        tokens = cache.restoreCache();
        return false;

    }

    private boolean expr(){
        if(exprAssign()){
            return true;
        }
        return false;
    }

    private boolean exprAssign(){
        Cache cache = new Cache(tokens);
        if(exprUnary()){
            if(consume(ASSIGN)){
                if(exprAssign()){
                    return true;
                }
            }
        }
        if(exprOr()){
                return true;

        }
        tokens = cache.restoreCache();

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
        Cache cache = new Cache(tokens);
        if(exprAnd()){
            if(exprOrPrim()){
                return true;
            }
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprAndPrim() {
        if(consume(AND)){
            if(exprEq()){
                if(exprAndPrim()){
                    return true;
                }
            }throw new missingTokenException(tokens.get(0),"Missing operand after AND");
        }

        return true;
    }

    private boolean exprAnd() {
        Cache cache = new Cache(tokens);
        if(exprEq()){
            if(exprAndPrim()){
                return true;

            }
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprEqPrim() {
        if(consume(EQUAL) || consume(NOTEQ)){
            if(exprRel()){
                if(exprEqPrim()){
                    return true;
                }
            }else throw new missingTokenException(tokens.get(0),"Missing operand after eq operand");
        }
        return true;
    }

    private boolean exprEq() {
        Cache cache = new Cache(tokens);
        if(exprRel()){
            if(exprEqPrim()){
                return true;
            }
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprRelPrim() {
        if(consume(LESS) || consume(LESSEQ) || consume(GREATER) || consume(GREATEREQ)){
            if(exprAdd()){
                if(exprRelPrim()){
                    return true;
                }
            }else throw new missingTokenException(tokens.get(0),"Missing operand");
        }

        return true;
    }

    private boolean exprRel() {
        Cache cache = new Cache(tokens);
        if(exprAdd()){
            if(exprRelPrim()){
                return true;
            }
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprAddPrim() {
        if(consume(ADD) || consume(SUB)){
            if(exprMul()){
                if(exprAddPrim()){
                    return true;
                }
            }throw new missingTokenException(tokens.get(0),"Missing operand after + or -");
        }
        return true;
    }

    private boolean exprAdd() {
        Cache cache = new Cache(tokens);
        if(exprMul()){
            if(exprAddPrim()){
                return true;
            }
        }
        tokens = cache.restoreCache();

        return true;
    }

    private boolean exprMulPrim() {
        if(consume(MUL) || consume(DIV)){
            if(exprCast()){
                if(exprMulPrim()){
                    return true;
                }
            }else throw new missingTokenException(tokens.get(0),"Missing operand after * or /");
        }
        return true;
    }

    private boolean exprMul() {
        Cache cache = new Cache(tokens);
        if(exprCast()){
            if(exprMulPrim()){
                return true;
            }
            else throw new missingTokenException(tokens.get(0),"Missing exprMul");
        }

        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprCast() {
        Cache cache = new Cache(tokens);
        if(consume(LPAR)){
            if(typeName()){
                if(consume(RPAR)){
                    if(exprCast()){
                        return true;
                    }
                }else throw new missingTokenException(tokens.get(0),"Missing RPAR");
            }else throw new missingTokenException(tokens.get(0),"Missing typeName");
        } else if (exprUnary()) {
            return true;
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprUnary() {
        Cache cache = new Cache(tokens);
        if(consume(SUB) || consume(NOT)){
            if(exprUnary()){
                return true;
            }
            }else if(exprPostfix()){
            return true;
        }
        tokens = cache.restoreCache();

        return false;
    }

    private boolean exprPostfixPrim() {
        if(consume(LBRACKET)){
            if(expr()){
                if(consume(RBRACKET)){
                    if(exprPostfixPrim()){
                        return true;
                    }
                }else throw new missingTokenException(tokens.get(0),"Missing RBRACKET");
            }
        }else if(consume(DOT)){
            if(consume(ID)){
                if(exprPostfixPrim()){
                    return true;
                }
            }
        }
        return true;
    }

    private boolean exprPostfix() {
        Cache cache = new Cache(tokens);
        if(exprPrimary()){
            if(exprPostfixPrim()){
                return true;
            }

            throw new missingTokenException(tokens.get(0),"Error in postFix");
        }

        tokens = cache.restoreCache();

        return false;

    }

    private boolean exprPrimary() {
        Cache cache = new Cache(tokens);
        if(consume(ID)){
            if(consume(LPAR)){
                if(expr()){
                    while(consume(COMMA)){
                        if(!expr())
                            return false;
                    }
                }
                if(consume(RPAR)){
                    return true;
                }
            }
            return true;
        }
        if(consume(CT_INT) || consume(CT_REAL)|| consume(CT_CHAR) || consume(CT_STRING)) {
            return true;
        }
        if(consume(LPAR)) {
            if (expr()) {
                if (consume(RPAR)) {
                    return true;
                }
            }
        }

        tokens = cache.restoreCache();

        return false;

    }
}
