package com.seniordesign.v02.activity;

import com.seniordesign.v02.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "application_activities")
public class Activity {
    @Id
    @SequenceGenerator(
            name = "activity_sequence",
            sequenceName = "activity_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activity_sequence"
    )
    @Column(name = "activity_id")
    private long activityID;
    private String activityName;
    private String activityDescription;

    @Column(columnDefinition = "TEXT")
    private String activityContent;
    private String activityStartTime;
    private String activityEndTime;

    private String activityDay;


    //Many to one relation from user table.
    @JsonIgnoreProperties
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    public Activity(String activityName, String activityDescription, String activityDay, String activityStartTime, String activityEndTime,User user) {
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityDay = activityDay;
        this.activityStartTime = activityStartTime;
        this.activityEndTime = activityEndTime;
        this.user = user;

    }


    @Override
    public String toString() {
        return "Activity{" +
                "activityID=" + activityID +
                ", activityName='" + activityName + '\'' +
                ", activityDescription=" + activityDescription +
                ", activityStartTime=" + activityStartTime +
                ", activityEndTime=" + activityEndTime +
                ", activityDay=" + activityDay +
                '}';
    }


}
