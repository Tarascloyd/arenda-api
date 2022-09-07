package com.taras.arenda;

import com.taras.arenda.dto.CityDto;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.ui.model.city.CreateCityRequestModel;
import com.taras.arenda.ui.model.user.CreateUserRequestModel;
import com.taras.arenda.ui.model.user.LoginRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {

    private static final String LOGIN_API_V1_URL = "/api/v1/users/login";
    private static final String AUTHORIZATION_TOKEN_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_TOKEN_HEADER_PREFIX = "Bearer";

    @Autowired
    private TestRestTemplate testRestTemplate;

    public static UserDto createValidUserDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(createValidUser(), UserDto.class);
    }

    public static CreateUserRequestModel createValidUser() {
        CreateUserRequestModel user = new CreateUserRequestModel();
        user.setFirstName("Mark");
        user.setLastName("Mailer");
        user.setEmail("aa@gmail.com");
        user.setPassword("Password99");
        return user;
    }

    public static LoginRequestModel createLoginRequestModel() {
        LoginRequestModel user = new LoginRequestModel();
        user.setEmail("aa@gmail.com");
        user.setPassword("Password99");
        return user;
    }

    public static UserDto createValidUserDto(String email) {
        CreateUserRequestModel user = createValidUser();
        user.setEmail(email);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
    }

    public static CreateCityRequestModel createValidCity() {
        CreateCityRequestModel city = new CreateCityRequestModel();
        city.setName("City");
        city.setAbout("About message about our city");
        return city;
    }

    public static CityDto createValidCityDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(createValidCity(), CityDto.class);
    }

    public static CityDto createValidCityDto(String name) {
        ModelMapper modelMapper = new ModelMapper();
        CityDto cityDto = modelMapper.map(createValidCity(), CityDto.class);
        cityDto.setName(name);
        return cityDto;
    }

    public <T> HttpEntity<T> getAuthorizedRequest(T body) {
        String token = authenticateUser();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_TOKEN_HEADER_NAME, AUTHORIZATION_TOKEN_HEADER_PREFIX + token);
        return new HttpEntity<>(body, headers);
    }

    private String authenticateUser() {
        LoginRequestModel loginModel = TestUtil.createLoginRequestModel();
        ResponseEntity<Object> response = postLogin(loginModel, Object.class);
        return response.getHeaders().get("token").get(0);
    }

    public <T> ResponseEntity<T> postLogin(Object request, Class<T> responseType) {
        return testRestTemplate.postForEntity(LOGIN_API_V1_URL, request, responseType);
    }
}
