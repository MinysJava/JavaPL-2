package Lesson_2_3.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Controller {
    @FXML
    ListView listView;

    @FXML
    TextField textField;

    @FXML
    Button btn1;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

    @FXML
    TextField loginField;

    @FXML
    TextField passwordField;

    @FXML
    ListView<String> clientList;

    private boolean isAuthorized;

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if(!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            clientList.setVisible(false);
            clientList.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            clientList.setVisible(true);
            clientList.setManaged(true);
        }
    }

    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    String msg;
    File log;

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
//----------------------------------------------------------------------------------------------------------------------------
                                if (!new File("\\Log").exists()){
                                    new File("\\Log").mkdir();
                                }

                                String[] tokens = str.split(" ");                                       // создаем файл лога при авторизации
                                log = new File("Log\\history_" + tokens[1] +".txt");
                                FileOutputStream writeLog = new FileOutputStream(log, true);
                                byte[] outData = "".getBytes();
                                writeLog.write(outData);
                                writeLog.close();
//----------------------------------------------------------------------------------------------------------------------------
                                FileReader logReader = new FileReader(log);                              // эта часть кода считывает историю из файла лога и выводит последние 100 сообщений
                                BufferedReader logBufferedReader = new BufferedReader(logReader);
                                String logHistoryLine;
                                ArrayList arrayHistoryLog = new ArrayList();

                                while((logHistoryLine = logBufferedReader.readLine()) != null) {        //считываем лог построчно и заполняем строками массив
                                    arrayHistoryLog.add(logHistoryLine);
                                }

                                for(int i = arrayHistoryLog.size() - 100; i < arrayHistoryLog.size(); i++){ // в цикле выводим последние 100 сообщений
                                    if (i >= 0) {
                                        String hMsg = (String) arrayHistoryLog.get(i);
                                        Label label = new Label(hMsg + "\n");
                                        VBox vBox = new VBox();
                                        String[] hTokens = hMsg.split(" ");
                                        System.out.println(tokens[2]);
                                        if (hTokens[0].equals(tokens[2] + ":")) {
                                            vBox.setAlignment(Pos.TOP_RIGHT);
                                        } else {
                                            vBox.setAlignment(Pos.TOP_LEFT);
                                        }
                                        vBox.getChildren().add(label);

                                        listView.getItems().add(vBox);
                                    }
                                }
//----------------------------------------------------------------------------------------------------------------------------
                                break;
                            } else {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.getItems().add(str + "\n");
                                    }
                                });
                            }
                        }

                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/serverclosed")) break;
                            if (str.startsWith("/clientlist")) {
                                String[] tokens = str.split(" ");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        clientList.getItems().clear();
                                        for (int i = 1; i < tokens.length; i++) {
                                            clientList.getItems().add(tokens[i]);
                                        }
                                    }
                                });
                            } else {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
//----------------------------------------------------------------------------------------------------------
                                        FileOutputStream writeLog = null;                           // Записываем сообщение в лог когда оно приходит на клиент
                                        try {
                                            writeLog = new FileOutputStream(log, true);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        byte[] outData = (str + "\n").getBytes();
                                        try {
                                            writeLog.write(outData);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            writeLog.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//-------------------------------------------------------------------------------------------------------
                                        Label label = new Label(str + "\n");
                                        VBox vBox = new VBox();
                                        String[] tokens = str.split(" ");
                                        if(tokens[1].equals(msg)){
                                            vBox.setAlignment(Pos.TOP_RIGHT);
                                        } else {
                                            vBox.setAlignment(Pos.TOP_LEFT);
                                        }
                                        vBox.getChildren().add(label);

                                        listView.getItems().add(vBox);
                                        msg = null;
                                    }
                                });
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setAuthorized(false);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Dispose() {
        System.out.println("Отправляем сообщение о закрытии");
        try {
            if(out != null) {
                out.writeUTF("/end");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            msg = textField.getText();
            out.writeUTF(msg);
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if(socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectClient(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            System.out.println("Двойной клик");
        }
    }
}
