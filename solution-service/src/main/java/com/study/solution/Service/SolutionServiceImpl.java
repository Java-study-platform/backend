package com.study.solution.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

@Service
public class SolutionServiceImpl implements SolutionService {
    private final JavaCompiler compiler;

    public SolutionServiceImpl() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    public ResponseEntity<?> runSolution(){
        return ResponseEntity.ok().build();
    }
}
