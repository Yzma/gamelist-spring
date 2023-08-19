package com.game.gamelist.service.impl;

import com.game.gamelist.entity.*;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.*;
import com.game.gamelist.service.LikeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final InteractiveEntityRepository interactiveEntityRepository;

//    Sending ID as a parameter and check if they are instance of Post or GameJournal
    @Override
    public LikeEntity createLike(User principle, Long interactiveEntityId) {

        // Check if the user has already liked the InteractiveEntity
        boolean alreadyLiked = likeRepository.existsByUserIdAndInteractiveEntityId(principle.getId(), interactiveEntityId);

        if (alreadyLiked) {
            throw new IllegalStateException("You have already liked this entity.");
        }

        User owner = userRepository.findById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LikeEntity like = new LikeEntity();

        Optional<InteractiveEntity> interactiveEntityOptional = interactiveEntityRepository.findById(interactiveEntityId);

        like.setUser(owner);

        if(interactiveEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("The entity is not found");
        }

        InteractiveEntity interactiveEntity = interactiveEntityOptional.get();

        if(interactiveEntity instanceof Post || interactiveEntity instanceof GameJournal) {
            like.setInteractiveEntity(interactiveEntity);

        } else {
            throw new RuntimeException("Invalid likeable entity");
        }

        return likeRepository.save(like);
    }

    @Override
    public void deleteLike(User principle, Long interactiveEntityId) {
//        LikeEntity existingLike = likeRepository.findByUserAndLikeable(principle, likeableEntity);
//        if (existingLike != null) {
//            likeRepository.delete(existingLike);
//        }
    }
}
