package com.taras.arenda;

import com.taras.arenda.Service.UserService;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.exceptions.ApiError;
import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.user.LoginRequestModel;
import org.junit.jupiter.api.BeforeEach;
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
public class LoginTest {

    private static final String LOGIN_API_V1_URL = "/api/v1/users/login";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private TestUtil testUtil;

    @BeforeEach
    public void cleanup() {
        userRepo.deleteAll();
    }

    @Test
    public void postLogin_withoutUserCredentials_receiveUnauthorized() {
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withIncorrectCredentials_receiveUnauthorized() {
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withValidCredentials_receiveOk() {
        userService.createUser(TestUtil.createValidUserDto());
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postLogin_withValidCredentials_receiveNullBody() {
        userService.createUser(TestUtil.createValidUserDto());
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void postLogin_withValidCredentials_receiveHeaderToken() {
        userService.createUser(TestUtil.createValidUserDto());
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getHeaders().get("token").get(0)).isNotNull();
    }

    @Test
    public void postLogin_withValidCredentials_receiveUserIdToken() {
        UserDto savedUser = userService.createUser(TestUtil.createValidUserDto());
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getHeaders().get("userId").get(0)).isEqualTo(savedUser.getUserId());
    }

    @Test
    public void postLogin_withoutUserCredentials_receiveApiError() {
        ResponseEntity<ApiError> response = login(ApiError.class);
        assertThat(response.getBody().getStatus()).isNotNull();
    }

    @Test
    public void postLogin_withIncorrectCredentials_receiveApiError() {
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<ApiError> response = testUtil.postLogin(loginModel, ApiError.class);
        assertThat(response.getBody().getStatus()).isNotNull();
    }

    @Test
    public void postLogin_withoutUserCredentials_receiveErrorMessage() {
        ResponseEntity<ApiError> response = login(ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Error");
    }

    @Test
    public void postLogin_withIncorrectCredentials_receiveErrorMessage() {
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<ApiError> response = testUtil.postLogin(loginModel, ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Error");
    }

    @Test
    public void postLogin_withoutUserCredentials_receiveApiErrorWithoutValidationErrors() {
        ResponseEntity<String> response = login(String.class);
        assertThat(response.getBody().contains("errors")).isFalse();
    }

    @Test
    public void postLogin_withIncorrectCredentials_receiveUnauthorizedWithoutWWWAuthenticationHeader() {
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = testUtil.postLogin(loginModel, Object.class);
        assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
    }

    private <T> ResponseEntity<T> login(Class<T> responseType) {
        return testRestTemplate.postForEntity(LOGIN_API_V1_URL, null, responseType);
    }
}
