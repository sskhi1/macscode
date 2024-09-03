package com.freeuni.macs.controller;

import com.freeuni.macs.model.DraftProblem;
import com.freeuni.macs.model.UploadProblemRequest;
import com.freeuni.macs.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        return ResponseEntity.ok(null);
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeProblem(@RequestParam("id") String id,
            @RequestBody UploadProblemRequest uploadProblemRequest) {
        authorService.changeProblem(id, uploadProblemRequest);
        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/drafts/{id}")
    public ResponseEntity<Void> deleteDraft(@PathVariable String id) {
        authorService.deleteDraft(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/drafts")
    public List<DraftProblem> uploadProblem() {
        return authorService.getAllDraftProblems();
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishProblem(
            @RequestParam(value = "id", required = false) String id,
            @RequestBody UploadProblemRequest uploadProblemRequest) {

        authorService.publishProblem(uploadProblemRequest, id);
        return ResponseEntity.ok("Problem processed successfully");
    }
}
