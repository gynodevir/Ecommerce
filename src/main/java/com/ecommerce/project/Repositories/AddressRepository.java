package com.ecommerce.project.Repositories;

import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
