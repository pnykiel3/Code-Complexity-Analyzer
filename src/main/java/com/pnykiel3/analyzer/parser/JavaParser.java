package com.pnykiel3.analyzer.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.pnykiel3.analyzer.dto.FileMetrics;
import org.springframework.stereotype.Component;

@Component
public class JavaParser implements FunctionParser{
    @Override
    public boolean supports(String extension) {
        return extension.equals("java");
    }

    @Override
    public FileMetrics analyzeMetrics(String code) {

        try {
            int linesCount = countLines(code);

            CompilationUnit cu = StaticJavaParser.parse(code);
            int functionCount = cu.findAll(MethodDeclaration.class).size();

            int complexity = 1;
            complexity += cu.findAll(IfStmt.class).size();
            complexity += cu.findAll(ForStmt.class).size();
            complexity += cu.findAll(ForEachStmt.class).size();
            complexity += cu.findAll(WhileStmt.class).size();
            complexity += cu.findAll(DoStmt.class).size();
            complexity += cu.findAll(CatchClause.class).size();

            // default cases are not counted
            complexity += (int) cu.findAll(SwitchEntry.class).stream()
                    .filter(entry -> entry.getLabels().isNonEmpty())
                    .count();

            return new FileMetrics(linesCount, functionCount, complexity);
        } catch (Exception e) {
            System.err.println("Java file parser error: " + e.getMessage());
            return new FileMetrics(0,0,1);
        }

    }
}
