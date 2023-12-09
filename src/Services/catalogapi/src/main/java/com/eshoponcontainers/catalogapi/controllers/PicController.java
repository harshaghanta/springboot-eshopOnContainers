package com.eshoponcontainers.catalogapi.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PicController {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @GetMapping("/api/v1/catalog/items/{id}/pic")
    public ResponseEntity<?> getItemPic(@PathVariable("id") Integer id) throws IOException {

        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if (item.isPresent()) {
            String pictureFileName = item.get().getPictureFileName();
            log.info("pictureFileName for id: {} is : {}", id,  pictureFileName);
            String resourcePath = "static/" + pictureFileName;
            log.info("resource Path is : {} ", resourcePath);
            InputStream picStream = getClass().getClassLoader()
                    .getResourceAsStream(resourcePath);
            ResponseEntity<byte[]> responseEntity;
            final HttpHeaders headers = new HttpHeaders();

            byte[] media = IOUtils.toByteArray(picStream);
            String extension = FilenameUtils.getExtension(pictureFileName);
            MediaType mediaType = getMediaTypeByExtension(extension);
            headers.setContentType(mediaType);
            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;

        }
        return ResponseEntity.notFound().build();
    }

    private MediaType getMediaTypeByExtension(String extension) {

        MediaType mediaType = null;
        switch (extension) {
            case "jpg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
        }
        return mediaType;
    }


}
