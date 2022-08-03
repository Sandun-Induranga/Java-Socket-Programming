import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {
    public TextArea textArea;
    public TextField textField;

    final int PORT = 5000;
    ServerSocket serverSocket;
    Socket accept;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;
    String message = "";

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                textArea.appendText("Server Started..");
                accept = serverSocket.accept();
                textArea.appendText("\nClient Connected..");

                dataOutputStream = new DataOutputStream(accept.getOutputStream());
                dataInputStream = new DataInputStream(accept.getInputStream());

//                bufferedReader = new BufferedReader(new InputStreamReader(System.in));


                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    textArea.appendText("\n" + message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException {
        dataOutputStream.writeUTF(textField.getText().trim());
        dataOutputStream.flush();
    }
}
