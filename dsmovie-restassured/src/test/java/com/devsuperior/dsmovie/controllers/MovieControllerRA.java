package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

    private Long existingId, nonExistingId;
    private String movieTitle;
    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String clientToken, adminToken, invalidToken;

    private Map<String, Object> postMovieInstance;

    @BeforeEach
    public void setUp() throws JSONException {
        baseURI = "http://localhost:8080";
        movieTitle = "Venom";
        existingId = 5L;
        nonExistingId = 100L;

        clientUsername = "joao@gmail.com";
        clientPassword = "123456";
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";

        postMovieInstance = new HashMap<>();
        postMovieInstance.put("title", "Test Movie");
        postMovieInstance.put("score", 0.0F);
        postMovieInstance.put("count", 0);
        postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
    }

    @Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
                .get("/movies")
                .then()
                .statusCode(200);
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        given()
                .get("/movies?title={movieTitle}", movieTitle)
                .then()
                .statusCode(200)
                .body("content[0].id", is(2))
                .body("content[0].count", is(3))
                .body("content[0].image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"))
                .body("content[0].score", is(3.3F))
                .body("content[0].title", equalTo("Venom: Tempo de Carnificina"));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
        given()
                .get("/movies/{existingId}", existingId)
                .then()
                .statusCode(200)
                .body("id", is(5))
                .body("count", is(0))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/cinER0ESG0eJ49kXlExM0MEWGxW.jpg"))
                .body("score", is(0.0F))
                .body("title", equalTo("Shang-Chi e a Lenda dos Dez Anéis"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
        given()
                .get("/movies/{nonExistingId}", nonExistingId)
                .then()
                .statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
        postMovieInstance.put("title", "  ");
        JSONObject newMovie = new JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newMovie.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(422);
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
        JSONObject newMovie = new JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newMovie.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        JSONObject newMovie = new JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .body(newMovie.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(401);
	}
}
