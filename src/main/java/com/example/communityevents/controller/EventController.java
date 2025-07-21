package com.example.communityevents.controller;

import com.example.communityevents.model.Event;
import com.example.communityevents.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.getEventsFiltered(location, category, date);
    }

    @GetMapping("/search")
    public List<Event> searchEvents(@RequestParam("query") String query) {
        return eventService.searchEvents(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return eventService.getEvent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/{id}/attend")
    public ResponseEntity<?> attendEvent(@PathVariable Long id) {
        boolean joined = eventService.incrementAttendees(id);
        return joined ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Max capacity reached");
    }
}
