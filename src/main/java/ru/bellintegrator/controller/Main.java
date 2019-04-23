package ru.bellintegrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.domain.File;
import ru.bellintegrator.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class Main {
    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/home")
    public String downloading(Map<String, Object> model) {
        List<File> files = fileService.findAllFile();

        model.put("files", files);

        return "home";
    }

    @PostMapping("/home")
    public String addFile(
            @RequestParam("file") MultipartFile file,
            Map<String, Object> model
    ) throws IOException {
        boolean isAdded = fileService.addFile(file);

        if (isAdded){
            model.put("message", "File was added");
        }else{
            model.put("message", "Error: file wasn't added!");
        }

        List<File> files = fileService.findAllFile();
        model.put("files", files);

        return "home";
    }
}
