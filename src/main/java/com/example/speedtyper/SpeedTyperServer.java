package com.example.speedtyper;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/ws/{roomID}")
public class SpeedTyperServer {

    // contains a static List of ChatRoom used to control the existing rooms and their users
//    private static ArrayList<ChatRoom> listOfChatRooms = new ArrayList<ChatRoom>();
}
