package com.krm.service;

import com.krm.contants.Event;
import com.krm.model.User;
import com.krm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserReactiveService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    public Flux<List<Event>> getEventStream() {

        User currentUser = getCurrentUser().get();

        List<Event> events = currentUser.getEvents();

        if (events.isEmpty()) {
            return Flux.empty();
        }

        ArrayList<Event> eventList = new ArrayList<>(events);

        events.clear();
        userRepository.save(currentUser);

        return Flux.just(eventList);
    }
}
