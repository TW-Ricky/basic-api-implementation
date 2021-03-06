package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.dto.RsEventDTO;
import com.thoughtworks.rslist.dto.UserDTO;
import com.thoughtworks.rslist.dto.VoteEventDTO;
import com.thoughtworks.rslist.exception.RsEventNotExistsException;
import com.thoughtworks.rslist.exception.TimeErrorException;
import com.thoughtworks.rslist.exception.UserNotExistsException;
import com.thoughtworks.rslist.exception.VoteNumberOverUsersOwnException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteEventRepository;
import com.thoughtworks.rslist.service.VoteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteEventServiceImpl implements VoteEventService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteEventRepository voteEventRepository;

    @Override
    @Transactional
    public void voteRsEvent(Integer rsEventId, VoteEvent voteEvent) {
        voteEvent.setRsEventId(rsEventId);
        Optional<RsEventDTO> rsEventDTOOptional = rsEventRepository.findById(rsEventId);
        Optional<UserDTO> userDTOOptional = userRepository.findById(voteEvent.getUserId());
        if (!rsEventDTOOptional.isPresent()) {
            throw new RsEventNotExistsException("rsEvent not exists");
        }
        if (!userDTOOptional.isPresent()) {
            throw new UserNotExistsException("user not exists");
        }
        RsEventDTO rsEventDTO = rsEventDTOOptional.get();
        UserDTO userDTO = userDTOOptional.get();
        if (userDTO.getVoteNum() < voteEvent.getVoteNum()) {
            throw new VoteNumberOverUsersOwnException("more votes than users own");
        }
        VoteEventDTO voteEventDTO = changeVoteEventToVoteEventDTO(voteEvent, rsEventDTO, userDTO);
        userDTO.setVoteNum(userDTO.getVoteNum() - voteEvent.getVoteNum());
        rsEventDTO.setVoteNum(rsEventDTO.getVoteNum() + voteEvent.getVoteNum());

        voteEventRepository.save(voteEventDTO);
        rsEventRepository.save(rsEventDTO);
        userRepository.save(userDTO);
    }

    @Override
    public List<VoteEvent> getVoteEventList() {
        return voteEventRepository.findAll().stream()
                .map(item -> changeVoteEventDTOToVoteEvent(item))
                .collect(Collectors.toList());
    }

    private VoteEvent changeVoteEventDTOToVoteEvent(VoteEventDTO voteEventDTO) {
        return VoteEvent.builder()
                .rsEventId(voteEventDTO.getRsEventDTO().getId())
                .userId(voteEventDTO.getUserDTO().getId())
                .voteNum(voteEventDTO.getVoteNum())
                .voteTime(voteEventDTO.getVoteTime())
                .build();
    }

    private VoteEventDTO changeVoteEventToVoteEventDTO(VoteEvent voteEvent, RsEventDTO rsEventDTO, UserDTO userDTO) {
        return VoteEventDTO.builder()
                .rsEventDTO(rsEventDTO)
                .userDTO(userDTO)
                .voteNum(voteEvent.getVoteNum())
                .voteTime(voteEvent.getVoteTime())
                .build();
    }

    @Override
    public List<VoteEvent> getVoteRecord(Integer userId, Integer rsEventId, Integer pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return voteEventRepository.findAccordingByUserIdAndRsEventId(userId, rsEventId, pageable).stream()
                .map(item -> changeVoteEventDTOToVoteEvent(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<VoteEvent> getVoteRecordByTime(LocalDateTime startTime, LocalDateTime endTime, Integer pageIndex) {
        if (startTime.isAfter(endTime)) {
            throw new TimeErrorException("startTime after than endTime");
        }

        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return voteEventRepository.findAccordingBetweenStartTimeAndEndTime(startTime, endTime, pageable).stream()
                .map(item -> changeVoteEventDTOToVoteEvent(item))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        voteEventRepository.deleteAll();
    }
}
