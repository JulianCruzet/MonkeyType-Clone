package com.example.speedtyper;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;



/**
 * This is a class that has services
 * In our case, we are using this to generate unique room IDs
 **/
@WebServlet(name = "speedtyper", value = {"/speedtyper-servlet", "/room-list", "/sentence-getter", "/calculate-accuracy"})
public class SpeedTyperServlet extends HttpServlet {

    //static so this set is unique
    public static Set<String> rooms = new HashSet<>();

    /**
     * Method generates unique room codes
     **/
    public String generatingRandomUpperAlphanumericString(int length) {
        String generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        // generating unique room code
        while (rooms.contains(generatedString)) {
            generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }
        rooms.add(generatedString);

        return generatedString;
    }

    // added endpoint to retrieve the list of rooms
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain");

        if ("/speedtyper-servlet".equals(request.getServletPath())) {
            // send the random code as the response's content
            PrintWriter out = response.getWriter();
            out.println(generatingRandomUpperAlphanumericString(5));
        } else if ("/room-list".equals(request.getServletPath())) { // if /room-list endpoint is chosen
            // send a json response with the list of rooms
            response.setContentType("application/json");
            JSONObject roomsObject = new JSONObject();
            for (String room : rooms)
                roomsObject.put(room, "");
            PrintWriter out = response.getWriter();
            out.println(roomsObject);
        }
        //sentence getter
        else if ("/sentence-getter".equals(request.getServletPath())) {
            response.setContentType("application/json");
            JSONObject sentencesObject = new JSONObject();
            try {
                String randomSentence = getRandomSentence();
                sentencesObject.put("sentence", randomSentence);
            } catch (IOException e) {
                //error handling
                sentencesObject.put("error", "cannot read sentences.json");
            }

            PrintWriter out = response.getWriter();
            //sends the jsonobject out as a response
            out.println(sentencesObject.toString());
        }
    }

    public static String getRandomSentence() throws IOException {
        //get the sentence using classLoader
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sentences.json");
        if (inputStream == null) {
            //error handling
            throw new FileNotFoundException("Resource 'sentences.json' is not found");
        }

        //reads and converts the inputstream into a string
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        //parsing data to get the sentences
        JSONObject jsonObj = new JSONObject(content);
        JSONArray sentences = jsonObj.getJSONArray("sentences");

        //using random function we generate a random index
        //then return the sentence at that index
        Random random = new Random();
        int index = random.nextInt(sentences.length());
        return sentences.getString(index);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/calculate-accuracy".equals(request.getServletPath())) {
            JSONObject requestBody = new JSONObject(new JSONTokener(request.getInputStream()));
            String typedSentence = requestBody.getString("typedSentence");
            String originalSentence = requestBody.getString("originalSentence");

            int accuracy = calculateAccuracy(originalSentence, typedSentence);

            JSONObject responseObject = new JSONObject();
            responseObject.put("accuracy", accuracy);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println(responseObject.toString());
        }
    }

    private int calculateAccuracy(String original, String typed){
        String[] originalWords = original.split("\\s+");
        String[] typedWords = typed.split("\\s+");
        int correct = 0;
        for (int i = 0; i < Math.min(originalWords.length, typedWords.length); i++) {
            if (originalWords[i].equals(typedWords[i])) {
                correct++;
            }
        }
        return (int) ((double) correct / originalWords.length * 100);
    }

    public void destroy() {
    }
}

