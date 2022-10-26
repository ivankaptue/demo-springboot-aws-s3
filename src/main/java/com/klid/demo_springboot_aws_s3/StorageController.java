package com.klid.demo_springboot_aws_s3;

import com.klid.demo_springboot_aws_s3.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> saveFile(@RequestBody String content) {
        var key = storageService.saveFile(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(key);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Map<String, String>> readFile(@PathVariable("key") String key) {
        var fileContent = storageService.getFileContent(key);
        return ResponseEntity.ok(Map.of(key, fileContent));
    }
}
