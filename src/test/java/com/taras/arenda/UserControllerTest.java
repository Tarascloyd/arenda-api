package com.taras.arenda;

import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.CreateUserRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String USERS_API_V1_URL = "/api/v1/users";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void postUser_whenUserIsValid_receiveCreated() {
        CreateUserRequestModel user = createValidUser();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(USERS_API_V1_URL, user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private CreateUserRequestModel createValidUser() {
        CreateUserRequestModel user = new CreateUserRequestModel();
        user.setFirstName("Mark");
        user.setLastName("Mailer");
        user.setEmail("aa@gmail.com");
        user.setPassword("password99");
        return user;
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDb() {
        CreateUserRequestModel user = createValidUser();

        testRestTemplate.postForEntity(USERS_API_V1_URL, user, Object.class);

        assertThat(userRepo.count()).isEqualTo(1);
    }
}
