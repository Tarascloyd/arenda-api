package com.taras.arenda;

import com.taras.arenda.jpa.entity.User;
import com.taras.arenda.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepo;

    @Test
    public void findByEmail_whenUserExist_returnsUser() {
        User user = createValidUser();
        testEntityManager.persist(user);

        User inDb = userRepo.findByEmail(user.getEmail()).orElse(null);
        assertThat(inDb).isNotNull();
    }

    @Test
    public void findByEmail_whenUserDoesNotExist_returnsNull() {
        User inDb = userRepo.findByEmail("aa@gmail.com").orElse(null);
        assertThat(inDb).isNull();
    }

    private User createValidUser() {
        User user = new User();
        user.setFirstName("Mark");
        user.setLastName("Mailer");
        user.setEmail("aa@gmail.com");
        user.setEncryptedPassword("ffff");
        return user;
    }
}
