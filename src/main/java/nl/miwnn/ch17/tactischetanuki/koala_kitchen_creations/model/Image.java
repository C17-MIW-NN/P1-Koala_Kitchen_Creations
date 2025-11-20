package nl.miwnn.ch17.tactischetanuki.koala_kitchen_creations.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.http.MediaType;

/**
 * @author Jantine van der Schaaf
 * @author Josse Muller
 * Doel methode
 */
@Getter
@Setter
@Entity
public class Image {
    @Id @GeneratedValue
    private Long imageId;

    @Column(unique = true)
    private String fileName;
    private String contentType;

    @Getter
    @Setter
    @Lob
    private byte[] data;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + imageId +
                ", fileName='" + fileName + '\'' +
                ", contentType=" + contentType +
                '}';
    }

    public MediaType getContentType() {
        return MediaType.parseMediaType(contentType);
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType.toString();
    }
}

