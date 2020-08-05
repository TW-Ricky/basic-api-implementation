package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
    List<RsEvent> rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("热搜来了", "热搜", user));
    rsEvents.add(new RsEvent("天气好热，没有空调", "难受", user));
    return rsEvents;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventList(@PathVariable int index) {
      return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  private ResponseEntity getRsListBetween(@RequestParam(required = false) Integer start
          ,@RequestParam(required = false) Integer end) {
    if (start == null) {
      start = 1;
    }
    if (end == null) {
      end = rsList.size();
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  private ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    UserController userController = new UserController();
    if (!userController.checkUser(rsEvent.getUser().getUserName())) {
      userController.addUser(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    return ResponseEntity.created(null).header("index", String.valueOf(rsList.size())).build();
  }

  @PatchMapping("/rs/{index}")
  private ResponseEntity updateRsEvent(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
    RsEvent newRsEvent = rsList.get(index - 1);
    if (rsEvent.getEventName() != null) {
      newRsEvent.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null) {
      newRsEvent.setKeyWord(rsEvent.getKeyWord());
    }
    rsList.set(index - 1, newRsEvent);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{index}")
  private ResponseEntity deleteRsEvent(@PathVariable Integer index) {
    rsList.remove(index - 1);
    return ResponseEntity.ok().build();
  }
}
