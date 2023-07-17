package Cronometro;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class conexao extends Frame implements ActionListener {
    private Label label1, label2, label3;
    private Button button, exitButton;
    private TextField textField1, textField2, textField3;
    private Connection connection;
    private Statement statement;
    
    public conexao() {
        super("Telemetria");
        
        // Definir layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        // Criar componentes
        label1 = new Label("Tempo da 1ª volta:");
        label2 = new Label("Tempo da 2ª volta:");
        label3 = new Label("Tempo total:");
        button = new Button("Salvar tempos");
        exitButton = new Button("Sair");
        textField1 = new TextField();
        textField2 = new TextField();
        textField3 = new TextField();
        textField1.setPreferredSize(new Dimension(100, 30));
        textField2.setPreferredSize(new Dimension(100, 30));
        textField3.setPreferredSize(new Dimension(100, 30));
        
        // Adicionar componentes ao frame
        add(label1);
        add(textField1);
        add(label2);
        add(textField2);
        add(label3);
        add(textField3);
        add(exitButton); 
        add(new Label(""));
        add(button);
        
        // Adicionar listener ao botão
        button.addActionListener(this);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Conectar com o banco de dados
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/telemetria?user=root&password=");
            statement = connection.createStatement();
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
        // Definir tamanho e visibilidade do frame
        setSize(800, 250);
        setVisible(true);
            //Desabilitando o clipboard
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_PRINTSCREEN) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection("");
                clipboard.setContents(stringSelection, null);
                System.out.println("Empty string copied to clipboard.");
                JOptionPane.showMessageDialog(null, "A tecla Print Screen está desabilitada!");
        
        }
        return false;
        }
    });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Salvar tempos no banco de dados
        try {
            String query = "INSERT INTO tempos (tempo_volta1, tempo_volta2, tempo_total) VALUES ('" + textField1.getText() + "', '" + textField2.getText() + "', '" + textField3.getText() + "')";
            statement.executeUpdate(query);
            System.out.println("Tempos salvos no banco de dados com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao salvar tempos no banco de dados: " + ex.getMessage());
        }
    }
   
    
    public static void main(String[] args) {
        new conexao();
    }

    private void setDefaultCloseOperation(int EXIT_ON_CLOSE) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
