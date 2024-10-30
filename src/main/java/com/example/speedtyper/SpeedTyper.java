package com.example.speedtyper;

import java.io.IOException;
import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;

//this is just a proof of concept, helps us understand how we want the game to flow
public class SpeedTyper extends SpeedTyperServlet {
    public static void playSpeedTyper() throws IOException {
        // initliazing scanner to be able to read the user's input
        Scanner scanner = new Scanner(System.in);
        String sentence = getRandomSentence();

        //prints out instructions and what the user needs to type
        System.out.println("type this sentence as fast as possible");
        System.out.println(sentence);

        //using Instant to get the current time, then read's the user's input, then Instant again
        Instant start = Instant.now();
        String typed = scanner.nextLine();
        Instant end = Instant.now();
        //calculates how long it took for the user to complete the input

        //calculates how long it took for the user to complete the input
        Duration elapsed = Duration.between(start, end);
        //prints out how long it took for the user to type the target string
        System.out.println("Elapsed time: " + elapsed.getSeconds() + "s.");

        //prints out the accuracy of the user's string
        double accuracy = calculateAccuracy(sentence, typed);
        System.out.println("Your accuracy: "+ String.format("%.2f", accuracy) + "%");

        // number of correct characters from the accuracy
        int correctChars = (int) (typed.length() * (accuracy / 100));
        // Calculate the number of errors
        int errors = typed.length() - correctChars;

        double wpm = calculateWPM( typed.length(), errors, elapsed);
        System.out.println("Your WPM Speed: " + String.format("%.2f", wpm));

        scanner.close();
    }


    //function to calculate the accuracy of the user's text
    public static double calculateAccuracy(String sample, String typed){
        //initialzing a variable to hold the amount of correct letters
        int correctLetter = 0;
        //need to find the minimum length of the two strings so that the loop doesnt index out
        int minLength = Math.min(sample.length(), typed.length());

        //loops through every single character, and checks if the same char exists at both index
        //if the same char is found, incriments correctLetter
        for(int i = 0; i < minLength; i++){
            if(sample.charAt(i) == typed.charAt(i)){
                correctLetter++;
            }
        }

        //need to find maxLength as it is the divisor
        int maxLength = Math.max(sample.length(), typed.length());
        //returns their accuracy as a percentage of 100
        double result = ((double) correctLetter / maxLength) * 100;
        return result;
    }

    // function to calculate the users typing speed - WPM
    public static double calculateWPM(int totalChars, int errors, Duration elapsed){

        // calculates the amount of words in the string
        double wordCount = totalChars/ 5.0;
        //calculates the amount of minutes it took for the user to type the string
        double minutes = elapsed.getSeconds() / 60.0;
        //calculates the words per minute
        double wpm = wordCount / minutes;
        return wpm;
    }
}
