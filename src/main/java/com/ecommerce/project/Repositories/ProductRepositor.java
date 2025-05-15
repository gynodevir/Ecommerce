package com.ecommerce.project.Repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositor extends JpaRepository<Product,Long> {
   Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDteails);

    Page<Product> findByProductNameIgnoreCaseContaining(String name, Pageable pageDteails);
}
