package com.pnykiel3.analyzer.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Component;

@Component
public class JavaParser implements FunctionParser{
    @Override
    public boolean supports(String extension) {
        return extension.equals("java");
    }

    @Override
    public int countFunctions(String code) {

        try {
            CompilationUnit cu = StaticJavaParser.parse(code);
            return cu.findAll(MethodDeclaration.class).size();
        } catch (Exception e) {
            System.err.println("Java file parser error: " + e.getMessage());
            return 0;
        }

    }
}
