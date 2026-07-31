package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.CupboardDAO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cupboard")
public class CupboardController {
         
    private final CupboardDAO fileDAO;

    //sets the fileDAO
    public CupboardController(CupboardDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    //gets a list of all needs
    @GetMapping("")
    public ResponseEntity<Need[]> getAllNeeds() {
        try {
            Need[] needs = fileDAO.getNeeds().toArray(new Need[0]);
            return new ResponseEntity<>(needs, HttpStatus.OK);

        } 
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //searches for a need by name
    @GetMapping("/search")
    public List<Need> searchNeeds(String name){
        try {
            return fileDAO.findNeeds(name);
        } 
        catch (IOException e) {
            return null;
        }
    }

    //creates a need
    @PostMapping
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        try {
            return new ResponseEntity<>(fileDAO.createNeed(need), HttpStatus.OK);
        } 
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //gets a need
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {

        try {
            Need need = this.fileDAO.getNeedByID(id);
            if (need == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(need, HttpStatus.OK);

        } 
        catch(IOException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //updates a need
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        try {
            Need updated = fileDAO.updateNeed(need);
            if (updated == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } 
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 
    //deletes a need by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNeed(@PathVariable int id) {
        try {
            boolean deleted = fileDAO.deleteNeed(id);

            if (!deleted) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
 
        } 
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //gets all needs by user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<Need[]> getNeedsByUser(@PathVariable int userId) {
        try {
            Need[] needs = fileDAO.getNeedsByUser(userId).toArray(new Need[0]);
            return new ResponseEntity<>(needs, HttpStatus.OK);
        } 
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}