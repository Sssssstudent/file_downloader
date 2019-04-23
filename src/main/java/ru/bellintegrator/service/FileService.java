package ru.bellintegrator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bellintegrator.domain.File;
import ru.bellintegrator.repos.FileRepo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    private FileRepo fileRepo;

    private <E> List<E> toList(Iterable<E> iterable) {
        if(iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E>();
        if(iterable != null) {
            for(E e: iterable) {
                list.add(e);
            }
        }
        return list;
    }

    public List<File> findAllFile(){
        return toList(fileRepo.findAll());
    }

    public boolean addFile(MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()){
            java.io.File uploadDir = new java.io.File(uploadPath);

            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String fileName = file.getOriginalFilename();
            file.transferTo(new java.io.File(uploadPath + "/" + fileName));

            File fileToRepo = new File(fileName);

            fileRepo.save(fileToRepo);
            return true;
        }

        return false;
    }


}
