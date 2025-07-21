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

    public List<Event> getEventsFiltered(String location, String category, LocalDate date) {
        if (location != null && category != null && date != null) {
            return eventRepository.findByLocationContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndDate(location, category, date);
        } else if (location != null) {
            return eventRepository.findByLocationContainingIgnoreCase(location);
        } else if (category != null) {
            return eventRepository.findByCategoryContainingIgnoreCase(category);
        } else if (date != null) {
            return eventRepository.findByDate(date);
        } else {
            return eventRepository.findAll();
        }
    }

    public List<Event> searchEvents(String query) {
        return eventRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
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
