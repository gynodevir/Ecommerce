package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();


    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressByUser(User user);


    AddressDTO updateAdress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
