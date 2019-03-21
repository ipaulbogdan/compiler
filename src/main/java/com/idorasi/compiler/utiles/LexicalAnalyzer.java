package com.idorasi.compiler.utiles;

import com.idorasi.compiler.CompilerController;
import com.idorasi.compiler.modules.Atoms;

import static com.idorasi.compiler.modules.Atoms.*;

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
// PANA LA && totul e in regula
    public Atoms getNextToken() {
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
                        tk = new Token(END);
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
                        tk = new Token(ID);
                        tk.setText(createString(chStart,crChar));
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
                    tk = new Token(CT_INT);
                    tk.setInteger(Integer.decode(createString(chStart,crChar)));
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
                    tk = new Token(CT_REAL);
                    tk.setDouble(Double.parseDouble(createString(chStart,crChar)));
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
                    tk = new Token(CT_STRING);
                    tk.setText(createString(chStart,crChar));
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
                    tk = new Token(CT_CHAR);
                    tk.setText(createString(chStart,crChar));
                    CompilerController.addToken(tk);
                    return CT_CHAR;
                case 24:
                    tk = new Token(COMMA);
                    CompilerController.addToken(tk);
                    return COMMA;
                case 25:
                    tk = new Token(SEMICOLON);
                    CompilerController.addToken(tk);
                    return SEMICOLON;
                case 26:
                    tk = new Token(LPAR);
                    CompilerController.addToken(tk);
                    return LPAR;
                case 27:
                    tk = new Token(RPAR);
                    CompilerController.addToken(tk);
                    return RPAR;
                case 28:
                    tk = new Token(RBRACKET);
                    CompilerController.addToken(tk);
                    return RBRACKET;
                case 29:
                    tk = new Token(LBRACKET);
                    CompilerController.addToken(tk);
                    return LBRACKET;
                case 30:
                    tk = new Token(LACC);
                    CompilerController.addToken(tk);
                    return LACC;
                case 31:
                    tk = new Token(RACC);
                    CompilerController.addToken(tk);
                    return RACC;
                case 32:
                    tk = new Token(ADD);
                    CompilerController.addToken(tk);
                    return ADD;
                case 33:
                    tk = new Token(SUB);
                    CompilerController.addToken(tk);
                    return SUB;
                case 34:
                    tk = new Token(MUL);
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
                    tk = new Token(DOT);
                    CompilerController.addToken(tk);
                    return DOT;
                case 37:
                    if (c == '|') {
                        crChar++;
                        state = 38;
                    }
                    break;
                case 38:
                    tk = new Token(OR);
                    CompilerController.addToken(tk);
                    return OR;
                case 39:
                    if (c == '=') {
                        crChar++;
                        state = 40;
                    } else
                        state = 41;
                case 40:
                    tk = new Token(NOTEQ);
                    CompilerController.addToken(tk);
                    return NOTEQ;
                case 41:
                    tk = new Token(NOT);
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
                    tk = new Token(EQUAL);
                    CompilerController.addToken(tk);
                    return EQUAL;
                case 44:
                    tk = new Token(ASSIGN);
                    CompilerController.addToken(tk);
                    return ASSIGN;
                case 45:
                    if (c == '=') {
                        crChar++;
                        state = 47;
                    } else
                        state = 46;
                case 46:
                    tk = new Token(GREATER);
                    CompilerController.addToken(tk);
                    return GREATER;
                case 47:
                    tk = new Token(GREATEREQ);
                    CompilerController.addToken(tk);
                    return GREATEREQ;
                case 48:
                    if (c == '=') {
                        crChar++;
                        state = 50;
                    } else
                        state = 50;
                case 49:
                    tk = new Token(LESS);
                    CompilerController.addToken(tk);
                    return LESS;
                case 50:
                    tk = new Token(LESSEQ);
                    CompilerController.addToken(tk);
                    return LESSEQ;
                case 51:
                    if (c == '&') {
                        crChar++;
                        state = 52;
                    }
                    break;
                case 52:
                    tk = new Token(AND);
                    CompilerController.addToken(tk);
                    return AND;
                case 53:
                    tk = new Token(DIV);
                    CompilerController.addToken(tk);
                    return DIV;
                case 54:
                    if (c == '*') {
                        crChar++;
                        state = 55;
                    } else
                        crChar++;
                    break;
                case 55:
                    if (c == '/') {
                        crChar++;
                        state = 56;
                    } else if (c == '*')
                        crChar++;
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
            for(i=sStart;i<sCurrent;i++){
                toBeCreated+=input.charAt(i);
            }

            return toBeCreated;

    }

    private boolean keyWord(String keyWord){
        if(keyWord.equalsIgnoreCase("break")) {
            tk = new Token(BREAK);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("char")){
            tk=new Token(CHAR);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("double")){
            tk=new Token(DOUBLE);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("else")){
            tk=new Token(ELSE);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("for")){
            tk=new Token(FOR);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("if")){
            tk=new Token(IF);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("int")) {
            tk = new Token(INT);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("return")){
            tk = new Token(RETURN);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("struct")){
            tk = new Token(STRUCT);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("void")){
            tk = new Token(VOID);
            CompilerController.addToken(tk);
            return true;
        }
        if(keyWord.equalsIgnoreCase("while")){
            tk = new Token(WHILE);
            CompilerController.addToken(tk);
            return true;
        }
        return false;
    }


}
