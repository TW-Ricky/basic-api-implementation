package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("热搜来了", "热搜"));
    rsEvents.add(new RsEvent("天气好热，没有空调", "难受"));
    return rsEvents;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventList(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  private List<RsEvent> getRsListBetween(@RequestParam(required = false) Integer start
          ,@RequestParam(required = false) Integer end) {
    if (start == null) {
      start = 1;
    }
    if (end == null) {
      end = rsList.size();
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  private void addRsEvent(@RequestBody RsEvent rsEvent) {
    rsList.add(rsEvent);
  }

}
