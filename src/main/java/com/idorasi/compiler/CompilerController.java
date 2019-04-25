package com.idorasi.compiler;

import com.idorasi.compiler.modules.LexicalAnalyzer;
import com.idorasi.compiler.modules.SyntacticAnalyzer;
import com.idorasi.compiler.utiles.Tokens.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

import static com.idorasi.compiler.utiles.Atom.END;


@RestController
@RequestMapping("/")
public class CompilerController {


    private LexicalAnalyzer lexicalAnalyzer;
    private SyntacticAnalyzer syntacticAnalyzer;
    private static List<Token> tokens = new ArrayList<>();
    private static int line=1;



    @PostMapping("/compile")
    public void setInput(@RequestBody String input){
        compile(input);
    }

    private void compile(String input){
        lexicalAnalyzer = new LexicalAnalyzer(input);
        lexicalAnalyzer.resetCompiler();

        line=1;
        while(lexicalAnalyzer.getNextToken()!=END){ }

        syntacticAnalyzer = new SyntacticAnalyzer(tokens);
        syntacticAnalyzer.start();


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
