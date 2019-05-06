package com.krm.service;

import com.krm.contants.Event;
import com.krm.exception.BadRequestException;
import com.krm.exception.InvalidUserException;
import com.krm.model.Friend;
import com.krm.model.User;
import com.krm.repository.FriendRepository;
import com.krm.repository.UserRepository;
import com.krm.util.OneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static com.krm.contants.ResponseMessage.*;

@Service
public class FriendService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    public ResponseEntity sendFriendRequest(String username) {

        User senderUser = getCurrentUser().get();
        User receiverUser = userRepository.findByUsername(username);

        // Check for invalid user and request.
        if (receiverUser == null ||
                senderUser.getId().equals(receiverUser.getId())) {
            throw new InvalidUserException();
        }

        Friend friend = new Friend(senderUser, receiverUser);

        // Don't create duplicate request.
        if (receiverUser.getFriends().contains(friend)
                && senderUser.getFriends().contains(friend)) {

            Friend oldFriend = receiverUser.getFriends().stream().filter(f
                    -> f != null && f.getSenderId().equals(senderUser.getId())).findFirst().get();

            if (oldFriend.isAccepted()) {
                throw new BadRequestException();
            } else {

                // Remove the old request
                friendRepository.delete(oldFriend);

                receiverUser.getFriends().remove(oldFriend);
                senderUser.getFriends().remove(oldFriend);
            }
        }

        Friend newFriend = friendRepository.save(friend);

        // Create a request on receiver side.
        receiverUser.getFriends().add(newFriend);
        createEvent(Event.FRIEND_REQUEST_RECEIVE, receiverUser);

        // Create a request on sender side
        senderUser.getFriends().add(newFriend);
        createEvent(Event.FRIEND_REQUEST_SENT, senderUser);

        // Update both side
        userRepository.saveAll(Arrays.asList(receiverUser, senderUser));

        return ResponseEntity.ok(new OneResponse(FRIEND_REQUEST_SENT));
    }

    private void createEvent(Event eventType, User user) {

        boolean eventNotExists = user.getEvents().stream().noneMatch(e ->
                e != null && e.equals(eventType));

        if (eventNotExists) {
            user.getEvents().add(eventType);
        }
    }

    public ResponseEntity rejectFriendRequest(String username) {

        User currentUser = getCurrentUser().get();
        User senderUser = userRepository.findByUsername(username);

        // Check for invalid user and request.
        if (senderUser == null ||
                currentUser.getId().equals(senderUser.getId())) {
            throw new InvalidUserException();
        }

        Optional<Friend> receiverSideRequest = currentUser.getFriends().stream().filter(friend
                -> friend != null && friend.getSenderId().equals(senderUser.getId())).findFirst();

        if (receiverSideRequest.isPresent()) {
            Friend friend = receiverSideRequest.get();
            friend.setAccepted(false);

            currentUser.getFriends().remove(friend);

            friendRepository.save(friend);
            userRepository.save(currentUser);

            return ResponseEntity.ok(new OneResponse(FRIEND_REQUEST_REJECTED));
        }

        throw new InvalidUserException();
    }

    public ResponseEntity cancelFriendRequest(String username) {

        User currentUser = getCurrentUser().get();
        User receiverUser = userRepository.findByUsername(username);

        // Check for invalid user and request.
        if (receiverUser == null ||
                currentUser.getId().equals(receiverUser.getId())) {
            throw new InvalidUserException();
        }

        // Get friend request at receiver side
        Optional<Friend> receiverSideRequest = receiverUser.getFriends().stream().filter((Friend t)
                -> t != null && t.getSenderId().equals(currentUser.getId())).findFirst();

        if (receiverSideRequest.isPresent()) {

            Friend friend = receiverSideRequest.get();

            currentUser.getFriends().remove(friend);
            receiverUser.getFriends().remove(friend);

            userRepository.saveAll(Arrays.asList(currentUser, receiverUser));
            friendRepository.delete(friend);
            return ResponseEntity.ok(new OneResponse(FRIEND_REQUEST_CANCELED));
        }

        throw new InvalidUserException();
    }

    public ResponseEntity getAllSentRequests() {
        User currentUser = getCurrentUser().get();
        Stream<Friend> filter = currentUser.getFriends().stream().filter((t)
                -> t.getSenderId().equals(currentUser.getId()));

        return ResponseEntity.ok(new OneResponse(filter.toArray()));
    }

    public ResponseEntity getAllReceivedRequests() {

        User currentUser = getCurrentUser().get();

        Stream<Friend> filter = currentUser.getFriends().stream().filter((t)
                -> !t.getSenderId().equals(currentUser.getId()));

        return ResponseEntity.ok(new OneResponse(filter.toArray()));
    }

}
