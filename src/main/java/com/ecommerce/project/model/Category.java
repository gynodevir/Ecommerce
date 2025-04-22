package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryid;
    @NotBlank
    @Size(min = 5,message = "Category name must contain atleast 5 characters")

    private String categoryName;



//    public Category(Long categoryid, String categoryname) {
//        this.categoryid = categoryid;
//        this.categoryname = categoryname;
//    }
//    public Category() {
//        // Default constructor required by JPA and Jackson
//    }
//
//    public Long getCategoryid() {
//        return categoryid;
//    }
//
//    public void setCategoryid(Long categoryid) {
//        this.categoryid = categoryid;
//    }
//
//    public String getCategoryname() {
//        return categoryname;
//    }
//
//    public void setCategoryname(String categoryname) {
//        this.categoryname = categoryname;
//    }
}
