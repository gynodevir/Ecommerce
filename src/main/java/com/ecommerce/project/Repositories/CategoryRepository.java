package com.ecommerce.project.Repositories;

import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

///entity and primary key
public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByCategoryName(String categoryname);

}
