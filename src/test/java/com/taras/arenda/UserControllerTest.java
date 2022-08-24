package com.taras.arenda;

import com.taras.arenda.dto.UserDto;
import com.taras.arenda.exceptions.ApiError;
import com.taras.arenda.jpa.entity.User;
import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.CreateUserRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String USERS_API_V1_URL = "/api/v1/users";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void cleanup() {
        userRepo.deleteAll();
    }

    @Test
    public void postUser_whenUserIsValid_receiveCreated() {
        CreateUserRequestModel user = createValidUser();
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDb() {
        CreateUserRequestModel user = createValidUser();
        postSignup(user, Object.class);
        assertThat(userRepo.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDb() {
        CreateUserRequestModel user = createValidUser();
        postSignup(user, Object.class);
        User savedUser = userRepo.findAll().get(0);
        assertThat(savedUser.getEncryptedPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullFirstName_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setFirstName(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullLastName_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setLastName(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullEmail_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setEmail(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullPassword_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword("123");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithMoreThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword("12345678901234567");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutUppercaseLetter_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword("password99");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutLowercaseLetter_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword("PASSWORD99");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutDigit_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setPassword("PasswordP");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasFirstNameWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setFirstName("F");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasLastNameWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setLastName("L");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasInvalidEmail_receiveBadRequest() {
        CreateUserRequestModel user = createValidUser();
        user.setEmail("email.com");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserIsInvalid_receiveApiErrorWithValidationErrors() {
        CreateUserRequestModel user = new CreateUserRequestModel();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getErrors().size()).isEqualTo(4);
    }

    @Test
    public void postUser_whenAnotherUserHasSameEmail_receiveBadRequest() {
        userRepo.save(createValidUserEntity());

        CreateUserRequestModel user = createValidUser();
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenAnotherUserHasSameEmail_receiveMessageOfDuplicateEmail() {
        userRepo.save(createValidUserEntity());

        CreateUserRequestModel user = createValidUser();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getErrors().get("email")).isEqualTo("This Email is in use");
    }


    private <T> ResponseEntity<T> postSignup(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(USERS_API_V1_URL, request, response);
    }
    private CreateUserRequestModel createValidUser() {
        CreateUserRequestModel user = new CreateUserRequestModel();
        user.setFirstName("Mark");
        user.setLastName("Mailer");
        user.setEmail("aa@gmail.com");
        user.setPassword("Password99");
        return user;
    }

    private User createValidUserEntity() {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(createValidUser(), UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword("ffff");
        return modelMapper.map(userDto, User.class);
    }
}
