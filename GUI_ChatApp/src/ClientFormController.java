import com.jfoenix.utils.JFXHighlighter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientFormController {
    public TextArea textArea;
    public TextField textMessage;

    final int PORT = 5000;
    public ScrollPane sp;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;
    String message = "";
    List<String> receivedMsg = new ArrayList<>();
    JFXHighlighter highlighter = new JFXHighlighter();
    AnchorPane context = new AnchorPane();
    int i = 0;

    public void initialize() {
        Platform.setImplicitExit(false);
        sp.setContent(context);
        new Thread(() -> {
            try {
                socket = new Socket("localhost", PORT);

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

//                bufferedReader = new BufferedReader(new InputStreamReader(System.in));

                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    highlighter.setPaint(Color.YELLOW);
                    receivedMsg.add(message);
                    textArea.appendText(message + "\n");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Label label = new Label(message);
//                            sp.setContent(label);
                            context.setPrefHeight(context.getPrefHeight() + label.getPrefHeight());
                            label.setLayoutY(i);
                            context.getChildren().add(label);
                        }
                    });
                    i += 20;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException {
        textArea.appendText(textMessage.getText());
        dataOutputStream.writeUTF(textMessage.getText().trim());
        dataOutputStream.flush();
    }
}
