package com.medium.blindindex.controllers;

import com.medium.blindindex.model.Contact;
import com.medium.blindindex.services.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private IContactService customerService;

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

    @PostMapping("/create")
    public String create(@RequestBody Contact contact) {
        customerService.createContact(contact);
        return "Created";
    }
}
