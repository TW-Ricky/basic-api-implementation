package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDTO;
import com.thoughtworks.rslist.dto.UserDTO;
import com.thoughtworks.rslist.exception.RsEventNotExistsException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.exception.UserIdNotMatchException;
import com.thoughtworks.rslist.exception.UserNotExistsException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RsEventServiceImpl implements RsEventService {
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    private RsEvent changeRsEventDTOToRsEvent(RsEventDTO rsEventDTO) {
        return RsEvent.builder()
                .id(rsEventDTO.getId())
                .eventName(rsEventDTO.getEventName())
                .keyword(rsEventDTO.getKeyword())
                .userId(rsEventDTO.getUserDTO().getId())
                .voteNum(rsEventDTO.getVoteNum())
                .build();
    }
    private RsEventDTO changeRsEventToRsEventDTO(RsEvent rsEvent, UserDTO userDTO) {
        return RsEventDTO.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .voteNum(rsEvent.getVoteNum())
                .userDTO(userDTO)
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
        Optional<UserDTO> userDTO= userRepository.findById(rsEvent.getUserId());
        if (!userDTO.isPresent()) {
            throw new UserNotExistsException("user not exists");
        }
        RsEventDTO rsEventDTO = rsEventRepository.save(changeRsEventToRsEventDTO(rsEvent, userDTO.get()));
        return rsEventDTO.getId();
    }

    @Override
    public void updateRsEventById(Integer id, RsEvent newRsEvent) {
        Optional<RsEventDTO> rsEventDTO = rsEventRepository.findById(id);
        if (!rsEventDTO.isPresent()) {
            throw new RsEventNotExistsException("rsEvent not exists");
        }
        RsEventDTO newRsEventDTO= rsEventDTO.get();
        if (!newRsEvent.getUserId().equals(newRsEventDTO.getUserDTO().getId())) {
            throw new UserIdNotMatchException("userId not match");
        }
        if (newRsEvent.getEventName() != null) {
            newRsEventDTO.setEventName(newRsEvent.getEventName());
        }
        if (newRsEvent.getKeyword() != null) {
            newRsEventDTO.setKeyword(newRsEvent.getKeyword());
        }
        rsEventRepository.save(newRsEventDTO);
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
