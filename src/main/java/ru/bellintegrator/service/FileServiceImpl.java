package ru.bellintegrator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.model.UploadFile;
import ru.bellintegrator.repos.FileRepo;


import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Service
public class FileServiceImpl implements FileService {
    @Value("${upload.path}")
    private String uploadPath;

    private final FileRepo fileRepo;

    @Autowired
    public FileServiceImpl(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UploadFile> findAllFile() {
        return toList(fileRepo.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFile(MultipartFile file) {
        if (file == null || (file.getOriginalFilename() == null) || (file.getOriginalFilename().isEmpty())) {
            throw new RuntimeException("Error: File cant be empty!");
        }
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileName = file.getOriginalFilename();
        String resultFilename = createUniqueFileName(fileName);

        try {
            file.transferTo(new File(uploadPath + "/" + resultFilename));
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("File or path was not found!", e);
        }

        UploadFile fileToRepo = new UploadFile();
        fileToRepo.setFileName(resultFilename);
        fileToRepo.setOriginalName(fileName);
        fileToRepo.setDownloadCount(0);
        fileRepo.save(fileToRepo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity downloadFile(UploadFile uploadFile) {
        if (uploadFile == null) {
            return null;
        }

        File file = new File(uploadPath + "/" + uploadFile.getFileName());

        uploadFile.setDownloadCount(uploadFile.getDownloadCount() + 1);
        fileRepo.save(uploadFile);

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("The file or path was not found!", e);
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(String fileId) {
        if (fileId == null || !fileId.matches("[-+]?\\d+")) {
            throw new RuntimeException("The file id is incorrect!");
        }
        Integer id = Integer.valueOf(fileId);
        try {
            UploadFile uploadFile = fileRepo.getOne(id);
            File fileFromDisk = new File(uploadPath + "/" + uploadFile.getFileName());
            if (!fileFromDisk.delete()) {
                throw new RuntimeException("The file was not deleted!");
            }
            fileRepo.delete(uploadFile);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("There is no file with id " + id, e);
        }
    }

    /**
     * Метод преобразования типа @interface Iterable
     * в тип @interface List
     *
     * @param iterable
     * @return коллекция типа List
     */
    private <E> List<E> toList(Iterable<E> iterable) {
        if (iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E>();
        if (iterable != null) {
            for (E e : iterable) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Сформировать уникальное имя файла
     *
     * @param originalFileName оригинальное имя файла
     * @return уникальное имя файла
     */
    private String createUniqueFileName(String originalFileName) {
        String uuidFile = UUID.randomUUID().toString();
        return uuidFile + "." + originalFileName;
    }
}
