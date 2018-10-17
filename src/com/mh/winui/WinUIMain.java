package com.mh.winui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class WinUIMain extends Application {

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(new URL(decode(getClass().getResource("/com/mh/winui/ui_exdict.fxml").toString())));
        ExDictController.setStage(primaryStage);
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("ExDict");
        primaryStage.getIcons().add(new Image(decode(getClass().getResource("/com/mh/winui/icon_exdict.png").toString())));
        primaryStage.show();
    }

    private static String decode(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return encoded;
        }
    }
}
