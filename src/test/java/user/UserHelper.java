package user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.Setter;
import org.junit.Before;


import java.net.HttpURLConnection;

import static constants.Endpoints.*;
import static io.restassured.RestAssured.*;


@Setter
public class UserHelper {

    private User user;
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = API_AUTH;
    }

    @Step("Create user")
    public Response createUser() {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/register");
    }

    @Step("Login user")
    public Response loginUser() {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/login");
    }

    @Step("Get accessToken")
    public Response getAccessToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/login")
                .then()
                .extract().path("accessToken");
    }

    @Step("Update user with token")
    public Response updateUserWithToken() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(user)
                .when().patch("/user");
    }

    @Step("Update user without token")
    public Response updateUserWithoutToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().patch("/user");
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/user")
                .then()
                .assertThat().statusCode(HttpURLConnection.HTTP_ACCEPTED);
    }
}