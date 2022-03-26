package com.medium.blindindex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto implements DTOMapper<Contact> {

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    @Override
    public void mapFrom(Contact contact) {
        this.setFirstName(contact.getFirstName());
        this.setLastName(contact.getLastName());
    }

    @Override
    public Contact mapToDomain() {
        Contact contact = new Contact();
        contact.setFirstName(this.getFirstName());
        contact.setLastName(this.getLastName());
        contact.setEmailAddress(this.getEmail());
        contact.setMobileNumber(this.getMobile());
        return contact;
    }
}
