package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        // File names of current file / orignal files
        String orignalFileName=image.getOriginalFilename();

        //Generate a unique file name
        String randomId= UUID.randomUUID().toString();
        //mat.jpg-->1234
        //1234.jpg
        String fileName=randomId.concat(orignalFileName.substring(orignalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator + fileName;
        //Check if path exist  and create
        File folder=new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        //upload on the server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        //returning the file name
        return  fileName;
    }
}
