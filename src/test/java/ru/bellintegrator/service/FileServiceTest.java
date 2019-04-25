package ru.bellintegrator.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bellintegrator.model.UploadFile;
import ru.bellintegrator.repos.FileRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тест FileService
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileServiceTest {
    @Mock
    private FileService service;

    @MockBean
    private FileRepo fileRepo;

    @Test
    public void findAllFileTest() {
        List<UploadFile> files = new ArrayList<>();
        files.add(new UploadFile(26, "src/test/resources/8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt", "text.txt", 0));

        when(service.findAllFile()).thenReturn(files);
        Assert.assertEquals(files, service.findAllFile());
    }

    @Test
    public void addFileTest() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt", "text/plain",
                "text".getBytes());
        service.addFile(multipartFile);
        UploadFile uploadFile = new UploadFile(26, "8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt", "text.txt", 0);
        Mockito.doReturn(uploadFile)
                .when(fileRepo)
                .getOne(26);

        Assert.assertEquals(uploadFile, fileRepo.getOne(26));
        verify(service, times(1)).addFile(multipartFile);
    }

    @Test
    public void downloadFileTest() throws Exception{
        UploadFile userFile = new UploadFile(26, "8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt", "text.txt", 0);
        fileRepo.save(userFile);
        File file = new File("src/test/resources/8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
        when(service.downloadFile(userFile)).thenReturn(responseEntity);
        Assert.assertEquals(responseEntity, service.downloadFile(userFile));

    }

    @Test
    public void deleteFileTest() {
        UploadFile userFile = new UploadFile(26, "8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt", "text.txt", 0);
        fileRepo.save(userFile);

        service.deleteFile("26");
        verify(service, times(1)).deleteFile("26");
        Assert.assertEquals(false, fileRepo.findById(26).isPresent());
    }

}
