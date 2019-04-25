package ru.bellintegrator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

/**
 * Загруженный файл
 */
@Entity
public class UploadFile {
    /**
     * Уникальный идентификатор файла
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Служебное поле Hibernate
     */
    @Version
    private Integer version;

    /**
     * Название файла
     */
    private String fileName;

    /**
     * Оригинальное название файла
     */
    private String originalName;

    /**
     * Количество скачиваний
     */
    private Integer downloadCount;

    public UploadFile() {
    }

    public UploadFile(Integer id, String fileName, String originalName, Integer downloadCount) {
        this.id = id;
        this.fileName = fileName;
        this.originalName = originalName;
        this.downloadCount = downloadCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}

        UploadFile uploadFile = (UploadFile) obj;
        return Objects.equals(id, uploadFile.id) &&
                Objects.equals(fileName, uploadFile.fileName) &&
                Objects.equals(originalName, uploadFile.originalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, originalName);
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", originalName='" + originalName + '\'' +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
