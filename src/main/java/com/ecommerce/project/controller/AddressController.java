package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;
    @Autowired
    AddressService addressService;
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAdress(@Valid @RequestBody AddressDTO addressDTO){
        User user=authUtil.loggedInUser();
        AddressDTO savedAddressDTO=addressService.createAddress(addressDTO,user);
        return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressList=addressService.getAddresses();
        return new ResponseEntity<>(addressList,HttpStatus.OK);
    }

    @GetMapping("/addresses/{address_id}")
    public ResponseEntity<AddressDTO> getAddressesbyId(@PathVariable Long address_id){
        AddressDTO addressDTO=addressService.getAddressById(address_id);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesbyUser(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addressDTOS=addressService.getAddressByUser(user);

        return new ResponseEntity<>(addressDTOS,HttpStatus.OK);
    }
    @PutMapping("/addresses/{address_id}")
    public ResponseEntity<AddressDTO> UpdateAddress(@PathVariable Long address_id,@RequestBody AddressDTO addressDTO){
        AddressDTO updatedAdress=addressService.updateAdress(address_id,addressDTO);
        return new ResponseEntity<>(updatedAdress,HttpStatus.OK);
    }
    @DeleteMapping("/addresses/{address_id}")
    public ResponseEntity<String> DeleteAddress(@PathVariable Long address_id){
        String status=addressService.deleteAddress(address_id);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }



}
