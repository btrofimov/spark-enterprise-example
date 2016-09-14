package com.example.controllers;

import com.example.commands.AddBulletin;
import com.example.messagebus.MessageBus;
import com.example.viewmodels.bulletin.Bulletin;
import com.example.viewmodels.bulletin.BulletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bulletins")
public class BulletinController {

    @Autowired
    protected BulletinRepository repo;

    @Autowired
    protected MessageBus messageBus;

    @RequestMapping(method= RequestMethod.GET)
    public Iterable<Bulletin> getAll() {

        return repo.findAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    public void create(@RequestBody AddBulletin newBulletin) {

        messageBus.send(newBulletin);
    }
}
