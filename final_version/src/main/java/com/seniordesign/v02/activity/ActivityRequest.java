package com.seniordesign.v02.activity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ActivityRequest {

    private final String email;
    private final long activityID;
    private final String token;
    private final String activityName;
    private final String activityDescription;
    private final String activityDay;
    private final String activityStartTime;
    private final String activityEndTime;
    private final String activityContent;


}