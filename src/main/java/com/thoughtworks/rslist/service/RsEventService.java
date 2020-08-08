package com.thoughtworks.rslist.service;


import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RsEventService {
    List<RsEvent> getRsEventList();

    RsEvent getRsEventByIndex(Integer index);

    List<RsEvent> subRsEventList(Integer start, Integer end);

    Integer addRsEvent(RsEvent rsEvent);

    void updateRsEventByIndex(Integer index, RsEvent rsEvent);

    void deleteRsEventByIndex(Integer index);

    void deleteAll();

}