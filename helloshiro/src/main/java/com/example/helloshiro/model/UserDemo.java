package com.example.helloshiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDemo implements Serializable {
    private final static long serialVersionUID = 1L;

    private String id;
    private String userName;
    private String password;

}
