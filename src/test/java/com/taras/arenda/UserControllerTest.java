package com.taras.arenda;

import com.taras.arenda.Service.UserService;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.exceptions.ApiError;
import com.taras.arenda.jpa.entity.User;
import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.user.CreateUserRequestModel;
import com.taras.arenda.ui.model.user.LoginRequestModel;
import com.taras.arenda.ui.model.user.UpdateUserRequestModel;
import com.taras.arenda.ui.model.user.UserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String USERS_API_V1_URL = "/api/v1/users";

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
    public void postUser_whenUserIsValid_receiveCreated() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDb() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        postSignup(user, Object.class);
        assertThat(userRepo.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDb() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        postSignup(user, Object.class);
        User savedUser = userRepo.findAll().get(0);
        assertThat(savedUser.getEncryptedPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullFirstName_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setFirstName(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullLastName_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setLastName(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullEmail_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setEmail(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasNullPassword_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword("123");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithMoreThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword("12345678901234567");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutUppercaseLetter_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword("password99");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutLowercaseLetter_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword("PASSWORD99");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordWithoutDigit_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setPassword("PasswordP");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasFirstNameWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setFirstName("F");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasLastNameWithLessThanRequired_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
        user.setLastName("L");
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasInvalidEmail_receiveBadRequest() {
        CreateUserRequestModel user = TestUtil.createValidUser();
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
        userService.createUser(TestUtil.createValidUserDto());

        CreateUserRequestModel user = TestUtil.createValidUser();
        ResponseEntity<Object> response = postSignup(user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenAnotherUserHasSameEmail_receiveMessageOfDuplicateEmail() {
        userService.createUser(TestUtil.createValidUserDto());

        CreateUserRequestModel user = TestUtil.createValidUser();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getErrors().get("email")).isEqualTo("This Email is in use");
    }

    @Test
    public void getUsers_whenThereAreNoUsersInDB_receiveOk() {
        ResponseEntity<Object> response = getUsers(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUsers_whenThereAreNoUsersInDB_receivePageWithZeroItems() {
        ResponseEntity<TestPage<Object>> response = getUsers(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getTotalElements()).isEqualTo(0);
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receivePageWithUser() {
        userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Object>> response = getUsers(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getNumberOfElements()).isEqualTo(1);
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receiveUserWithoutPassword() {
        userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Map<String,Object>>> response = getUsers(new ParameterizedTypeReference<TestPage<Map<String,Object>>>() {});
        Map<String,Object> entity = response.getBody().getContent().get(0);
        assertThat(entity.containsKey("password")).isFalse();
        assertThat(entity.containsKey("encryptedPassword")).isFalse();
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receiveUserWithFirstName() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Map<String,Object>>> response = getUsers(new ParameterizedTypeReference<TestPage<Map<String,Object>>>() {});
        Map<String,Object> entity = response.getBody().getContent().get(0);
        assertThat(entity.containsKey("firstName")).isTrue();
        assertThat(entity.get("firstName")).isEqualTo(user.getFirstName());
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receiveUserWithLastName() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Map<String,Object>>> response = getUsers(new ParameterizedTypeReference<TestPage<Map<String,Object>>>() {});
        Map<String,Object> entity = response.getBody().getContent().get(0);
        assertThat(entity.containsKey("lastName")).isTrue();
        assertThat(entity.get("lastName")).isEqualTo(user.getLastName());
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receiveUserWithoutEmail() {
        userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Map<String,Object>>> response = getUsers(new ParameterizedTypeReference<TestPage<Map<String,Object>>>() {});
        Map<String,Object> entity = response.getBody().getContent().get(0);
        assertThat(entity.containsKey("email")).isFalse();
    }

    @Test
    public void getUsers_whenThereIsUserInDB_receiveUserWithUserId() {
        userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<TestPage<Map<String,Object>>> response = getUsers(new ParameterizedTypeReference<TestPage<Map<String,Object>>>() {});
        Map<String,Object> entity = response.getBody().getContent().get(0);
        assertThat(entity.containsKey("userId")).isTrue();
    }

    @Test
    public void getUsers_whenPageIsRequestedFor3ItemsPerPageWhereTheDBHas20Users_receive3Users() {
        IntStream.rangeClosed(1,20).mapToObj(i -> "aa" + i +"@gmail.com")
                .map(TestUtil::createValidUserDto)
                .forEach(userService::createUser);
        String path = USERS_API_V1_URL + "?page=0&size=3";
        ResponseEntity<TestPage<Object>> response = getUsers(path, new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getContent().size()).isEqualTo(3);
    }

    @Test
    public void getUsers_whenPageSizeNotProvided_receivePageSizeAs10() {
        ResponseEntity<TestPage<Object>> response = getUsers(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getSize()).isEqualTo(10);
    }

    @Test
    public void getUsers_whenPageSizeIsGreaterThan100_receivePageSizeAs100() {
        String path = USERS_API_V1_URL + "?size=150";
        ResponseEntity<TestPage<Object>> response = getUsers(path, new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getSize()).isEqualTo(100);
    }

    @Test
    public void getUsers_whenPageSizeIsNegative_receivePageSizeAs10() {
        String path = USERS_API_V1_URL + "?size=-25";
        ResponseEntity<TestPage<Object>> response = getUsers(path, new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getSize()).isEqualTo(10);
    }

    @Test
    public void getUsers_whenPageIsNegative_receiveFirstPage() {
        String path = USERS_API_V1_URL + "?page=-2";
        ResponseEntity<TestPage<Object>> response = getUsers(path, new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getNumber()).isEqualTo(0);
    }

    @Test
    public void getUser_whenUserExist_receiveOk() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<Object> response = getUser(user.getUserId(), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUser_whenUserExist_receiveUserWithoutPassword() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<String> response = getUser(user.getUserId(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contains("password")).isFalse();
        assertThat(response.getBody().contains("encryptedPassword")).isFalse();
    }

    @Test
    public void getUser_whenUserExist_receiveUserWithoutEmail() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<String> response = getUser(user.getUserId(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contains("email")).isFalse();
    }

    @Test
    public void getUser_whenUserExist_receiveUserWithFirstName() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<Map<String,Object>> response = getUser(user.getUserId(),new ParameterizedTypeReference<Map<String,Object>>() {});
        Map<String,Object> entity = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.containsKey("firstName")).isTrue();
        assertThat(entity.get("firstName")).isEqualTo(user.getFirstName());
    }

    @Test
    public void getUser_whenUserNotExist_receiveNotFound() {
        ResponseEntity<Object> response = getUser("random_id", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUser_whenUserNotExist_receiveApiError() {
        ResponseEntity<ApiError> response = getUser("random_id", ApiError.class);
        assertThat(response.getBody().getMessage().contains("Resource Not Found")).isTrue();
    }

    @Test
    public void getUserByEmail_whenUnauthorizedUserSendsRequest_receiveUnauthorized() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<Object> response = getUserByEmail(user.getEmail(), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getUserByEmail_whenAuthorizedUserSendsRequestForAnotherUser_receiveForbidden() {
        userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<Object> response = getUserByEmail("/unknown@gg.com", request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getUserByEmail_whenUnauthorizedUserSendsRequest_receiveApiError() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        ResponseEntity<ApiError> response = getUserByEmail(user.getEmail(), ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Error");
    }

    @Test
    public void getUserByEmail_whenAuthorizedUserSendsRequestForAnotherUser_receiveApiError() {
        userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<ApiError> response = getUserByEmail("/unknown@gg.com", request, ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Denied");
    }

    @Test
    public void getUserByEmail_whenAuthorizedUserSendsRequestForSelf_receiveOk() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<Object> response = getUserByEmail(user.getEmail(), request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserByEmail_whenAuthorizedUserSendsRequestForSelf_receiveUserWithoutPassword() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<String> response = getUserByEmail(user.getEmail(), request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contains("password")).isFalse();
        assertThat(response.getBody().contains("encryptedPassword")).isFalse();
    }

    @Test
    public void getUserByEmail_whenAuthorizedUserSendsRequestForSelf_receiveUserWithEmail() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<Map<String,Object>> response = getUserByEmail(user.getEmail(), request, new ParameterizedTypeReference<Map<String,Object>>() {});
        Map<String,Object> entity = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.containsKey("email")).isTrue();
        assertThat(entity.get("email")).isEqualTo(user.getEmail());
    }

    @Test
    public void patchUser_whenUnauthorizedUserSendsRequest_receiveUnauthorized() {
        String id = "/random_id";
        ResponseEntity<Object> response = patchUser(id, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void patchUser_whenAuthorizedUserSendsUpdateForAnotherUser_receiveForbidden() {
        userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<Object> response = patchUser("/random_id", request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void patchUser_whenUnauthorizedUserSendsRequest_receiveApiError() {
        String id = "/random_id";
        ResponseEntity<ApiError> response = patchUser(id, null, ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Error");
    }

    @Test
    public void patchUser_whenAuthorizedUserSendsUpdateForAnotherUser_receiveApiError() {
        userService.createUser(TestUtil.createValidUserDto());
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(null);
        ResponseEntity<ApiError> response = patchUser("/random_id", request, ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Access Denied");
    }

    @Test
    public void patchUser_whenValidRequestBodyFromAuthorizedUser_receiveOk() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        ResponseEntity<Object> response = patchUser(user.getUserId(), request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void patchUser_whenValidRequestBodyFromAuthorizedUser_lastNameUpdated() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        patchUser(user.getUserId(), request, Object.class);
        User userInDB = userRepo.findByEmail(user.getEmail()).orElse(null);
        assertThat(userInDB.getLastName()).isEqualTo(updatedUser.getLastName());
    }

    @Test
    public void patchUser_whenValidRequestBodyFromAuthorizedUser_firstNameUpdated() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        patchUser(user.getUserId(), request, Object.class);
        User userInDB = userRepo.findByEmail(user.getEmail()).orElse(null);
        assertThat(userInDB.getFirstName()).isEqualTo(updatedUser.getFirstName());
    }

    @Test
    public void patchUser_whenValidRequestBodyFromAuthorizedUser_receiveUserWithFirstNameUpdated() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        ResponseEntity<UserResponseModel> response = patchUser(user.getUserId(), request, UserResponseModel.class);
        assertThat(response.getBody().getFirstName()).isEqualTo(updatedUser.getFirstName());
    }

    @Test
    public void patchUser_whenValidRequestBodyFromAuthorizedUser_receiveUserWithLastNameUpdated() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        ResponseEntity<UserResponseModel> response = patchUser(user.getUserId(), request, UserResponseModel.class);
        assertThat(response.getBody().getLastName()).isEqualTo(updatedUser.getLastName());
    }

    @Test
    public void patchUser_whenInvalidRequestBodyLastIsNullFromAuthorizedUser_receiveBadRequest() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        updatedUser.setLastName(null);
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        ResponseEntity<Object> response = patchUser(user.getUserId(), request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void patchUser_whenInvalidRequestBodyLastNameIsLessThanRequiredFromAuthorizedUser_receiveBadRequest() {
        UserDto user = userService.createUser(TestUtil.createValidUserDto());
        UpdateUserRequestModel updatedUser = createValidUpdateUserRM();
        updatedUser.setLastName("a");
        HttpEntity<UpdateUserRequestModel> request = testUtil.getAuthorizedRequest(updatedUser);
        ResponseEntity<Object> response = patchUser(user.getUserId(), request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    private UpdateUserRequestModel createValidUpdateUserRM() {
        UpdateUserRequestModel updatedUser = new UpdateUserRequestModel();
        updatedUser.setFirstName("Marcus");
        updatedUser.setLastName("Black");
        return updatedUser;
    }

    private <T> ResponseEntity<T> postSignup(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(USERS_API_V1_URL, request, response);
    }

    private <T> ResponseEntity<T> getUsers(ParameterizedTypeReference<T> responseType) {
        return testRestTemplate.exchange(USERS_API_V1_URL, HttpMethod.GET, null, responseType);
    }

    private <T> ResponseEntity<T> getUsers(String path, ParameterizedTypeReference<T> responseType) {
        return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
    }

    private <T> ResponseEntity<T> getUser(String userId, Class<T> responseType) {
        String path = USERS_API_V1_URL + "/" + userId;
        return testRestTemplate.getForEntity(path, responseType);
    }

    private <T> ResponseEntity<T> getUser(String userId, ParameterizedTypeReference<T> responseType) {
        String path = USERS_API_V1_URL + "/" + userId;
        return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
    }

    private <T> ResponseEntity<T> getUserByEmail(String email, Class<T> responseType) {
        String path = USERS_API_V1_URL + "/email/" + email;
        return testRestTemplate.getForEntity(path, responseType);
    }

    private <T> ResponseEntity<T> getUserByEmail(String email, HttpEntity<?> requestEntity, Class<T> responseType) {
        String path = USERS_API_V1_URL + "/email/" + email;
        return testRestTemplate.exchange(path, HttpMethod.GET, requestEntity, responseType);
    }

    private <T> ResponseEntity<T> getUserByEmail(String email, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        String path = USERS_API_V1_URL + "/email/" + email;
        return testRestTemplate.exchange(path, HttpMethod.GET, requestEntity, responseType);
    }

    private <T> ResponseEntity<T> patchUser(String id, HttpEntity<?> requestEntity, Class<T> responseType) {
        String path = USERS_API_V1_URL + "/" + id;
        return testRestTemplate.exchange(path, HttpMethod.PATCH, requestEntity, responseType);
    }
}
