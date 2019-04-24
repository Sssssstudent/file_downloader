package ru.bellintegrator.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.model.UploadFile;

import java.io.IOException;
import java.util.List;

/**
 * Сервис файлов
 */
public interface FileService {

    /**
     * Найти все загруженные файлы
     *
     * @return список файлов
     */
    List<UploadFile> findAllFile();

    /**
     * Загрузить файл
     *
     * @param uploadFile файл
     */
    boolean addFile(MultipartFile uploadFile) throws IOException;

    /**
     * Скачать файл
     *
     * @param uploadFile файл
     * @return ResponseEntity сформированный ответ для контроллера
     */
    ResponseEntity downloadFile(UploadFile uploadFile);

    /**
     * Удалить файл
     *
     * @param id
     */
    void deleteFile(String id);
}
