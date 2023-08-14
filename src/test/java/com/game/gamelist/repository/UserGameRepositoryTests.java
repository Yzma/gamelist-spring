package com.game.gamelist.repository;


import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class UserGameRepositoryTests extends ContainersEnvironment {

    @Autowired
    private UserGameRepository userGameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;


    @Test
    void loadContext() {
        assertNotEquals(null, userGameRepository);
        assertNotEquals(null, userRepository);

        System.out.println("test");
    }

    @Test
    @Transactional
    public void when_findAll_ShouldReturn_EmptyList() {
        List<UserGame> userGameList = userGameRepository.findAll();
        Assertions.assertEquals(0, userGameList.size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UserGameRepositoryCRUDTests {

        private User owner;
        private Game game1;
        private Game game2;
        private Game game3;

        @BeforeEach
        void beforeEachTest() {
            owner = User.builder().username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

            userRepository.save(owner);

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79Owner name: " + owner.getUsername());

            game1 = Game.builder().name("testgame").description("testgame").imageURL("testImage").bannerURL("testGameBanner").build();
            game1.setId(10L);
            game2 = Game.builder().name("testgame2").description("testgame2").imageURL("testImage").build();
            game2.setId(11L);
            game3 = Game.builder().name("testgame3").description("testgame3").imageURL("testImage").build();
            game3.setId(12L);


            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            Game savedGame1 = gameRepository.findById(10L).get();

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 savedGame ID: " +savedGame1.getId());

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 savedGame name: " +savedGame1.getName());


            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 Game name: " + game1.getName());


            User savedOwner = userRepository.findByEmail(owner.getEmail()).get();

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 Owner name: " + savedOwner.getId());

            UserGame userGame1 = UserGame.builder().gameNote("testNote").rating(5).gameStatus(GameStatus.Paused).user(owner).game(savedGame1).build();

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 UserGame note: " + userGame1.getGameNote());

            userGameRepository.save(userGame1);

        }

        @Test
        @Transactional
        @Order(1)
        public void when_findAll_ShouldReturn_ListWithOneElement() {
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(1, userGameList.size());
        }

        @Test
        @Transactional
        @Order(2)
        public void when_findById_ShouldReturn_UserGame() {


            Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());

            for (UserGame userGame : userGamesSet) {
                System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 UserGame note: " + userGame.getGameNote());
            }
            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            System.out.println("\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79\uD83D\uDC79 UserGame note: " + onlyUserGame.getGameNote());

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            UserGame userGame = userGameRepository.findById(onlyUserGame.getId()).get();
            Assertions.assertEquals(onlyUserGame.getUser().getId(), userGame.getUser().getId());
            Assertions.assertEquals(GameStatus.Paused, userGame.getGameStatus());
        }

        @Test
        @Transactional
        @Order(3)
        public void when_deleteById_ShouldReturn_EmptyList() {                  Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());
            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            userGameRepository.deleteById(onlyUserGame.getId());
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(0, userGameList.size());
        }

        @Test
        @Transactional
        @Order(4)
        public void when_deleteAll_ShouldReturn_EmptyList() {
            userGameRepository.deleteAll();
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(0, userGameList.size());
        }

        @Test
        @Transactional
        @Order(5)
        public void when_update_ShouldReturn_UpdatedUserGame() {
            Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());
            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            onlyUserGame.setGameStatus(GameStatus.Completed);
            onlyUserGame.setRating(4);
            onlyUserGame.setGameNote("updatedNote");

            userGameRepository.save(onlyUserGame);

            UserGame updatedUserGame = userGameRepository.findById(onlyUserGame.getId()).get();

            Assertions.assertEquals(GameStatus.Completed, updatedUserGame.getGameStatus());
            Assertions.assertEquals(4, updatedUserGame.getRating());
            Assertions.assertEquals("updatedNote", updatedUserGame.getGameNote());
        }
    }

}
