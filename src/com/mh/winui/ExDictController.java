package com.mh.winui;

import com.mh.exdict.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ExDictController implements Initializable {

    private static File file = new File("sample/hp.json");

    private FileChooser loadChooser;
    private FileChooser saveChooser;

    private static Stage stage;

    @FXML
    private TextField tfdSearch;

    @FXML
    private Label lblVocInfo;

    @FXML
    private ScrollPane sclSearchResult;

    @FXML
    private ScrollPane sclWords;

    @FXML
    private VBox vbxSearchResult;

    @FXML
    private VBox vbxWords;

    @FXML
    private VBox vbxDescs;

    @FXML
    private TextField tfdWordText;

    @FXML
    private TextField tfdDescPart;

    @FXML
    private ChoiceBox<String> chbDescPart;

    @FXML
    private TextArea tarDescContent;

    @FXML
    private TextArea tarInfo;

    @FXML
    private TextField tfdDictName;

    @FXML
    private TextArea tarDictIntro;

    @FXML
    private VBox vbxLog;


    private Dict editingDict;
    private Word editingWord;
    private Desc editingDesc;


    private Map<Word, HBox> wordItemMap = new HashMap<>();
    private Map<Desc, HBox> descItemMap = new HashMap<>();



    public static final String[] BUINDIN_PARTS = new String[] {
            "exp", "n", "v", "adj", "adv", "prep", "conj"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chbDescPart.setItems(FXCollections.observableArrayList(BUINDIN_PARTS));
        chbDescPart.setOnAction(event -> tfdDescPart.setText(chbDescPart.getValue()));
        editDict(new Dict());
    }


    @FXML
    public void onBtnClear(ActionEvent e) {
        editingDict = new Dict();
        refreshDictEditingArea();
    }

    @FXML
    public void onBtnCreateWord(ActionEvent e) {
        Word word = new Word();
        word.setText(String.valueOf(Math.floor(Math.random() * 100)));
        addWordToDict(word, editingDict);
        editWord(word);
    }

    @FXML
    public void onBtnCreateDesc(ActionEvent e) {
        Desc desc = new Desc();
        desc.setContent(String.valueOf(Math.floor(Math.random() * 100)));
        addDescToWord(desc, editingWord);
        editDesc(desc);
    }

    @FXML
    public void onBtnConfirmWord(ActionEvent e) {
        confirmWord();
    }

    @FXML
    public void onBtnAbandonWord(ActionEvent e) {
        refreshWordEditingArea();
    }

    @FXML
    public void onBtnConfirmDesc(ActionEvent e) {
        confirmDesc();
    }

    @FXML
    public void onBtnAbandonDesc(ActionEvent e) {
        refreshDescEditingArea();
    }

    private FileChooser fileChooser = new FileChooser();

    @FXML
    public void onBtnLoad(ActionEvent e) {
        fileChooser.setTitle("载入");
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent.exists()) {
            fileChooser.setInitialDirectory(parent);
        }
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        this.file = file;
        try (InputStream in = new FileInputStream(file)) {
            Dict dict = PersistUtil.load(in);
            if (dict != null) {
                editDict(dict);
                log("Succeeded loading Dictionary ["+dict.getName()+"] from file ["+file.getAbsolutePath()+"]", Color.GREEN);
            } else {
                log("Failed loading Dictionary ["+dict.getName()+"] from file ["+file.getAbsolutePath()+"]", Color.RED);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void onBtnSave(ActionEvent e) throws IOException {
        if (editingDict != null) {
            log("Cannot save a null Dictionary");
            try (OutputStream out = new FileOutputStream(file)) {
                PersistUtil.save(editingDict, out);
                log("Succeeded saving Dictionary ["+editingDict.getName()+"] to file ["+file.getAbsolutePath()+"]", Color.GREEN);
            } catch (IOException ex) {
                log("Failed saving Dictionary ["+editingDict.getName()+"] to file ["+file.getAbsolutePath()+"]", Color.RED);
                ex.printStackTrace();
            }
        }


    }

    @FXML
    public void onBtnSaveAs(ActionEvent e) throws IOException {
        fileChooser.setTitle("另存为");
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent.exists()) {
            fileChooser.setInitialDirectory(parent);
        }
        fileChooser.setInitialFileName(file.getName());
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        this.file = file;
        if (editingDict != null) {
            log("Cannot save a null Dictionary");
            try (OutputStream out = new FileOutputStream(file)) {
                PersistUtil.save(editingDict, out);
                log("Succeeded saving Dictionary ["+editingDict.getName()+"] to file ["+file.getAbsolutePath()+"]", Color.GREEN);
            } catch (IOException ex) {
                log("Failed saving Dictionary ["+editingDict.getName()+"] to file ["+file.getAbsolutePath()+"]", Color.RED);
                ex.printStackTrace();
            }
        }


    }

    @FXML
    public void onBtnSearch(ActionEvent e) {
    }

    @FXML
    public void onBtnSearchCancel(ActionEvent e) {
    }

    @FXML
    public void onBtnConfirmDictInfo(ActionEvent e) {
        confirmDict();
    }

    @FXML
    public void onBtnCancelDictInfo(ActionEvent e) {
        refreshDictEditingArea();
    }




    private HBox createWordItem(Word word) {
        HBox hbxWord = new HBox();

        hbxWord.setAlignment(Pos.CENTER_LEFT);
        hbxWord.setSpacing(10);

        Label lblText = new Label(word.toString());
        AnchorPane acrText = new AnchorPane(lblText);
        Button btnDeleteWord = new Button("删除");

        btnDeleteWord.setMinWidth(Region.USE_PREF_SIZE);

        HBox.setHgrow(acrText, Priority.ALWAYS);

        AnchorPane.setTopAnchor(lblText, 0.0);
        AnchorPane.setBottomAnchor(lblText, 0.0);
        AnchorPane.setLeftAnchor(lblText, 0.0);
        AnchorPane.setRightAnchor(lblText, 0.0);

        hbxWord.setOnMouseClicked(event -> {
            editWord(word);
        });
        btnDeleteWord.setOnAction(event -> {
            delWordFromDict(word, editingDict);
        });

        hbxWord.getChildren().addAll(acrText, btnDeleteWord);

        return hbxWord;
    }

    private HBox createDescItem(Desc desc) {
        if (editingWord == null) {
            log("Cannot add Desc ["+desc.getContent()+"] to an null word!", Color.RED);
            return null;
        }
        HBox hbxDesc = new HBox();

        hbxDesc.setAlignment(Pos.CENTER_LEFT);
        hbxDesc.setSpacing(10);

        Label lblDesc = new Label(desc.toString());
        AnchorPane acrDesc = new AnchorPane(lblDesc);
        Button btnDeleteWord = new Button("删除");

        lblDesc.setWrapText(true);
        acrDesc.setPrefHeight(Region.USE_COMPUTED_SIZE);

        btnDeleteWord.setMinWidth(Region.USE_PREF_SIZE);

        HBox.setHgrow(acrDesc, Priority.ALWAYS);

        AnchorPane.setTopAnchor(lblDesc, 0.0);
        AnchorPane.setBottomAnchor(lblDesc, 0.0);
        AnchorPane.setLeftAnchor(lblDesc, 0.0);
        AnchorPane.setRightAnchor(lblDesc, 0.0);

        hbxDesc.setOnMouseClicked(event -> {
            editDesc(desc);
        });
        btnDeleteWord.setOnAction(event -> {
            event.consume();
            delDescFromWord(desc, editingWord);
        });

        hbxDesc.getChildren().addAll(acrDesc, btnDeleteWord);

        return hbxDesc;
    }


    private void refreshDictEditingArea() {
        if (editingDict == null) {
            lblVocInfo.setText("词汇量："+0);
            tfdDictName.setText("");
            tfdDictName.setDisable(true);
            tarDictIntro.setText("");
            tarDictIntro.setDisable(true);
            wordItemMap.clear();
            vbxWords.getChildren().clear();
        } else {
            lblVocInfo.setText("词汇量："+editingDict.wordCount());
            tfdDictName.setText(editingDict.getName());
            tfdDictName.setDisable(false);
            tarDictIntro.setText(editingDict.getIntro());
            tarDictIntro.setDisable(false);

            wordItemMap.clear();
            vbxWords.getChildren().clear();
            for (int i = 0; i < editingDict.wordCount(); i++) {
                Word word = editingDict.getWord(i);
                HBox hbxWord = createWordItem(word);
                vbxWords.getChildren().add(hbxWord);
                wordItemMap.put(word, hbxWord);
            }
        }
        editWord(null);
        editDesc(null);
    }

    private void refreshWordEditingArea() {
        if (this.editingWord == null) {
            tfdWordText.setText("");
            tfdWordText.setDisable(true);
            vbxDescs.getChildren().clear();
        } else {
            tfdWordText.setText(editingWord.getText());
            tfdWordText.setDisable(false);
            vbxDescs.getChildren().clear();
            descItemMap.clear();
            for (int i = 0; i < editingWord.descCount(); i++) {
                HBox hbxDesc = createDescItem(editingWord.getDesc(i));
                vbxDescs.getChildren().add(hbxDesc);
                descItemMap.put(editingWord.getDesc(i), hbxDesc);
            }
        }
        tfdDescPart.setText("");
        tarDescContent.setText("");
        tfdDescPart.setDisable(true);
        tarDescContent.setDisable(true);
        editDesc(null);
    }

    private void refreshDescEditingArea() {
        if (this.editingWord == null || editingDesc == null) {
            tfdDescPart.setText("");
            tfdDescPart.setDisable(true);
            tarDescContent.setText("");
            tarDescContent.setDisable(true);
        } else {
            tfdDescPart.setText(editingDesc.getPart());
            tfdDescPart.setDisable(false);
            tarDescContent.setText(editingDesc.getContent());
            tarDescContent.setDisable(false);
        }
    }

    private void updateWord(Word word) {
        HBox hbxWord = wordItemMap.get(word);
        if (hbxWord != null) {
            ((Label) ((AnchorPane) hbxWord.getChildren().get(0)).getChildren().get(0)).setText(word.toString());
        }
    }

    private void updateDesc(Desc desc) {
        HBox hbxDesc = descItemMap.get(desc);
        if (hbxDesc != null) {
            ((Label) ((AnchorPane) hbxDesc.getChildren().get(0)).getChildren().get(0)).setText(desc.toString());
        }
    }





    private void addWordToDict(Word word, Dict dict) {
        if (dict != null && word != null) {
            dict.addWord(word);
            log("Add Word ["+word.getText()+"] to Dictionary ["+dict.getName()+"]", Color.GREEN);
            if (dict == editingDict) {
                HBox hbxWord = createWordItem(word);
                vbxWords.getChildren().add(hbxWord);
                wordItemMap.put(word, hbxWord);
                lblVocInfo.setText("词汇量："+dict.wordCount());
            }
        } else {
            log("Cannot add null!", Color.RED);
        }
    }

    private void addDescToWord(Desc desc, Word word) {
        if (word != null && desc != null) {
            word.addDesc(desc);
            log("Add Description ["+desc.getContent()+"] to Word ["+word.getText()+"]", Color.GREEN);
            if (editingWord != null && editingWord == word) {
                HBox hbxDesc = createDescItem(desc);
                vbxDescs.getChildren().add(hbxDesc);
                descItemMap.put(desc, hbxDesc);
            }
        } else {
            log("Cannot add null!", Color.RED);
        }
    }

    private void delWordFromDict(Word word, Dict dict) {
        if (word == null || dict == null) {
            log("Cannot delete null", Color.RED);
            return;
        }
        dict.delWord(word);
        if (dict == editingDict) {
            vbxWords.getChildren().remove(wordItemMap.get(word));
            wordItemMap.remove(word);
            lblVocInfo.setText("词汇量："+dict.wordCount());
        }
        if (editingWord == word) {
            editWord(null);
        }
        log("Delete Word ["+word.getText()+"] from Dictionary ["+dict.getName()+"]", Color.YELLOW);
    }

    private void delDescFromWord(Desc desc, Word word) {
        if (desc == null || word == null) {
            log("Cannot delete null", Color.RED);
            return;
        }
        word.delDesc(desc);
        if (word == editingWord) {
            vbxDescs.getChildren().remove(descItemMap.get(desc));
            descItemMap.remove(desc);
        }
        if (editingDesc == desc) {
            editDesc(null);
        }
        log("Delete Description ["+desc.getContent()+"] from Word ["+word.getText()+"]", Color.YELLOW);
    }



    private void confirmDict() {
        if (this.editingDict == null) {
            return;
        }
        editingDict.setName(tfdDictName.getText());
        editingDict.setIntro(tarDictIntro.getText());
    }

    private void confirmWord() {
        if (this.editingWord == null) {
            return;
        }
        editingWord.setText(tfdWordText.getText());
        updateWord(editingWord);
        log("Confirm Word ["+editingWord.getText()+"]");
    }

    private void confirmDesc() {
        if (this.editingWord == null || this.editingDesc == null) {
            return;
        }
        editingDesc.setPart(tfdDescPart.getText());
        editingDesc.setContent(tarDescContent.getText());
        refreshDescEditingArea();
        updateDesc(editingDesc);
        log("Confirm Description ["+editingDesc.getContent()+"]");
    }




    private void editDict(Dict dict) {
        this.editingDict = dict;
        refreshDictEditingArea();
    }

    private void editWord(Word word) {
        this.editingWord = word;
        refreshWordEditingArea();
    }

    private void editDesc(Desc desc) {
        if (this.editingWord == null) {
            return;
        }
        this.editingDesc = desc;
        refreshDescEditingArea();
    }







    private void log(Object message) {
        log(message, null);
    }

    private void log(Object message, Paint fill) {
        System.out.println(message);
        Label lblMsg = new Label(Objects.toString(message));
        if (fill != null) {
            lblMsg.setTextFill(fill);
        }
        lblMsg.setWrapText(true);
        vbxLog.getChildren().add(lblMsg);
    }


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ExDictController.stage = stage;
    }
}
