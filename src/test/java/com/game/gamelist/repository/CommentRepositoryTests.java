package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.Comment;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class CommentRepositoryTests extends ContainersEnvironment {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void contextLoads() {
        assertNotEquals(null, commentRepository);
        assertNotEquals(null, postRepository);
        assertNotEquals(null, likeRepository);
        assertNotEquals(null, entityManager);
    }

    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {
        List<Comment> commentList = commentRepository.findAll();
        assertEquals(0, commentList.size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CommentRepositoryCRUDTests {

        @BeforeEach
        void beforeEachTest() {
            User user = new User();
            user.setUsername("changli");
            user.setEmail("changli@gmail.com");
            user.setPassword("123456");
            user.setUserPicture("User Picture URL");
            user.setBannerPicture("Banner Picture URL");
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            Post post1 = new Post();
            post1.setText("Hello World");
            post1.setUser(user);
            post1.setCreatedAt(LocalDateTime.now());
            post1.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post1);

            Post post2 = new Post();
            post2.setText("Another Post");
            post2.setUser(user);
            post2.setCreatedAt(LocalDateTime.now());
            post2.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post2);

            LikeEntity like = new LikeEntity();
            like.setInteractiveEntity(post1);
            like.setUser(user);
            likeRepository.save(like);
        }

        @Test
        @Transactional
        @Order(1)
        public void test_beforeEach_context() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("User not found"));

            assertEquals("changli", user.getUsername());
            assertEquals("changli@gmail.com", user.getEmail());

            Post post = postRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            entityManager.refresh(post);

            assertEquals("Hello World", post.getText());
            assertEquals("changli", post.getUser().getUsername());
            assertEquals(1, post.getLikes().size());
        }

        @Test
        @Transactional
        @Order(2)
        public void when_saveComment_onPost_expect_success() {

            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            Comment comment = new Comment();
            comment.setText("This is a comment");
            comment.setInteractiveEntity(post);
            comment.setUser(owner);

            post.getComments().add(comment);

            commentRepository.save(comment);
            List<Comment> commentList = commentRepository.findAll();
            assertEquals(1, commentList.size());
            assertEquals(1, post.getComments().size());
            Post postFromDB = postRepository.findAll().get(0);

            assertEquals(1, postFromDB.getComments().size());
            assertEquals("This is a comment", postFromDB.getComments().get(0).getText());
            assertEquals("changli", postFromDB.getComments().get(0).getUser().getUsername());
        }

        @Test
        @Transactional
        @Order(3)
        public void when_saveOnComment_Expect_Success() {
            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            Comment comment = new Comment();
            comment.setText("This is a comment");
            comment.setInteractiveEntity(post);
            comment.setUser(owner);

            post.getComments().add(comment);
            commentRepository.save(comment);

            User commentOnCommentOwner = User.builder().username("commentOnCommentOwner").email("commentowner@gmail.com").password("123456").bannerPicture("banner picture").userPicture("user picture").build();
            userRepository.save(commentOnCommentOwner);

            Comment commentOfComment = new Comment();
            commentOfComment.setText("This is a comment of comment");
            commentOfComment.setUser(commentOnCommentOwner);
            commentOfComment.setInteractiveEntity(comment);
            commentRepository.save(commentOfComment);

            assertEquals(2, commentRepository.findAll().size());

            Comment commentOfCommentFromDB = commentRepository.findAll().get(1);

            assertEquals("This is a comment of comment", commentOfCommentFromDB.getText());
            assertEquals("commentOnCommentOwner", commentOfCommentFromDB.getUser().getUsername());
            assertEquals("This is a comment", ((Comment)commentOfCommentFromDB.getInteractiveEntity()).getText());

        }
//
//        @Test
//        @Transactional
//        public void whenUpdate_Expect_Success() {
//            Comment comment = new Comment();
//            comment.setComment("This is a comment");
//            comment.setPost(postRepository.findById(1L).get());
//            commentRepository.save(comment);
//            Comment comment1 = commentRepository.findById(1L).get();
//            comment1.setComment("This is a new comment");
//            commentRepository.save(comment1);
//            assertEquals("This is a new comment", comment1.getComment());
//        }
//
//        @Test
//        @Transactional
//        public void whenDelete_Expect_Success() {
//            Comment comment = new Comment();
//            comment.setComment("This is a comment");
//            comment.setPost(postRepository.findById(1L).get());
//            commentRepository.save(comment);
//            List<Comment> commentList = commentRepository.findAll();
//            assertEquals(1, commentList.size());
//            commentRepository.deleteById(1L);
//            List<Comment> commentList1 = commentRepository.findAll();
//            assertEquals(0, commentList1.size());
//        }
    }
}