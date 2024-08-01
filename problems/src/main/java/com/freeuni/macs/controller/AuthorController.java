package com.freeuni.macs.controller;

import com.freeuni.macs.model.UploadProblemRequest;
import com.freeuni.macs.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProblem(@RequestBody UploadProblemRequest uploadProblemRequest) {
        authorService.processUpload(uploadProblemRequest);
        return null;
    }

}
