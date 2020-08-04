package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
