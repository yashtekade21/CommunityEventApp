package com.example.communityevents.service;

import com.example.communityevents.model.Event;
import com.example.communityevents.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public void autoDeleteTodayEvents() {
        LocalDate today = LocalDate.now();
        List<Event> todayEvents = eventRepository.findByDate(today);
        for (Event event : todayEvents) {
            if (!event.isDeleted()) {
                event.setDeleted(true);
                eventRepository.save(event);
            }
        }
    }

    public List<Event> getEventsFiltered(String location, String category, LocalDate date) {
        autoDeleteTodayEvents();
        if (location != null && category != null && date != null) {
            return eventRepository
                    .findByLocationContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndDate(location, category, date)
                    .stream().filter(e -> !e.isDeleted()).toList();
        } else if (location != null) {
            return eventRepository.findByLocationContainingIgnoreCase(location)
                    .stream().filter(e -> !e.isDeleted()).toList();
        } else if (category != null) {
            return eventRepository.findByCategoryContainingIgnoreCase(category)
                    .stream().filter(e -> !e.isDeleted()).toList();
        } else if (date != null) {
            return eventRepository.findByDate(date)
                    .stream().filter(e -> !e.isDeleted()).toList();
        } else {
            return eventRepository.findAll()
                    .stream().filter(e -> !e.isDeleted()).toList();
        }
    }

    public List<Event> searchEvents(String query) {
        autoDeleteTodayEvents();
        return eventRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream().filter(e -> !e.isDeleted()).toList();
    }

    public Optional<Event> getEvent(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        event.setCurrentAttendees(0);
        return eventRepository.save(event);
    }

    public boolean incrementAttendees(Long id) {
        Optional<Event> optional = eventRepository.findById(id);
        if (optional.isPresent()) {
            Event event = optional.get();
            if (event.getCurrentAttendees() < event.getMaxAttendees()) {
                event.setCurrentAttendees(event.getCurrentAttendees() + 1);
                eventRepository.save(event);
                return true;
            }
        }
        return false;
    }
}
