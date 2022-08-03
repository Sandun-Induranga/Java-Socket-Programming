import com.jfoenix.utils.JFXHighlighter;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;
    String message = "";
    List<String> receivedMsg = new ArrayList<>();
    JFXHighlighter highlighter = new JFXHighlighter();

    public void initialize() {
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
                    for (String s :
                            receivedMsg) {
                        highlighter.highlight(textArea, s);
                    }
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
