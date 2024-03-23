package freeuni.macs.macscode.controller;

import freeuni.macs.macscode.service.JavaCodeRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class RunnerController {

    private final JavaCodeRunner javaCodeRunner;

    @GetMapping
    public void nothingYet() {
        javaCodeRunner.run();
    }

}
