package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest {

    private User userForCreate;
    private User userForLogin;
    private UserHelper userHelper;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userForCreate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLogin = new User(userForCreate.getEmail(), userForCreate.getPassword());
        userHelper = new UserHelper();
    }

    @Test
    @DisplayName("Create user happy path")
    public void createUser() {
        userHelper.setUser(userForCreate);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create duplicate user")
    public void createTheSameUser() {
        userHelper.setUser(userForCreate);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();
        userHelper.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user with empty body request")
    public void createInvalidUser() {
        userHelper.setUser(new User());
        userHelper.setUserLogin(new User());
        userHelper.createUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        userHelper.deleteUser();
    }
}