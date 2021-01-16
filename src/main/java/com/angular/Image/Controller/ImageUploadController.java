package com.angular.Image.Controller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.angular.Image.Models.ImageModel;
import com.angular.Image.Repository.ImageRepository;
import com.angular.Image.Service.BaseResponse;
import com.angular.Image.Service.ResponseHandler;
import com.angular.Image.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageUploadController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    UploadService uploadService;

    @PostMapping("/upload")
    public BaseResponse uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
                uploadService.compressBytes(file.getBytes()));
        ImageModel imageSaveReponse = imageRepository.saveAndFlush(img);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setDescription("Image Uploaded Successfully.");
        baseResponse.setData(imageSaveReponse);
        baseResponse.setStatusCode(200);

        return baseResponse;
    }

    @GetMapping(path = { "/get/{imageName}" })
    public ImageModel getImage(@PathVariable("imageName") String imageName) throws IOException {

        final ImageModel retrievedImage = imageRepository.findByName(imageName);
        ImageModel img = new ImageModel(retrievedImage.getName(), retrievedImage.getType(),
                uploadService.decompressBytes(retrievedImage.getPicByte()));
        return img;
    }


}
