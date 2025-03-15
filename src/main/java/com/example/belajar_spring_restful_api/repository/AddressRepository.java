package com.example.belajar_spring_restful_api.repository;


import com.example.belajar_spring_restful_api.entity.Address;
import com.example.belajar_spring_restful_api.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findFirstByContactAndId(Contact contact, String id);

    List<Address> findAllByContact(Contact contact);
}
