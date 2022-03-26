package com.medium.blindindex.services;

import com.medium.blindindex.model.Contact;
import com.medium.blindindex.model.ContactDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ContactService implements IContactService {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private SecurityService securityService;

    @Override
    public void createContact(ContactDto contactDto) {
        try {
            Contact contact = contactDto.mapToDomain();
            contact.setEmail_bidx(securityService.generateBlindIndex(contactDto.getEmail()));
            contact.setMobile_bidx(securityService.generateBlindIndex(contactDto.getMobile()));
            contact.setMobileNumber(securityService.encrypt(contactDto.getMobile()));
            contact.setEmailAddress(securityService.encrypt(contactDto.getEmail()));

            template.save(contact);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ContactDto searchContactByEmailAndOrMobile(String email, String mobileNumber) {
        try {
            if (StringUtils.hasLength(email) || StringUtils.hasLength(mobileNumber)) {
                Query query = new Query();
                if (StringUtils.hasLength(email)) {
                    query.addCriteria(Criteria.where("email_bidx").is(securityService.generateBlindIndex(email)));
                }

                if (StringUtils.hasLength(mobileNumber)) {
                    query.addCriteria(Criteria.where("mobile_bidx").is(securityService.generateBlindIndex(mobileNumber)));
                }

                List<Contact> all = template.find(query, Contact.class);
                all.forEach(System.out::println);

                return all.stream().filter(c -> isMatchingContact(c, email, mobileNumber)).findFirst().map(c -> {
                    ContactDto contactDto = new ContactDto();
                    contactDto.mapFrom(c);
                    try {
                        contactDto.setEmail(securityService.decrypt(c.getEmailAddress()));
                        contactDto.setMobile(securityService.decrypt(c.getMobileNumber()));
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                        e.printStackTrace();
                    }

                    return contactDto;
                }).orElseThrow(NoSuchElementException::new);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;

    }

    private boolean isMatchingContact(Contact contact, String email, String mobile) {
        boolean result = false;

        if (StringUtils.hasLength(email)) {
            try {
                result = securityService.decrypt(contact.getEmailAddress()).equals(email);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }

        if (result && StringUtils.hasLength(mobile)) {
            try {
                result = securityService.decrypt(contact.getMobileNumber()).equals(mobile);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
