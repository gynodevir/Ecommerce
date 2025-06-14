package com.ecommerce.project.service;

import com.ecommerce.project.Repositories.AddressRepository;
import com.ecommerce.project.Repositories.UserRepository;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressImp implements AddressService {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO,Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress=addressRepository.save(address);
         return modelMapper.map(savedAddress,AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses=addressRepository.findAll();
        List<AddressDTO> addressDTOS=addresses.stream().map((element) -> modelMapper.map(element, AddressDTO.class))
                .toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));// or a custom exception;

        AddressDTO addressDTO=modelMapper.map(address,AddressDTO.class);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getAddressByUser(User user) {
        List<Address> addresses=user.getAddresses();
        List<AddressDTO> addressDTOS=addresses.stream().map((e)->modelMapper.map(e,AddressDTO.class))
                .toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO updateAdress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressFromDatabase.setCity(addressDTO.getCity());
        addressFromDatabase.setPincode(addressDTO.getPincode());
        addressFromDatabase.setState(addressDTO.getState());
        addressFromDatabase.setCountry(addressDTO.getCountry());
        addressFromDatabase.setStreet(addressDTO.getStreet());
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress=addressRepository.save(addressFromDatabase);
        User user=addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);


        return modelMapper.map(updatedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        User user=addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDatabase);
        return "Address deleted sucessfully with addressId : "+addressId;
    }


}
