package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;

public class UpdateUserTest {

    private User userForCreate;
    private User userForUpdate;
    private User userForLoginBeforeUpdate;
    private User userForLoginAfterUpdate;
    private UserHelper userHelper;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        userForCreate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForUpdate = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLoginBeforeUpdate = new User(userForCreate.getEmail(), userForCreate.getPassword());
        userForLoginAfterUpdate = new User(userForUpdate.getEmail(), userForUpdate.getPassword());
        userHelper = new UserHelper();
    }

    @Test
    @DisplayName("Update authorized user")
    public void updateAuthorizedUser() {
        userHelper.setUser(userForCreate);
        userHelper.createUser();
        userHelper.setUser(userForUpdate);
        userHelper.setUserLogin(userForLoginBeforeUpdate);
        String token = userHelper.getAccessToken();
        userHelper.updateUserWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Update unauthorized user")
    public void updateUnauthorizedUser() {
        userHelper.setUser(userForCreate);
        userHelper.createUser();
        userHelper.setUser(userForUpdate);
        userHelper.updateUserWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userHelper.setUserLogin(userForLoginAfterUpdate);
        userHelper.deleteUser();
    }
}