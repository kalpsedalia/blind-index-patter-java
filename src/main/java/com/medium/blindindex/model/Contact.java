package com.medium.blindindex.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("contact")
@Data
@NoArgsConstructor
public class Contact {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String email_bidx;

    private String mobileNumber;

    private String mobile_bidx;

}
