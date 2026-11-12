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

public class ScoreControllerRA {

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String adminToken;

    private Map<String, Object> postScoreInstance;

    @BeforeEach
    public void setUp() throws JSONException {
        baseURI = "http://localhost:8080";

        clientUsername = "joao@gmail.com";
        clientPassword = "123456";
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

        postScoreInstance = new HashMap<>();
        postScoreInstance.put("movieId", 5L);
        postScoreInstance.put("score", 4.0F);

    }
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
        postScoreInstance.put("movieId", 1000L);
        JSONObject newScore = new JSONObject(postScoreInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
        postScoreInstance.remove("movieId");
        JSONObject newScore = new JSONObject(postScoreInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
        postScoreInstance.put("score", -2.0F);
        JSONObject newScore = new JSONObject(postScoreInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newScore.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422);
	}
}
