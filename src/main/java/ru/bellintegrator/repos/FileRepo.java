package ru.bellintegrator.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bellintegrator.domain.File;

public interface FileRepo extends CrudRepository<File, Long> {

}
