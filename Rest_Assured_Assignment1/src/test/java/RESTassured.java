import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class RESTassured {

    @BeforeTest
    void init() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void test_get_call() {

        Response response = given().
                                baseUri("https://jsonplaceholder.typicode.com").
                            when().
                                get("/posts").
                            then().extract().response();

        assert (response.getStatusCode() == 200);
        assert (response.getContentType().contains("json"));

        JSONArray arr = new JSONArray(response.asString());

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            assert obj.getInt("userId") != 40 || (obj.getInt("id") == 4);
            assert obj.get("title") != null;
            assert obj.get("title") instanceof String;
        }
    }

    @Test
    void test_put_cell() {
        File jsonData = new File("src//test//resources//postdata.json");
        Response response = given().
                                baseUri("https://reqres.in/api").
                                body(jsonData).
                                header("Content-Type", "application/json").
                            when().
                                put("/users").then().extract().response();

        assert response.statusCode() == 200;
        JSONObject obj = new JSONObject(response.asString());
        assert obj.get("name").equals("Arun") && obj.get("job").equals("Manager");
    }
}