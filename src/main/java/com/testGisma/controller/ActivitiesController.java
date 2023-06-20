package com.testGisma.controller;

import com.testGisma.dao.ActivitiesDAO;
import com.testGisma.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ActivitiesController {
    
    @Autowired
    private ActivitiesDAO activitiesDAO;

    @GetMapping("/activities")
    public List<Activity> getActivities() {
        return (List<Activity>) activitiesDAO.findAll();
    }
    
    @GetMapping("/activities/{activityId}")
    public Activity getActivity(@PathVariable Long activityId) throws ResponseStatusException {
        return activitiesDAO.findById(activityId).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"); } );
    }

    @PostMapping("/activities")
    void addActivity(@RequestBody Activity activity) {
        activitiesDAO.save(activity);
    }
    
    @PutMapping("/activities/{activityId}")
    void putActivity(@RequestBody Activity activity,@PathVariable Long activityId) throws ResponseStatusException {
        Optional<Activity> dbTask = activitiesDAO.findById(activityId);
        if (dbTask.isPresent())
        {
            activity.setId(activityId);
            activitiesDAO.save(activity);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found");
        }
    }
    
    @DeleteMapping("/activities/{activityId}")
    void deleteActivity(@PathVariable Long activityId) throws ResponseStatusException {
        Optional<Activity> dbTask = activitiesDAO.findById(activityId);
        if (dbTask.isPresent())
        {
            activitiesDAO.deleteById(activityId);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found");
        }
    }
}
