package com.taskflow.repository;
import com.taskflow.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;





@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUsername() {
        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("testuser@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should check existence by username")
    void testExistsByUsername() {
        boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isTrue();
        boolean notExists = userRepository.existsByUsername("nouser");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should check existence by email")
    void testExistsByEmail() {
        boolean exists = userRepository.existsByEmail("testuser@example.com");
        assertThat(exists).isTrue();
        boolean notExists = userRepository.existsByEmail("noemail@example.com");
        assertThat(notExists).isFalse();
    }
}