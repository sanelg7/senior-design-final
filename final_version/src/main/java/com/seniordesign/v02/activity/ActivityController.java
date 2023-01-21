package com.seniordesign.v02.activity;

import com.seniordesign.v02.password.Password;
import com.seniordesign.v02.password.PasswordService;
import com.seniordesign.v02.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/activity")
public class ActivityController {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService){
        this.activityService = activityService;
    }


    @PostMapping(path = "gActivities")
    public String getActivities(@RequestBody ActivityRequest activityRequest){
        return activityService.getActivities(activityRequest);
    }


    @PostMapping(path = "cActivity")
    public String createActivity(@RequestBody ActivityRequest activityRequest){
        return activityService.createActivity(activityRequest);
    }

    @PostMapping(path = "uActivity")
    public String updateActivity(@RequestBody ActivityRequest activityRequest){
        return activityService.updateActivity(activityRequest);
    }

    @PostMapping(path = "dActivity")
    public String deleteActivity(@RequestBody ActivityRequest activityRequest){
        return activityService.deleteActivity(activityRequest);
    }

}
