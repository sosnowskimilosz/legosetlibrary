package milo.legosetlibrary.LegoSetLibrary.uploads.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload {

    @Id
    @GeneratedValue
    Long id;
    byte[] file;
    String contentType;
    String filename;

    @CreatedDate
    LocalDateTime createdAt;

    public Upload(String filename, String contentType, byte[] file){
        this.filename = filename;
        this.contentType = contentType;
        this.file = file;
    }
}
