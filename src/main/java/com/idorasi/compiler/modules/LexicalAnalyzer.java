package com.idorasi.compiler.modules;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.utiles.Atom;
import com.idorasi.compiler.utiles.Tokens.*;

import static com.idorasi.compiler.utiles.Atom.*;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;


public class LexicalAnalyzer {

    private String input;
    private Token tk;
    private static  int crChar;

    public LexicalAnalyzer(String input) {
        input +='\0';
        this.input = input;

    }

    public void resetCompiler(){
        crChar = 0;
    }

    public Atom getNextToken() {
        char c = 'c';
        int chStart = 0, state = 0;
        int chLenght = 0;
        for (; ; ) {
            c = input.charAt(crChar);
            switch (state) {
                case 0:
                    if (isLetter(c) || c == '_') { //looking for ID
                        chStart = crChar;
                        crChar++;
                        state = 1;
                    } else if (c == ' ' || c == '\r' || c == '\t') { //spaces
                        crChar++;
                    } else if (c == '\n') { //new line
                        CompilerController.incrementLine();
                        crChar++;
                    } else if (Character.getNumericValue(c) >= 1 && Character.getNumericValue(c) < 10) { //CT_INT zecimal start
                        chStart = crChar;
                        crChar++;
                        state = 3;
                    } else if (c - '0' == 0) {  // CT_INT octal/hexa start
                        chStart = crChar;
                        crChar++;
                        state = 5;
                    } else if (c == '"') { // CT_STRING
                        chStart = crChar;
                        crChar++;
                        state = 16;
                    } else if (c == '\'') { // CT_CHAR;
                        chStart = crChar;
                        crChar++;
                        state = 20;
                    } else if (c == ',') { // COMMA
                        crChar++;
                        state = 24;
                    } else if (c == ';') { //SEMICOLON
                        crChar++;
                        state = 25;
                    } else if (c == '(') { //LBRACKET
                        crChar++;
                        state = 26;
                    } else if (c == ')') {    // RBRACKET
                        crChar++;
                        state = 27;
                    } else if (c == ']') {
                        crChar++;
                        state = 28;
                    } else if (c == '[') {
                        crChar++;
                        state = 29;
                    } else if (c == '{') {
                        crChar++;
                        state = 30;
                    } else if (c == '}') {
                        crChar++;
                        state = 31;
                    } else if (c == '+') {
                        crChar++;
                        state = 32;
                    } else if (c == '-') {
                        crChar++;
                        state = 33;
                    } else if (c == '*') {
                        crChar++;
                        state = 34;
                    } else if (c == '/') {
                        crChar++;
                        state = 35;
                    } else if (c == '.') {
                        crChar++;
                        state = 36;
                    } else if (c == '|') {
                        crChar++;
                        state = 37;
                    } else if (c == '!') {
                        crChar++;
                        state = 39;
                    } else if (c == '=') {
                        crChar++;
                        state = 42;
                    } else if (c == '>') {
                        crChar++;
                        state = 45;
                    } else if (c == '<') {
                        crChar++;
                        state = 48;
                    } else if (c == '&') {
                        crChar++;
                        state = 51;
                    }
                    else if (c == '\u0000') {
                        tk = new SimpleToken(END);
                        CompilerController.addToken(tk);
                        return END;
                    }
                    break;
                case 1:
                    if (isLetter(c) || isDigit(c) || c == '_')
                        crChar++;
                    else state = 2;
                    break;
                case 2: // ID final STATE
                    chLenght = crChar - chStart;
                    if (!keyWord(createString(chStart,crChar))){
                        tk = new StringToken(ID,createString(chStart,crChar));
                        CompilerController.addToken(tk);
                    }
                    return tk.getCode();
                case 3:
                    if (isDigit(c)) {
                        crChar++;
                    } else if (c == '.') {
                        crChar++;
                        state = 10;
                    } else if (c == 'E' || c == 'e') {
                        crChar++;
                        state = 12;
                    } else {
                        crChar++;
                        state = 4;
                    }
                    break;
                case 4:
                    chLenght = crChar - chStart;
                    tk = new IntegerToken(CT_INT,Integer.decode(createString(chStart,crChar)));
                    CompilerController.addToken(tk);
                    return CT_INT;
                case 5:
                    if (Character.getNumericValue(c) >= 0 && Character.getNumericValue(c) < 8) {
                        crChar++;
                        state = 6;
                    } else if (c == 'x') {
                        crChar++;
                        state = 7;
                    } else if (c - '0' == 8 || c - '0' == 9) {
                        crChar++;
                        state = 9;
                    } else if(c == '.'){
                        crChar++;
                        state = 10;
                    }
                    break;
                case 6:
                    if (Character.getNumericValue(c) >= 0 && Character.getNumericValue(c)<= 7) {
                        crChar++;
                    } else if (c == '.') {
                        crChar++;
                        state = 10;
                    } else if (c == 'E' || c == 'e') {
                        crChar++;
                        state = 12;
                    } else if (c == '8' || c == '9') {
                        crChar++;
                        state = 9;
                    } else {
                        state = 4;
                    }
                    break;
                case 7:
                    if (isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                        crChar++;
                        state = 8;
                    }
                    break;
                case 8:
                    if (isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                        crChar++;
                        state = 8;
                    } else
                        state = 4;
                    break;
                case 9:
                    if (isDigit(c)) {
                        crChar++;
                    } else if (c == '.') {
                        crChar++;
                        state = 10;
                    } else if (c == 'E' || c == 'e') {
                        crChar++;
                        state = 12;
                    }
                    break;
                case 10:
                    if (isDigit(c)) {
                        crChar++;
                        state = 11;
                    }
                    break;
                case 11:
                    if (isDigit(c)) {
                        crChar++;

                    } else if (c == 'E' || c == 'e') {
                        crChar++;
                        state = 12;
                    }else {
                        state = 14;
                    }
                    break;
                case 12:
                    if (c == '+' || c == '-') {
                        crChar++;
                        state = 13;
                    } else {
                        state = 13;
                    }
                    break;
                case 13:
                    if (isDigit(c)) {
                        crChar++;
                        state = 14;
                    }
                    break;
                case 14:
                    if (isDigit(c)) {
                        crChar++;
                    } else
                        state = 15;
                    break;
                case 15:
                    tk = new RealToken(CT_REAL,Double.parseDouble(createString(chStart,crChar)));
                    CompilerController.addToken(tk);
                    return CT_REAL;
                case 16:
                    if (!(c == '"' || c == '\\')) {
                        crChar++;
                        state = 17;
                    } else if (c == '\\') {
                        crChar++;
                        state = 19;
                    } else
                        state = 17;
                    break;
                case 17:
                    if (c == '"') {
                        crChar++;
                        state = 18;
                    } else
                        state = 16;
                    break;
                case 18:
                    tk = new StringToken(CT_STRING,createString(chStart,crChar));
                    CompilerController.addToken(tk);
                    return CT_STRING;
                case 19:
                    if (c == 'a' || c == 'b' || c == 'n' || c == 'f' || c == 'r' || c == 't' || c == 'v' || c == '\'' || c == '?' || c == '"' || c == '\0') {
                        crChar++;
                        state = 17;
                    }
                    break;
                case 20:
                    if (c == '\\') {
                        crChar++;
                        state = 21;
                    } else if (!(c == '"' && c == '\\')) {
                        crChar++;
                        state = 22;

                    }
                    break;
                case 21:
                    if (c == 'a' || c == 'b' || c == 'n' || c == 'f' || c == 'r' || c == 't' || c == 'v' || c == '\'' || c == '?' || c == '"' || c == '0' || c == '\\') {
                        crChar++;
                        state = 22;
                    }
                    break;
                case 22:
                    if (c == '\'') {
                        crChar++;
                        state = 23;
                    }
                    break;
                case 23:
                    tk = new StringToken(CT_CHAR,createString(chStart,crChar));
                    CompilerController.addToken(tk);
                    return CT_CHAR;
                case 24:
                    tk = new SimpleToken(COMMA);
                    CompilerController.addToken(tk);
                    return COMMA;
                case 25:
                    tk = new SimpleToken(SEMICOLON);
                    CompilerController.addToken(tk);
                    return SEMICOLON;
                case 26:
                    tk = new SimpleToken(LPAR);
                    CompilerController.addToken(tk);
                    return LPAR;
                case 27:
                    tk = new SimpleToken(RPAR);
                    CompilerController.addToken(tk);
                    return RPAR;
                case 28:
                    tk = new SimpleToken(RBRACKET);
                    CompilerController.addToken(tk);
                    return RBRACKET;
                case 29:
                    tk = new SimpleToken(LBRACKET);
                    CompilerController.addToken(tk);
                    return LBRACKET;
                case 30:
                    tk = new SimpleToken(LACC);
                    CompilerController.addToken(tk);
                    return LACC;
                case 31:
                    tk = new SimpleToken(RACC);
                    CompilerController.addToken(tk);
                    return RACC;
                case 32:
                    tk = new SimpleToken(ADD);
                    CompilerController.addToken(tk);
                    return ADD;
                case 33:
                    tk = new SimpleToken(SUB);
                    CompilerController.addToken(tk);
                    return SUB;
                case 34:
                    tk = new SimpleToken(MUL);
                    CompilerController.addToken(tk);
                    return MUL;
                case 35:
                    if (c == '*') {
                        crChar++;
                        state = 54;
                    } else if (c == '/') {
                        crChar++;
                        state = 57;
                    } else
                        state = 53;
                    break;
                case 36:
                    tk = new SimpleToken(DOT);
                    CompilerController.addToken(tk);
                    return DOT;
                case 37:
                    if (c == '|') {
                        crChar++;
                        state = 38;
                    }
                    break;
                case 38:
                    tk = new SimpleToken(OR);
                    CompilerController.addToken(tk);
                    return OR;
                case 39:
                    if (c == '=') {
                        crChar++;
                        state = 40;
                    } else
                        state = 41;
                case 40:
                    tk = new SimpleToken(NOTEQ);
                    CompilerController.addToken(tk);
                    return NOTEQ;
                case 41:
                    tk = new SimpleToken(NOT);
                    CompilerController.addToken(tk);
                    return NOT;
                case 42:
                    if (c == '=') {
                        crChar++;
                        state = 43;
                    } else
                        state = 45;
                    break;
                case 43:
                    tk = new SimpleToken(EQUAL);
                    CompilerController.addToken(tk);
                    return EQUAL;
                case 44:
                    tk = new SimpleToken(ASSIGN);
                    CompilerController.addToken(tk);
                    return ASSIGN;
                case 45:
                    if (c == '=') {
                        crChar++;
                        state = 47;
                    } else
                        state = 46;
                case 46:
                    tk = new SimpleToken(GREATER);
                    CompilerController.addToken(tk);
                    return GREATER;
                case 47:
                    tk = new SimpleToken(GREATEREQ);
                    CompilerController.addToken(tk);
                    return GREATEREQ;
                case 48:
                    if (c == '=') {
                        crChar++;
                        state = 50;
                    } else
                        state = 50;
                case 49:
                    tk = new SimpleToken(LESS);
                    CompilerController.addToken(tk);
                    return LESS;
                case 50:
                    tk = new SimpleToken(LESSEQ);
                    CompilerController.addToken(tk);
                    return LESSEQ;
                case 51:
                    if (c == '&') {
                        crChar++;
                        state = 52;
                    }
                    break;
                case 52:
                    tk = new SimpleToken(AND);
                    CompilerController.addToken(tk);
                    return AND;
                case 53:
                    tk = new SimpleToken(DIV);
                    CompilerController.addToken(tk);
                    return DIV;
                case 54:
                    if (c == '*') {
                        crChar++;
                        state = 55;
                    } else if(c == '\n'){
                        crChar++;
                        CompilerController.incrementLine();
                    }else
                        crChar++;
                    break;
                case 55:
                    if (c == '/') {
                        crChar++;
                        state = 56;
                    } else if (c == '*') {
                        crChar++;
                    }else if(c == '\n'){
                        crChar++;
                        CompilerController.incrementLine();
                    }
                    break;
                case 56:
                    crChar++;
                    state = 0;
                    break;
                case 57:
                    if (!(c == '\n' || c == '\r' || c == '\0')) {
                        crChar++;
                    }else
                        state = 0;
                    break;
            }
        }
    }

    private String createString(int sStart,int sCurrent){
            String toBeCreated = "";
            int i;
            char c;
            for(i=sStart;i<sCurrent;i++){
                c=input.charAt(i);
                if(c != '\\') {
                    toBeCreated += c;
                }else if(input.charAt(i+1) == 't') {
                    toBeCreated += "\t";
                    i++;
                }
            }

            return toBeCreated;

    }

    private boolean keyWord(String keyWord){
        if(keyWord.equalsIgnoreCase("break")) {
            tk = new SimpleToken(BREAK);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("char")){
            tk=new SimpleToken(CHAR);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("double")){
            tk=new SimpleToken(DOUBLE);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("else")){
            tk=new SimpleToken(ELSE);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("for")){
            tk=new SimpleToken(FOR);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("if")){
            tk=new SimpleToken(IF);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("int")) {
            tk = new SimpleToken(INT);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("return")){
            tk = new SimpleToken(RETURN);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("struct")){
            tk = new SimpleToken(STRUCT);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("void")){
            tk = new SimpleToken(VOID);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("while")){
            tk = new SimpleToken(WHILE);
            CompilerController.addToken(tk);
            return true;
        }
        return false;
    }


}
