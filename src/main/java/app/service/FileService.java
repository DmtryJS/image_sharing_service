package app.service;

import app.controllers.FileController;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
public class FileService {

    private Path sharedFolder;

    @Value("${files.folder}")
    private String fileFolder;

    @PostConstruct
    public void init() {
        //найдем путь где будут хранится файлы
        String pathInString = FileController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try {
            String decodedPath = URLDecoder.decode(pathInString, "UTF-8");
            File file = new File(decodedPath);
            String rootFolder = file.getParentFile()
                    .getParentFile()
                    .getParent()
                    .replaceFirst("^(file:\\\\?)", "");

            sharedFolder = Paths.get(rootFolder, fileFolder);
            if (!Files.exists(sharedFolder)) {
                Files.createDirectory(sharedFolder);
            }
        } catch (IOException e) {
            log.error("Ошибка создания необходимой струкруры папкок", e);
            throw new RuntimeException(e);
        }
    }

    private Path buildFilePath(String fileName) {
        return Paths.get(sharedFolder.toString(),
                fileName);
    }

    public boolean fileIsExist(String fileName) {
        Path path = buildFilePath(fileName);
        return Files.exists(path);
    }

    public byte[] byteArrayFromFile(String fileName) throws IOException {
        InputStream stream = new FileInputStream(buildFilePath(fileName).toString());
        return IOUtils.toByteArray(stream);
    }

    public void saveFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(sharedFolder.toString(), file.getOriginalFilename());
        Files.write(path, bytes);
    }

    public boolean checkMediaTypeIsAccepted(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            MediaType mediaType = MediaType.parseMediaType(contentType);
            return mediaType.equals(MediaType.IMAGE_JPEG) || mediaType.equals(MediaType.IMAGE_PNG);
        }
        return false;
    }
}
