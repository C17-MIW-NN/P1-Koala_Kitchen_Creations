package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.service;

import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model.Image;
import nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.repositories.ImageRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 */

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void setUniqueFilename(Image image, String originalFilename) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        image.setFileName(uniqueFileName);
    }

    public Image saveImage(MultipartFile file) throws IOException {
        MediaType contentType = MediaType.IMAGE_JPEG;
        if (file.getContentType() != null) {
            contentType = MediaType.parseMediaType(file.getContentType());
        }

        Image image = new Image();
        setUniqueFilename(image, file.getOriginalFilename());
        image.setContentType(contentType);
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }

    public Image saveImage(Resource imageResource) throws IOException {
        Image image = new Image();
        setUniqueFilename(image, imageResource.getFilename());
        MediaType contentType = MediaTypeFactory.getMediaType(imageResource).orElse(MediaType.IMAGE_JPEG);
        image.setContentType(contentType);
        image.setData(imageResource.getInputStream().readAllBytes());
        return imageRepository.save(image);
    }

    public Image getImage(String fileName) {
        return imageRepository.findByFileName(fileName)
                .orElseThrow(() -> new NoSuchElementException(fileName));
    }
}

