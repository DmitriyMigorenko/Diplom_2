package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class LoginUserTest {

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
    @DisplayName("Login user happy path")
    public void loginUser() {
        userHelper.setUser(userForCreate);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();
        userHelper.loginUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Login user with wrong data")
    public void loginUserWithIncorrectData() {
        userHelper.setUser(userForCreate);
        userHelper.setUserLogin(new User("Login", "pass"));
        userHelper.createUser();
        userHelper.loginUser()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userHelper.deleteUser();
    }
}