package com.krm.repository;

import com.krm.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserReactiveRepository extends ReactiveMongoRepository<User, String> {

    @Tailable
    Flux<User> findByFirstName(String firstName);

}
