package com.taras.arenda;

import com.taras.arenda.Service.CityService;
import com.taras.arenda.Service.UserService;
import com.taras.arenda.dto.CityDto;
import com.taras.arenda.exceptions.ApiError;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import com.taras.arenda.jpa.repository.UserRepository;
import com.taras.arenda.ui.model.city.CityResponseModel;
import com.taras.arenda.ui.model.city.CreateCityRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private CityService cityService;

    @BeforeEach
    public void cleanup() {
        userRepo.deleteAll();
        cityRepo.deleteAll();
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

    @Test
    public void postCity_whenNameIsLessThan2CharactersAndUserIsAuthorized_receiveBadRequest() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        city.setName("a");
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postCity_whenAboutIsLessThan16CharactersAndUserIsAuthorized_receiveBadRequest() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        city.setAbout("short message");
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postCity_whenAboutIs5000CharactersAndUserIsAuthorized_receiveCreated() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        String veryLongString = IntStream.rangeClosed(1,5000).mapToObj(i -> "x").collect(Collectors.joining());
        city.setAbout(veryLongString);
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postCity_whenAboutIs5001CharactersAndUserIsAuthorized_receiveBadRequest() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        String veryLongString = IntStream.rangeClosed(1,5001).mapToObj(i -> "x").collect(Collectors.joining());
        city.setAbout(veryLongString);
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<Object> response = postCity(request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postCity_whenNameIsNullAndUserIsAuthorized_receiveApiErrorWithValidationErrors() {
        userService.createUser(TestUtil.createValidUserDto());
        CreateCityRequestModel city = TestUtil.createValidCity();
        city.setName(null);
        HttpEntity<Object> request = testUtil.getAuthorizedRequest(city);

        ResponseEntity<ApiError> response = postCity(request, ApiError.class);
        assertThat(response.getBody().getErrors().get("name")).isNotNull();
    }

    @Test
    public void getCities_whenThereAreNoCities_receiveOk() {
        ResponseEntity<Object> response = getCities(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCities_whenThereAreNoCities_receivePageWithZeroItems() {
        ResponseEntity<TestPage<Object>> response = getCities(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getTotalElements()).isEqualTo(0);
    }

    @Test
    public void getCities_whenThereAreCities_receiveOk() {
        cityService.createCity(TestUtil.createValidCityDto("city1"));
        cityService.createCity(TestUtil.createValidCityDto("city2"));
        ResponseEntity<Object> response = getCities(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCities_whenThereAreCities_receivePageWithItems() {
        cityService.createCity(TestUtil.createValidCityDto("city1"));
        cityService.createCity(TestUtil.createValidCityDto("city2"));
        cityService.createCity(TestUtil.createValidCityDto("city3"));
        ResponseEntity<TestPage<Object>> response = getCities(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertThat(response.getBody().getTotalElements()).isEqualTo(3);
    }

    @Test
    public void getCities_whenThereAreCities_receivePageCityWithName() {
        CityDto cityDto = cityService.createCity(TestUtil.createValidCityDto());
        ResponseEntity<TestPage<CityResponseModel>> response = getCities(new ParameterizedTypeReference<TestPage<CityResponseModel>>() {});
        CityResponseModel city = response.getBody().getContent().get(0);
        assertThat(city.getName()).isEqualTo(cityDto.getName());
    }

    @Test
    public void getCities_whenThereAreCities_receivePageCityWithAbout() {
        CityDto cityDto = cityService.createCity(TestUtil.createValidCityDto());
        ResponseEntity<TestPage<CityResponseModel>> response = getCities(new ParameterizedTypeReference<TestPage<CityResponseModel>>() {});
        CityResponseModel city = response.getBody().getContent().get(0);
        assertThat(city.getAbout()).isEqualTo(cityDto.getAbout());
    }

    @Test
    public void getCities_whenThereAreCities_receivePageCityWithCityId() {
        CityDto cityDto = cityService.createCity(TestUtil.createValidCityDto());
        ResponseEntity<TestPage<CityResponseModel>> response = getCities(new ParameterizedTypeReference<TestPage<CityResponseModel>>() {});
        CityResponseModel city = response.getBody().getContent().get(0);
        assertThat(city.getCityId()).isEqualTo(cityDto.getCityId());
    }

    private <T> ResponseEntity<T> postCity(Object request, Class<T> response) {
        return testRestTemplate.postForEntity(CITIES_API_V1_URL, request, response);
    }

    private <T> ResponseEntity<T> getCities(ParameterizedTypeReference<T> responseType) {
        return testRestTemplate.exchange(CITIES_API_V1_URL, HttpMethod.GET, null, responseType);
    }
}
