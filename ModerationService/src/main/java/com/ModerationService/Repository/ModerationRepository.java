package com.ModerationService.Repository;

import com.ModerationService.Entity.BannedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ModerationRepository extends MongoRepository<BannedUser , String> {

    Optional<BannedUser> findByRoomId(String roomId);

}
