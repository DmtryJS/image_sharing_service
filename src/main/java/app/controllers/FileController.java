package app.controllers;

import app.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Log4j2
@RestController
public class FileController {


    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/image/{file_name}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@PathVariable(value = "file_name") String fileName)
            throws IOException {
        HttpHeaders headers = new HttpHeaders();
        if (!fileService.fileIsExist(fileName)) {
            log.warn("File with name " + fileName + " does not found");
            throw new FileNotFoundException(fileName);
        }
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(fileService.byteArrayFromFile(fileName), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) throws IOException,
            HttpMediaTypeNotSupportedException {
        if (file.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!fileService.checkMediaTypeIsAccepted(file)) {
            String message = "MediaType not supported";
            log.warn(message);
            throw new HttpMediaTypeNotSupportedException(message);
        }
        fileService.saveFile(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
