package com.krm.controller;

import com.krm.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/send-request/{username}")
    public ResponseEntity sendRequest(@PathVariable String username) {
        return friendService.sendFriendRequest(username);
    }

    @GetMapping("/sent-requests")
    public ResponseEntity getSentRequests() {
        return friendService.getAllSentRequests();
    }

    @GetMapping("/receive-requests")
    public ResponseEntity getReceivedRequests() {
        return friendService.getAllReceivedRequests();
    }

    @PostMapping("/cancel-request/{username}")
    public ResponseEntity cancelFriendRequest(@PathVariable String username) {
        return friendService.cancelFriendRequest(username);
    }

    @PostMapping("/reject-request/{username}")
    public ResponseEntity rejectFriendRequest(@PathVariable String username) {
        return friendService.rejectFriendRequest(username);
    }

}
