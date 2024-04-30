package com.example.playgroundmanage.refactoring.repo;

import com.example.playgroundmanage.refactoring.Athletics;
import org.eclipse.jdt.internal.compiler.parser.JavadocParser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AthleticsRepository extends JpaRepository<Athletics, Long> {
}
