package ru.bellintegrator.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.bellintegrator.model.UploadFile;
import ru.bellintegrator.service.FileService;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест для контроллера файлов
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService service;


    @Test
    public void downloadingTest() throws Exception {
        List<UploadFile> files = new ArrayList<>();
        files.add(new UploadFile(25, "8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.Демонстрация 2.wmv",
                "Демонстрация 2.wmv", 0));
        given(this.service.findAllFile()).willReturn(files);

        this.mockMvc.perform(get("/files")).andExpect(status().isOk())
                .andExpect(model().attribute("files", files));
    }

    @Test
    public void addFileTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "Демонстрация 2.wmv", "video/mpeg", "Демонстрация 2".getBytes());
        this.mockMvc.perform(fileUpload("/files").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/files"));

        then(this.service).should().addFile(multipartFile);
    }

    @Test
    public void downloadFileTest() throws Exception {
        File file = new File("src/test/resources/8f4fbc25-1cbf-4002-8451-5e4f04a3a0b5.text.txt");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);


        UploadFile uploadFile = new UploadFile(5, "47fb4801-10e9-49a7-a3c4-ffb34db0f1cc.test.txt",
                "test.txt", 0);
        given(this.service.downloadFile(uploadFile))
                .willReturn(responseEntity);

        this.mockMvc.perform(get("/files/5")).andExpect(status().isOk());
    }

    @Test
    public void deleteFileTest() throws Exception {
        doNothing().when(service).deleteFile(isA(String.class));
        service.deleteFile("5");
        this.mockMvc.perform(delete("/files/5")).andExpect(status().isFound());
    }

}
