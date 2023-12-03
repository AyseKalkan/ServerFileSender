package server;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.io.File;

public class Server {

    static ArrayList<MyFile> myFiles = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        int fileId = 0 ;
        JFrame jFrame = new JFrame("Server");
        jFrame.setSize(400,400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
       
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       
        JLabel jlTitle = new JLabel("File Receiver");
        jlTitle.setFont(new Font("Arial",Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
       
        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
       
        ServerSocket serverSocket = new ServerSocket(60000);
       
        while(true){
            try {
                Socket socket = serverSocket.accept();
                sendFileList(socket);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
               
                int fileNameLenght = dataInputStream.readInt();
               
                if(fileNameLenght > 0){
                	System.out.println(fileNameLenght);
                    byte[] fileNameBytes = new byte[fileNameLenght];
                    dataInputStream.readFully(fileNameBytes,0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);
                   
                    int fileContentLenght = dataInputStream.readInt();
                   
                    if(fileContentLenght >0 ){
                    	System.out.println(fileNameLenght);
                        byte[] fileContentBytes = new byte[fileContentLenght];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLenght);
                       
                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));
                       
                        JLabel jlFileName = new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial",Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10,0,10,0));
                        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
                       
                        if(getFileExtension(fileName).equalsIgnoreCase("txt")){
                            jpFileRow.setName(String.valueOf(fileId));
                            jpFileRow.addMouseListener(getMyMouseListener());
                           
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();
                           
                           
                        }else{
                            jpFileRow.setName(String.valueOf(fileId));
                            jpFileRow.addMouseListener(getMyMouseListener());
                           
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                           
                            jFrame.validate();
                                   
                        }
                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                        fileId++;
                    }
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
   
    public static MouseListener getMyMouseListener(){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                int fileId = Integer.parseInt(jPanel.getName());
               
                for(MyFile myFile: myFiles){
                    if(myFile.getId() == fileId){
                        JFrame jfPrewiew = createFrame(myFile.getName(),myFile.getData(),myFile.getFileExtension());
                        jfPrewiew.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
               
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               
            }

            @Override
            public void mouseExited(MouseEvent e) {
               
            }
        };
    }
   
   public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
    JFrame jFrame = new JFrame("File Downloader");
    jFrame.setSize(400, 400);

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

    JScrollPane jScrollPane = new JScrollPane(jPanel); // JScrollPane ekleyin

    JLabel jlTitle = new JLabel("File Downloader");
    jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
    jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

    JLabel jlPrompt = new JLabel("Are you sure you want to download " + fileName);
    jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
    jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));

    JButton jbYes = new JButton("Yes");
    jbYes.setPreferredSize(new Dimension(150, 75));
    jbYes.setFont(new Font("Arial", Font.BOLD, 20));

    JButton jbNo = new JButton("No");
    jbNo.setPreferredSize(new Dimension(150, 75));
    jbNo.setFont(new Font("Arial", Font.BOLD, 20));

    JLabel jlFileContent = new JLabel();
    jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel jbButtons = new JPanel();
    jbButtons.setBorder(new EmptyBorder(20, 0, 10, 0));

    jbButtons.add(jbYes);
    jbButtons.add(jbNo);

    if (fileExtension.equalsIgnoreCase("txt")) {
        jlFileContent.setText(("<html>" + new String(fileData) + "<html/>"));
    } else {
        jlFileContent.setIcon(new ImageIcon(fileData));
    }

    jbYes.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	String targetDirectory = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "ServerDepo" + File.separator;
            File fileToDownload = new File(targetDirectory + fileName);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);

                fileOutputStream.write(fileData);
                fileOutputStream.close();

                jFrame.dispose();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    });

    jbNo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
        }
    });

    jPanel.add(jlTitle);
    jPanel.add(jlPrompt);
    jPanel.add(jlFileContent);
    jPanel.add(jbButtons);

    jFrame.add(jScrollPane); // JScrollPane'ı JFrame'a ekleyin
    return jFrame;
}

    public static String getFileExtension(String fileName){
        int i = fileName.lastIndexOf('.');
       
        if(i > 0){
            return fileName.substring(i + 1);
        }else{
            return "No extension found";
        }
    }
    
    
    public static void sendFileList(Socket socket) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Gönderilecek dosya listesi
            StringBuilder fileList = new StringBuilder();
            for (MyFile file : myFiles) {
                fileList.append(file.getName()).append("\n");
            }

            // Dosya listesinin uzunluğunu ve içeriğini istemciye gönder
            byte[] fileListBytes = fileList.toString().getBytes();
            dataOutputStream.writeInt(fileListBytes.length);
            dataOutputStream.write(fileListBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
}