package com.ecommerce.project.Repositories;

import com.ecommerce.project.model.Cartitem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<Cartitem,Long> {
    @Query("SELECT ci FROM Cartitem ci WHERE ci.cart.id=?1 AND ci.product.id=?2")
    Cartitem findCartItemByProductIdAndCartId(long cartid, Long productId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Cartitem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
}
