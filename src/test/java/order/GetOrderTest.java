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

public class GetOrderTest {

    private Order order;
    private OrderHelper orderHelper;
    private User user;
    private User userForLogin;
    private UserHelper userHelper;

    @Before
    public void setUp() {

        orderHelper = new OrderHelper();

        List<String> anyIngredients = orderHelper.getIngredients().subList(0, 2);
        order = new Order(anyIngredients);

        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().fullName());
        userForLogin = new User(user.getEmail(), user.getPassword());
        userHelper = new UserHelper();
    }

    @Test
    @DisplayName("Get order for auth user")
    public void getOrderAuthUser() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();
        String token = userHelper.getAccessToken();

        orderHelper.setOrder(order);
        orderHelper.createOrderWithToken(token);
        orderHelper.getUserOrders(token)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get order for unauth user")
    public void getOrderUnauthUser() {
        userHelper.setUser(user);
        userHelper.setUserLogin(userForLogin);
        userHelper.createUser();

        orderHelper.setOrder(order);
        orderHelper.createOrderWithoutToken();
        orderHelper.getOrdersWithoutToken()
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