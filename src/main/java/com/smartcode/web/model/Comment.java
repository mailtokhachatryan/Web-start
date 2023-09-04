package com.smartcode.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Comment {

    public Comment(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }



    private Integer id;
    private String title;
    private String description;
    private Integer userId;
}
