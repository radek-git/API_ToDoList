package org.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskData {

    private Boolean completed;
    private String _id;
    private String description;
    private String owner;
    private String createdAt;
    private String updatedAt;
    private int __v;

}
