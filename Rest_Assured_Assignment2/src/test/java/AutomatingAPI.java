import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class AutomatingAPI {
    RequestSpecification rs;
    ResponseSpecification res;

    RequestSpecification rs1;
    ResponseSpecification res1;

    @BeforeClass
    public void setup(){
        RequestSpecBuilder rsp = new RequestSpecBuilder();
        rsp.setBaseUri("https://jsonplaceholder.typicode.com").addHeader("Content-Type","application/json");
        rs = RestAssured
                .with().spec(rsp.build());
        res = RestAssured.expect().contentType(ContentType.JSON);

        RequestSpecBuilder rsp1 = new RequestSpecBuilder();
        rsp1.setBaseUri("https://reqres.in/api").addHeader("Content-Type","application/json");
        rs1 = RestAssured
                .with().spec(rsp1.build());
        res1 = RestAssured.expect().contentType(ContentType.JSON);

    }

    @Test
    public void test_get_call(){
        Response response = given().spec(rs).when().get("/posts").then().statusCode(200).spec(res).extract().response();

        JSONArray arr = new JSONArray(response.asString());

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            assert obj.getInt("userId") != 40 || (obj.getInt("id") == 4);
            assert obj.get("title") != null;
            assert obj.get("title") instanceof String;
        }

    }

    @Test
    public void test_put_call(){
        File jsonData = new File("src//test//resources//postdata.json");
        given().spec(rs1).
                body(jsonData).
                when().
                put("/users").then().spec(res1).statusCode(200).
                body("name",equalTo("Arun")).body("job",equalTo("Manager"));
    }


}
