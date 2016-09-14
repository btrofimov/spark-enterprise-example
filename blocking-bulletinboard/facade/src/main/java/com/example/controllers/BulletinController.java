package com.example.controllers;

import com.example.commands.AddBulletin;
import com.example.events.CmdCompleted;
import com.example.messagebus.MessageBus;
import com.example.viewmodels.bulletin.Bulletin;
import com.example.viewmodels.bulletin.BulletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
    public DeferredResult<Bulletin> create(@RequestBody AddBulletin newBulletin) {

        final DeferredResult<Bulletin> deferredResult = new DeferredResult<>();

        messageBus.send(newBulletin, cmdCompleted -> onCompleted(newBulletin.getId(), cmdCompleted, deferredResult));

        return deferredResult;
    }

    private void onCompleted(
            final String cmdId,
            final CmdCompleted cmdCompleted,
            final DeferredResult<Bulletin> deferredResult) {

        if(cmdCompleted.getSucceeded()) {

            deferredResult.setResult(repo.findOne(cmdId));
        } else {

            deferredResult.setErrorResult(new RuntimeException(cmdCompleted.getErrorMessage()));
        }

    }
}
