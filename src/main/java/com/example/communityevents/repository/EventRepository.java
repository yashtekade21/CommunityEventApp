package com.example.communityevents.repository;

import com.example.communityevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByLocationContainingIgnoreCase(String location);

    List<Event> findByCategoryContainingIgnoreCase(String category);

    List<Event> findByDate(LocalDate date);

    List<Event> findByLocationContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndDate(
            String location, String category, LocalDate date);

    List<Event> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    List<Event> findByDeletedFalse();
}
