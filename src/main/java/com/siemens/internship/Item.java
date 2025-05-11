package com.siemens.internship;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(0L)
    private Long id;

    //name validation
    @Pattern(regexp = "^[a-zA-Z]{1,20}$", message = "Invalid name!")
    private String name;

    //description validation
    @Pattern(regexp = "^.{1,200}$", message = "Invalid description!")
    private String description;

    //status validation
    @Pattern(regexp = "^[a-zA-Z]{1,20}$", message = "Invalid status!")
    private String status;

    //email validation
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email!")
    private String email;
}