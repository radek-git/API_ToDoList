package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private int age;
    private String _id;
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
    private int __v;
}
