package com.example.springdb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDosDTO {
    private int id;
    private int userId;
    private String title;
    private boolean completed;
}
