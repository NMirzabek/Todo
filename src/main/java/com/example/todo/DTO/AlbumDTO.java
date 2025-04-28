package com.example.springdb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private int id;
     private int userId;
     private String title;
}
