package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDTO;
import com.thoughtworks.rslist.dto.UserDTO;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.exception.UserIdNotMatchException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RsEventServiceImpl implements RsEventService {
    @Autowired
    RsEventRepository rsEventRepository;

    private RsEvent changeRsEventDTOToRsEvent(RsEventDTO rsEventDTO) {
        return RsEvent.builder()
                .eventName(rsEventDTO.getEventName())
                .keyword(rsEventDTO.getEventName())
                .userId(rsEventDTO.getUserDTO().getId())
                .build();
    }
    private RsEventDTO changeRsEventToRsEventDTO(RsEvent rsEvent) {
        return RsEventDTO.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userDTO(UserDTO.builder().id(rsEvent.getUserId()).build())
                .build();
    }
    @Override
    public List<RsEvent> getRsEventList() {
        return rsEventRepository.findAll().stream()
                .map(item -> changeRsEventDTOToRsEvent(item))
                .collect(Collectors.toList());
    }

    @Override
    public RsEvent getRsEventById(Integer id) {
        if (!rsEventRepository.existsById(id)) {
            throw new RsEventNotValidException("invalid index");
        }
        return changeRsEventDTOToRsEvent(rsEventRepository.findById(id).get());
    }

    @Override
    public List<RsEvent> subRsEventList(Integer start, Integer end) {
        List<RsEvent> rsList = rsEventRepository.findAll().stream()
                .map(item -> changeRsEventDTOToRsEvent(item))
                .collect(Collectors.toList());
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
        RsEventDTO rsEventDTO = rsEventRepository.save(changeRsEventToRsEventDTO(rsEvent));
        return rsEventDTO.getId();
    }

    @Override
    public void updateRsEventById(Integer id, RsEvent rsEvent) {
        RsEventDTO rsEventDTO = rsEventRepository.findById(id).get();
        if (!id.equals(rsEventDTO.getUserDTO().getId())) {
            throw new UserIdNotMatchException("userId not match");
        }
        if (rsEvent.getEventName() != null) {
            rsEventDTO.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyword() != null) {
            rsEventDTO.setKeyword(rsEvent.getKeyword());
        }
        rsEventRepository.save(rsEventDTO);
    }

    @Override
    public void deleteRsEventById(Integer id) {
        if (!rsEventRepository.existsById(id)) {
            throw new RsEventNotValidException("invalid index");
        }
        rsEventRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        rsEventRepository.deleteAll();
    }
}
