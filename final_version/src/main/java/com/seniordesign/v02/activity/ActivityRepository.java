package com.seniordesign.v02.activity;

import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findActivityByUser_Email(String email);
    Activity findActivityByActivityID(Long activityID);


    //List<Activity> findByActivityDayAndUser_Email(String day,String email);
    List<Activity> findByActivityDayContainingAndUser_Email(String day,String email);

    /*To hibernate
    SELECT *
    FROM Activity a Where day like monday
    INNER JOIN user u
    ON a.userid = u.id


    @Query("SELECT * FROM application_activities a WHERE a.activityDay LIKE %:title%")*/


}
