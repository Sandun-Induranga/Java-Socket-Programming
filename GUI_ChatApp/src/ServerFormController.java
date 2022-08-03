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

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Server Started..");
                accept = serverSocket.accept();
                System.out.println("Client Connected..");

                dataOutputStream = new DataOutputStream(accept.getOutputStream());
                dataInputStream = new DataInputStream(accept.getInputStream());

//                bufferedReader = new BufferedReader(new InputStreamReader(System.in));

                String message = dataInputStream.readUTF();
                System.out.println(message);

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
