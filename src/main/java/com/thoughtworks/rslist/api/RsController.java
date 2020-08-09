package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.exception.UserNotExistsException;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
public class RsController {

  @Autowired
  UserService userService;

  @Autowired
  RsEventService rsEventService;

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventList(@PathVariable int index) {
    return ResponseEntity.ok(rsEventService.getRsEventById(index));
  }

  @GetMapping("/rs/list")
  private ResponseEntity getRsListBetween(@RequestParam(required = false) Integer start
          ,@RequestParam(required = false) Integer end) {

    return ResponseEntity.ok(rsEventService.subRsEventList(start, end));
  }

  @PostMapping("/rs/event")
  private ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!userService.existsUserById(rsEvent.getUserId())) {
      throw new UserNotExistsException("user not exists");
    }
    String id = String.valueOf(rsEventService.addRsEvent(rsEvent));
    return ResponseEntity.created(null).header("id", id).build();
  }

  @PatchMapping("/rs/{rsEventId}")
  private ResponseEntity updateRsEvent(@PathVariable Integer rsEventId, @RequestBody RsEvent newRsEvent) {
    rsEventService.updateRsEventById(rsEventId, newRsEvent);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{index}")
  private ResponseEntity deleteRsEvent(@PathVariable Integer index) {
    rsEventService.deleteRsEventById(index);
    return ResponseEntity.ok().build();
  }
}
