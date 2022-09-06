package com.taras.arenda;

import com.taras.arenda.Service.UserService;
import com.taras.arenda.exceptions.ApiError;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.city.CreateCityRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CityControllerTest {

    private static final String CITIES_API_V1_URL = "/api/v1/cities";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private RoomTypeRepository roomTypeRepo;

    @BeforeEach
    public void cleanup() {
        userRepo.deleteAll();
        roomTypeRepo.deleteAll();
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsUnauthorized_receiveUnauthorized() {
        CreateCityRequestModel city = TestUtil.createValidCity();
        ResponseEntity<Object> response = postCity(city, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsUnauthorized_receiveApiError() {
        CreateCityRequestModel city = TestUtil.createValidCity();
        ResponseEntity<ApiError> response = postCity(city, ApiError.class);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsAuthorized_receiveCreated() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsAuthorized_CitySavedToDB() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        postCity(request, Object.class);
        assertThat(cityRepo.count()).isEqualTo(1);
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsAuthorized_CitySavedToDBWithNameEqual() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        postCity(request, Object.class);

        City inDB = cityRepo.findAll().get(0);
        assertThat(inDB.getName()).isEqualTo(city.getName());
    }

    @Test
    public void postCity_whenCityIsValidAndUserIsAuthorized_CitySavedToDBWithAboutEqual() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        postCity(request, Object.class);

        City inDB = cityRepo.findAll().get(0);
        assertThat(inDB.getAbout()).isEqualTo(city.getAbout());
    }

    @Test
    public void postCity_whenNameIsNullAndUserIsAuthorized_receiveBadRequest() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        city.setName(null);
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postCity_whenAboutIsNullAndUserIsAuthorized_receiveCreated() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        city.setAbout(null);
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private <T> ResponseEntity<T> postCity(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(CITIES_API_V1_URL, request, response);
    }
}
