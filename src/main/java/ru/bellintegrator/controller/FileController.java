package ru.bellintegrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.model.UploadFile;
import ru.bellintegrator.service.FileServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Контроллер обработчика запросов по работе с файлами
 */
@Controller
@RequestMapping("/files")
public class FileController {
    private final FileServiceImpl fileService;

    @Autowired
    public FileController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }

    /**
     * Отобразить все загруженные файлы
     *
     * @param model модель
     * @return страница со списком загруженных файлов
     */
    @GetMapping
    public String downloading(Map<String, Object> model) {
        List<UploadFile> files = fileService.findAllFile();

        model.put("files", files);

        return "home";
    }

    /**
     * Загрузить файл на сервис
     *
     * @param file загружаемый файл
     * @return страница со списком загруженных файлов
     */
    @PostMapping
    public String addFile(@RequestParam("file") MultipartFile file) {
        fileService.addFile(file);
        return "redirect:/files";
    }

    /**
     * Скачать файл из списка загруженных файлов
     *
     * @param uploadFile
     * @return ResponseEntity Ответ от контроллера
     */
    @GetMapping("/{uploadFile:.+}")
    public ResponseEntity downloadFile(@PathVariable(value = "uploadFile") UploadFile uploadFile) {
        return fileService.downloadFile(uploadFile);
    }

    /**
     * Удалить файл из списка загруженных файлов
     *
     * @param id уникальный идентификатор файла
     * @return страница со списком фалов
     */
    @DeleteMapping("/{id")
    public String deleteFile(@PathVariable(value = "id") String id) {
        fileService.deleteFile(id);
        return "redirect:/files";
    }


}
