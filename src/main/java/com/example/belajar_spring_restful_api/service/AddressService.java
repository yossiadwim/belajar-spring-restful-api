package com.example.belajar_spring_restful_api.service;


import com.example.belajar_spring_restful_api.entity.Address;
import com.example.belajar_spring_restful_api.entity.Contact;
import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.AddressResponse;
import com.example.belajar_spring_restful_api.model.CreateAddressRequest;
import com.example.belajar_spring_restful_api.model.UpdateAddressRequest;
import com.example.belajar_spring_restful_api.repository.AddressRepository;
import com.example.belajar_spring_restful_api.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;


    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setProvince(request.getProvince());
        address.setStreet(request.getStreet());
        address.setContact(contact);

        addressRepository.save(address);

        return toAddressResponse(address);

    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId){
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")
                );

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found")
                );

        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findByUserAndId(user, request.getContactId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")
                );

        Address address = addressRepository.findFirstByContactAndId(contact, request.getAddressId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found")
                );

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return toAddressResponse(address);
    }


    @Transactional
    public void remove(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")
                );

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found")
                );

        addressRepository.delete(address);

    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")
                );

        List<Address> addresses = addressRepository.findAllByContact(contact);

        return addresses.stream().map(this::toAddressResponse).toList();
    }
}
