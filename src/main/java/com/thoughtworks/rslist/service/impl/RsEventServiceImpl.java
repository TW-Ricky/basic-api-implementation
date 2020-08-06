package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsDataBase;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsEventServiceImpl implements RsEventService {
    @Autowired
    RsDataBase rsDataBase;

    @Override
    public List<RsEvent> getRsEventList() {
        return rsDataBase.getRsList();
    }

    @Override
    public RsEvent getRsEventByIndex(Integer index) {
        List<RsEvent> rsList = rsDataBase.getRsList();
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return rsList.get(index - 1);
    }

    @Override
    public List<RsEvent> subRsEventList(Integer start, Integer end) {
        List<RsEvent> rsList = rsDataBase.getRsList();
        if (start == null) {
            start = 1;
        }
        if (end == null) {
            end = rsList.size();
        }
        if (start <= 0 || end > rsList.size() || start > end) {
            throw new RsEventNotValidException("invalid request param");
        }
        return rsList.subList(start - 1, end);
    }

    @Override
    public Integer addRsEvent(RsEvent rsEvent) {
        List<RsEvent> rsList = rsDataBase.getRsList();
        rsList.add(rsEvent);
        return rsList.size();
    }

    @Override
    public void updateRsEventByIndex(Integer index, RsEvent rsEvent) {
        List<RsEvent> rsList = rsDataBase.getRsList();
        RsEvent newRsEvent = rsList.get(index - 1);
        if (rsEvent.getEventName() != null) {
            newRsEvent.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            newRsEvent.setKeyWord(rsEvent.getKeyWord());
        }
        rsList.set(index - 1, newRsEvent);
    }

    @Override
    public void deleteRsEventByIndex(Integer index) {
        List<RsEvent> rsList = rsDataBase.getRsList();
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        rsList.remove(index - 1);
    }

    @Override
    public void deleteAll() {
        List<RsEvent> rsList = rsDataBase.getRsList();
        rsList.clear();
    }
}
