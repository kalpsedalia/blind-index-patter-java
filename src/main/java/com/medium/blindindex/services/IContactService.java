package com.medium.blindindex.services;

import com.medium.blindindex.model.ContactDto;

public interface IContactService {

    void createContact(ContactDto contactDto);

    ContactDto searchContactByEmailAndOrMobile(String email, String mobileNumber);
}
