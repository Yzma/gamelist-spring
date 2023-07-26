package com.game.gamelist;

import com.game.gamelist.entity.GameJournal;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
public class GameListJsonTests {
    @Autowired
    private JacksonTester<GameJournal> json;

    @Autowired
    private JacksonTester<GameJournal[]> jsonList;

    private GameJournal[] gameJournals;

    @BeforeEach
    void setUp() {
        gameJournals = Arrays.array(
                new GameJournal(99L, "Test Content 99"),
                new GameJournal(100L, "Test Content 100"),
                new GameJournal(101L, "Test Content 101")
        );
    }

    @Test
    public void cashCardSerializationTest() throws IOException {
        GameJournal gameJournal = gameJournals[0];
        assertThat(json.write(gameJournal)).isStrictlyEqualToJson("gameJournal.json");
        assertThat(json.write(gameJournal)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(gameJournal)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(gameJournal)).hasJsonPathNumberValue("@.content");
        assertThat(json.write(gameJournal)).extractingJsonPathNumberValue("@.content")
                .isEqualTo("Test Content 99");
    }

}