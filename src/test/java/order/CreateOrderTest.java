package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserHelper;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {

    private Order order;
    private Order emptyOrder;
    private Order invalidOrder;
    private OrderHelper orderHelper;
    private User user;
    private User userForLogin;
    private UserHelper userHelper;

    @Before
    public void setUp() {

        orderHelper = new OrderHelper();

        List<String> anyIngredients = orderHelper.getIngredients().subList(0, 2);
        order = new Order(anyIngredients);

        emptyOrder = new Order();

        List<String> invalidIngredients = List.of("11c0c5a71d1f82001bd1aa6d");
        invalidOrder = new Order(invalidIngredients);


        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLogin = new User(user.getEmail(), user.getPassword());
        userHelper = new UserHelper();
    }

    @Test
    @DisplayName("Create order with ingredients and token")
    public void createOrder() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();
        String token = userHelper.getAccessToken();

        orderHelper.setOrder(order);
        orderHelper.createOrderWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order with ingredients and without token")
    public void createOrderWithoutToken() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();

        orderHelper.setOrder(order);
        orderHelper.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order without ingredients and with token")
    public void createEmptyOrder() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();
        String token = userHelper.getAccessToken();

        orderHelper.setOrder(emptyOrder);
        orderHelper.createOrderWithToken(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order without ingredients and token")
    public void createEmptyOrderAndWithoutToken() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();

        orderHelper.setOrder(emptyOrder);
        orderHelper.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order with incorrect ingredient")
    public void createInvalidOrder() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();

        orderHelper.setOrder(invalidOrder);
        orderHelper.createOrderWithoutToken()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userHelper.deleteUser();
    }
}