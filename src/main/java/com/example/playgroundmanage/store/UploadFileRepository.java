package com.example.playgroundmanage.store;

import com.example.playgroundmanage.store.vo.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
