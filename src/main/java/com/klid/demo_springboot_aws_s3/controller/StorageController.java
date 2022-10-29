package com.klid.demo_springboot_aws_s3.controller;

import com.klid.demo_springboot_aws_s3.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Ivan Kaptue
 */
@RestController
@RequestMapping("/")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile multipartFile) {
        var key = storageService.saveFile(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(key);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Map<String, String>> readFile(@PathVariable("key") String key) {
        var fileContent = storageService.getFileContent(key);
        return ResponseEntity.ok(Map.of(key, fileContent));
    }
}
