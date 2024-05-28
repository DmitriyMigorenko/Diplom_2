package order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Setter;
import org.junit.Before;

import static constants.Endpoints.*;
import static io.restassured.RestAssured.*;

@Setter
public class OrderHelper {

    private Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = API_ORDERS;
    }

    @Step("Create order with token")
    public Response createOrderWithToken(String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(order)
                .when().post();
    }

    @Step("Create order without token")
    public Response createOrderWithoutToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .with().post();
    }

    @Step("Get orders for user with token")
    public Response getUserOrders(String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get();
    }

    @Step("Get orders without token")
    public Response getOrdersWithoutToken() {
        return given()
                .contentType(ContentType.JSON)
                .when().get();
    }
}