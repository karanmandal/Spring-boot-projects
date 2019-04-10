package com.krm.controller;

import com.krm.components.WordCount;
import com.krm.model.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api")
@RestController
public class ApiController {

    @Autowired
    private WordCount wordCount;

    @RequestMapping("wordcount")
    public ResponseEntity<List<Count>> words(@RequestParam("input") String input) {
        return new ResponseEntity<>(wordCount.count(input), HttpStatus.OK);
    }
}