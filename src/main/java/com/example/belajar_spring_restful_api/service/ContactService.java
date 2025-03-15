package com.example.belajar_spring_restful_api.service;

import com.example.belajar_spring_restful_api.entity.Contact;
import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.ContactResponse;
import com.example.belajar_spring_restful_api.model.CreateContactRequest;
import com.example.belajar_spring_restful_api.model.SearchContactRequest;
import com.example.belajar_spring_restful_api.model.UpdateContactRequest;
import com.example.belajar_spring_restful_api.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    private ContactResponse toContactResponse(Contact contact){
        return ContactResponse.builder().
                id(contact.getId()).
                firstName(contact.getFirstName()).
                lastName(contact.getLastName()).
                email(contact.getEmail()).
                phone(contact.getPhone()).
                build();
    }


    @Transactional
    public ContactResponse create (User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return toContactResponse(contact);

    }



    @Transactional(readOnly = true)
    public ContactResponse get(User user, String contactId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest updateContactRequest) {
        validationService.validate(updateContactRequest);
        Contact contact = contactRepository.findByUserAndId(user, updateContactRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(updateContactRequest.getFirstName());
        contact.setLastName(updateContactRequest.getLastName());
        contact.setEmail(updateContactRequest.getEmail());
        contact.setPhone(updateContactRequest.getPhone());

        contactRepository.save(contact);

        return toContactResponse(contact);
    }


    @Transactional
    public void delete(User user, String contactId) {
        Contact contact = contactRepository.findByUserAndId(user, contactId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found")
        );

        contactRepository.delete(contact);

    }



    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification  = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if(Objects.nonNull(request.getName())){

                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + request.getName() + "%"),
                        builder.like(root.get("lastName"), "%" + request.getName() + "%")
                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            if (Objects.nonNull(request.getPhone())){
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream().map(this::toContactResponse).toList();
        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
