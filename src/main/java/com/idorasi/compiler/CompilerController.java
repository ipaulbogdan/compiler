package com.idorasi.compiler;

import com.idorasi.compiler.utiles.LexicalAnalyzer;
import com.idorasi.compiler.utiles.Tokens.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

import static com.idorasi.compiler.modules.Atoms.END;


@RestController
@RequestMapping("/")
public class CompilerController {

    private static List<Token> tokens = new ArrayList<>();
    private static int line=1;



    @PostMapping("/compile")
    public void setInput(@RequestBody String input){
        compile(input);
    }

    private void compile(String input){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(input);
        lexicalAnalyzer.resetCompiler();
        line=1;
        while(lexicalAnalyzer.getNextToken()!=END){

        }
        printTokens();

    }

    private void printTokens() {
        for(Token tk : tokens){
            System.out.println(tk);

        }
}


    public static void addToken(Token token){
        tokens.add(token);
    }

    public static void incrementLine(){
        line++;
    }

    public static int getLine(){return line;}

}
