package com.medium.blindindex.services;

import com.medium.blindindex.model.Contact;
import com.medium.blindindex.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ContactService implements IContactService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private SecurityService securityService;

    @Override
    public void createContact(Contact contact) {
        try {
            contact.setEmail_bidx(securityService.generateBlindIndex(contact.getEmailAddress()));
            contact.setMobile_bidx(securityService.generateBlindIndex(contact.getMobileNumber()));
            contact.setMobileNumber(securityService.encrypt(contact.getMobileNumber()));
            contact.setEmailAddress(securityService.encrypt(contact.getEmailAddress()));

            repository.save(contact);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

    }
}
