package com.idorasi.compiler;

import com.idorasi.compiler.modules.Atoms;
import com.idorasi.compiler.utiles.LexicalAnalyzer;
import com.idorasi.compiler.utiles.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.idorasi.compiler.modules.Atoms.END;


@RestController
@RequestMapping("/")
public class CompilerController {

    private static List<Token> tokens = new ArrayList<>();
    private static int line=0;



    @PostMapping("/compile")
    public void setInput(@RequestBody String input){
        compile(input);
    }

    private void compile(String input){
        LexicalAnalyzer lexialAnalyzer = new LexicalAnalyzer(input);
        lexialAnalyzer.resetCompiler();
        line=0;
        while(lexialAnalyzer.getNextToken()!=END){

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
