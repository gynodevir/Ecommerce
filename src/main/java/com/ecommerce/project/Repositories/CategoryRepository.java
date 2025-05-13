package com.ecommerce.project.CategoryRepository;

import com.ecommerce.project.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

///entity and primary key
public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByCategoryName(String categoryname);

}
