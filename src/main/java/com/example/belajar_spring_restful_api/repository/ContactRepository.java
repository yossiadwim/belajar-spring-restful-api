package com.example.belajar_spring_restful_api.repository;

import com.example.belajar_spring_restful_api.entity.Contact;
import com.example.belajar_spring_restful_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {

    Optional<Contact> findByUserAndId(User user, String id);
}
