package com.seniordesign.v02.activity;

import com.seniordesign.v02.user.User;
import com.seniordesign.v02.user.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public String getActivities(ActivityRequest activityRequest) {
        List<Activity> activityByUserEmail = activityRepository.findActivityByUser_Email(activityRequest.getEmail());
        Optional<User> userOptional = userRepository.findUserByEmail(activityRequest.getEmail());
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","No user found.").toString();
            return json;
        }
        User user = userOptional.get();
        boolean tokenMatch = activityRequest.getToken().equals(user.getSessionToken().getToken());

        if(tokenMatch) {

            JSONObject theObject = new JSONObject();
            theObject.put("status" , "success");
            JSONObject json = new JSONObject();
            JSONArray activityJson = new JSONArray();

            for (Activity a : activityByUserEmail) {
                json.put("activityID", a.getActivityID());
                json.put("activityName", a.getActivityName());
                json.put("activityDescription", a.getActivityDescription());
                if(a.getActivityContent()==null){
                    json.put("activityContent", "You can put your notes here...");

                }else{
                    json.put("activityContent", a.getActivityContent());

                }
                json.put("activityDay", a.getActivityDay());
                json.put("activityStartTime", a.getActivityStartTime());
                json.put("activityEndTime", a.getActivityEndTime());
                activityJson.put(json);
                json = new JSONObject();
            }
            theObject.put("activities",activityJson);
            return theObject.toString();

        }

        else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","Your session has expired.Please logout and re-login..").toString();
            return json;
        }
    }


    public String createActivity(ActivityRequest activityRequest) {
        String email = activityRequest.getEmail();

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {

            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user with this email.")
                    .toString();
            return json;

        } else {
            User user = userOptional.get();

            if (activityRequest.getActivityStartTime().compareTo(activityRequest.getActivityEndTime()) == 0) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Activity start and end times can not be equal.")
                        .toString();
                return json;
            } else if (activityRequest.getActivityStartTime().compareTo(activityRequest.getActivityEndTime()) > 0) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Activity start time can not be greater than activity end time.")
                        .toString();
                return json;
            }else{
                List<Activity> activitiesOnTheSameDay = activityRepository.
                        findActivityByUser_Email(user.getEmail());
                activitiesOnTheSameDay.removeIf(d -> !d.getActivityDay().equals(activityRequest.getActivityDay()));



                boolean available = true;
                for (Activity a : activitiesOnTheSameDay){
                    String start = a.getActivityStartTime();
                    String end = a.getActivityEndTime();

                    if((start.equals(activityRequest.getActivityStartTime()) && end.equals(activityRequest.getActivityEndTime()))
                            || (start.compareTo(activityRequest.getActivityStartTime()) > 0 && activityRequest.getActivityEndTime().compareTo(start) > 0)
                            || (end.compareTo(activityRequest.getActivityStartTime()) > 0 && activityRequest.getActivityEndTime().compareTo(end) > 0)){
                    available = false;

                    }

                }
                if(available == true){
                    Activity createdActivity = new Activity(activityRequest.getActivityName(),
                            activityRequest.getActivityDescription(),
                            activityRequest.getActivityDay(),
                            activityRequest.getActivityStartTime(),
                            activityRequest.getActivityEndTime(),
                            user);
                    boolean tokenMatch = activityRequest.getToken().equals(user.getSessionToken().getToken());
                    if(tokenMatch) {
                        activityRepository.save(createdActivity);
                        String json = new JSONObject()
                                .put("status", "success")
                                .put("message", "Activity has been created successfully.")
                                .toString();
                        return json;
                    }else{
                        String json = new JSONObject()
                                .put("status", "failure")
                                .put("errorMessage", "Your session has expired.Please logout and re-login..")
                                .toString();
                        return json;
                    }
                }else{
                    String json = new JSONObject()
                            .put("status", "failure")
                            .put("errorMessage", "Timeslot is taken by another activity.")
                            .toString();
                    return json;

                }

            }

        }


    }

    public String deleteActivity(ActivityRequest activityRequest) {
        Long activityId = activityRequest.getActivityID();
        Activity activity = activityRepository.findActivityByActivityID(activityId);

        boolean exists = activityRepository.existsById(activityId);
        if (!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No activity found to delete.")
                    .toString();
            return json;        }
        boolean tokenMatch = activityRequest.getToken().equals(activity.getUser().getSessionToken().getToken());
        if(tokenMatch) {
            activityRepository.deleteById(activityId);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Activity has been deleted successfully.")
                    .toString();
            return json;
        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Your session has expired.Please logout and re-login..")
                    .toString();
            return json;
        }


    }

    @Transactional
    public String updateActivity(ActivityRequest activityRequest) {
        String email = activityRequest.getEmail();
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {

            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user with this email.")
                    .toString();
            return json;

        } else {
            User user = userOptional.get();

            List<Activity> activitiesOnTheSameDay = activityRepository.
                    findActivityByUser_Email(user.getEmail());
            activitiesOnTheSameDay.removeIf(d -> !d.getActivityDay().equals(activityRequest.getActivityDay()));

            }
        Optional<Activity> activityOptional = activityRepository.findById(activityRequest.getActivityID());
        boolean exists = activityOptional.isPresent();
        if(!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No activity found to update.")
                    .toString();
            return json;


        }
        Activity activity = activityOptional.get();
        activity.setActivityContent(activityRequest.getActivityContent());

        boolean tokenMatch = activityRequest.getToken().equals(activity.getUser().getSessionToken().getToken());
        if(tokenMatch) {
            activityRepository.save(activity);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Activity has been updated successfully.")
                    .toString();
            return json;
        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Your session has expired.Please logout and re-login..")
                    .toString();
            return json;
        }





        }
    }


