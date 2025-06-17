package com.ModerationService.Service;


import com.ModerationService.Entity.BannedRequest;
import com.ModerationService.Entity.BannedUser;
import com.ModerationService.Repository.ModerationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ModerationService {

    private ModerationRepository moderationRepository;

    public ModerationService(ModerationRepository moderationRepository) {
        this.moderationRepository = moderationRepository;
    }


    public boolean banUser(BannedRequest bannedRequest) {
        Optional<BannedUser> OpBannedUser = moderationRepository.findByRoomId(bannedRequest.getRoomId());

        if(OpBannedUser.isPresent()) {
            BannedUser bannedUser = OpBannedUser.get();
            bannedUser.getUsernames().add(bannedRequest.getUsername());
            log.info(bannedUser.toString());
            moderationRepository.save(bannedUser);
        }
        else{
            BannedUser bannedUser = new BannedUser();
            bannedUser.setRoomId(bannedRequest.getRoomId());
            bannedUser.getUsernames().add(bannedRequest.getUsername());
            moderationRepository.save(bannedUser);
        }

        return true;
    }

    public boolean deleteByRoomIdAndUsername(String roomId, String username) {

        Optional<BannedUser> optionalBannedUser = moderationRepository.findByRoomId(roomId);

        if (optionalBannedUser.isPresent()) {
            BannedUser bannedUser = optionalBannedUser.get();

            List<String> usernames = bannedUser.getUsernames();
            if (usernames.remove(username)) {
                moderationRepository.save(bannedUser); // save the updated list
                System.out.println("User removed successfully from banned list.");
                return true;
            } else {
                System.out.println("User not found in banned list.");
                return false;
            }

        } else {
            System.out.println("Room ID not found.");
            throw new RuntimeException("RoomId not present");
        }
    }

    public List<String> listUser(String roomId){

        Optional<BannedUser> optionalBannedUser = moderationRepository.findByRoomId(roomId);

        log.info(optionalBannedUser.toString());
        if(optionalBannedUser.isPresent()){
            BannedUser bannedUser = optionalBannedUser.get();
            return bannedUser.getUsernames();
        }

        return Collections.emptyList();
    }
}
