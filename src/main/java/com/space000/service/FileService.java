package com.space000.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${location}")
    private String location;

    /**
     * 파일 업로드
     * @param uploadPath - 파일 실제 업로드 경로
     * @param originalFileName - 업로드할 파일의 이름
     * @param fileData - 업로드할 파일
     * @return uuid.확장자명
     * @throws IOException
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid + ext;
        String filePath = uploadPath + "/" + savedFileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileData);
        } catch (IOException e) {
            log.error("파일 업로드 중 에러 발생 : {}", e.getMessage());
            throw e;
        }

        return savedFileName;
    }

    /**
     * 외부 경로로 변경
     * @param url - /files/**
     * @return 실제 저장된 외부경로로 반환
     */
    public String replaceUrl(String url) {
        String path = url.replaceFirst("/files", location);
        return path;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않아 삭제할 수 없습니다.");
        }
    }

}
