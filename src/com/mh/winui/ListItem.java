package com.mh.winui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class ListItem extends HBox {

    //private Label lblText;
    private TextField tfdText;

    public ListItem(String text, Button btnDelete, Button btnEdit) {
        super();
        initComponents(text, btnDelete, btnEdit);
    }

    private void initComponents(String text, Button btnDelete, Button btnEdit) {
        //lblText = new Label(text);
        tfdText = new TextField(text);
        tfdText.setEditable(false);
        tfdText.setBackground(null);

        HBox.setHgrow(tfdText, Priority.ALWAYS);

        btnDelete.setMinWidth(USE_PREF_SIZE);
        btnEdit.setMinWidth(USE_PREF_SIZE);

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(tfdText, btnDelete, btnEdit);
    }


    public void setText(String text) {
        //lblText.setText(text);
        tfdText.setText(text);
    }


}
