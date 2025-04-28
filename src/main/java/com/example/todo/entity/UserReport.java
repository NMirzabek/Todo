package com.example.springdb.entity;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class UserReport {
    String username;
     Long albumCount;
     Long postCount;
     Long toDoCount;
}
