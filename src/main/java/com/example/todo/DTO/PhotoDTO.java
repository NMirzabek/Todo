package com.example.springdb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private int id;
    private int albumId;
    private String title;
    private String url;
    private String thumbnailUrl;
}
