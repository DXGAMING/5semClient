package com.client.connection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerConnection {
    private final static ServerConnection instance = new ServerConnection();
    private final UserRequest request;
    private final ServerResponse response;
    private static Socket socket;
    private static final int PORT = 1280;
    private final Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private ServerConnection() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            request = UserRequest.getInstance();
            response = ServerResponse.getInstance();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
        logger.info("Client connected to socket");
    }

    public static ServerConnection getInstance() {
        return instance;
    }

    public UserRequest getRequest() {
        return request;
    }

    public ServerResponse getResponse() {
        return response;
    }

    public void sendRequest() {
        try {
            response.clear();
            Session session = Session.getInstance();
            request.setAllAttributes(session.getAttributes());
            Map<String, Object> request1 = new HashMap<>(request.getAttributes());
            out.flush();
            out.writeObject(request1);
            request.clear();
            response.setAllAttributes((Map<String, Object>) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            logger.error("error while sending request to server");
        }
    }
}
