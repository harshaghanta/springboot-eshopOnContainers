package com.eshoponcontainers.catalogapi.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PicController {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @GetMapping("/api/v1/catalog/item/{id}/pic")
    public ResponseEntity<?> getItemPic(@PathVariable("id") Integer id) throws IOException {

        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if (item.isPresent()) {
            String pictureFileName = item.get().getPictureFileName();
            InputStream picStream = getClass()
                    .getResourceAsStream("/com/eshoponcontainers/catalogapi/controllers/pic/" + pictureFileName);
            ResponseEntity<byte[]> responseEntity;
            final HttpHeaders headers = new HttpHeaders();

            byte[] media = IOUtils.toByteArray(picStream);
            headers.setContentType(MediaType.IMAGE_JPEG);
            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;

        }
        return ResponseEntity.notFound().build();
    }
}
