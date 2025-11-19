package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Image;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author Jantine van der Schaaf
 * Doel methode
 */

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String saveImage(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

//        if (imageRepository.existsByFileName(file.getOriginalFilename())) {
//            throw new IllegalIdentifierException(file.getOriginalFilename() + " already exists");
//        }

        MediaType contentType = MediaType.IMAGE_JPEG;
        if (file.getContentType() != null) {
            contentType = MediaType.parseMediaType(file.getContentType());
        }

        Image image = new Image();
        image.setFileName(uniqueFileName);
        image.setContentType(contentType);
        image.setData(file.getBytes());

        imageRepository.save(image);

        return uniqueFileName;
    }


    public Image getImage(String fileName) {
        return imageRepository.findByFileName(fileName)
                .orElseThrow(() -> new NoSuchElementException(fileName));
    }

    public void saveImage(ClassPathResource imageResource) throws IOException {
        Image image = new Image();
        image.setFileName(imageResource.getFilename());
        image.setContentType(MediaType.IMAGE_JPEG);
        image.setData(imageResource.getInputStream().readAllBytes());
        imageRepository.save(image);
    }
}

