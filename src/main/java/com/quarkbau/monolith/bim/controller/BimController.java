package com.quarkbau.monolith.bim.controller;

import com.quarkbau.monolith.bim.service.BimService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bim")
@RequiredArgsConstructor
public class BimController {

    private final BimService bimService;

    @PostMapping("/upload")
    public List<Map<String, Object>> uploadIfc(@RequestParam("file") MultipartFile file) {
        return bimService.extractMetadata(file);
    }
}
