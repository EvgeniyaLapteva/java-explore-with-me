package ru.practicum.ewm.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findByPinned(Boolean pinned, PageRequest page);
}
