package com.example.playgroundmanage.store;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.playgroundmanage.store.FileRootParser.getExtFilePath;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgFileName;

    private String storeFileName;


    @Builder
    public UploadFile(Long id, String orgFileName, String storeFileName) {
        this.id = id;
        this.orgFileName = orgFileName;
        this.storeFileName = storeFileName;
    }

    public String getFileUrl() {
        return getExtFilePath() + storeFileName;
    }
}
