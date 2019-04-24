package ru.bellintegrator.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bellintegrator.model.UploadFile;

public interface FileRepo extends JpaRepository<UploadFile, Integer> {

}
