package com.medium.blindindex.repositories;

import com.medium.blindindex.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Contact, String> {
}
