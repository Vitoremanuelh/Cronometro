package Cronometro;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class Cronometro extends JFrame {

    private JLabel labelVolta1, labelVolta2, labelTempoTotal;
    private JButton botaoInicio1 , botaoInicio2, botaoFim1, botaoFim2;
    private long tempoInicio1, tempoInicio2, tempoFim1, tempoFim2;
    private boolean emExecucao1, emExecucao2;
    private Connection connection;
    private Statement statement;

    public Cronometro() throws IOException {
        super("APS -Cronômetro");

        // INTERFACE - BOTOES E LABEL 
        getContentPane() .setBackground(Color.lightGray);
        labelVolta1 = new JLabel("‎‎1ª volta: 00:00.000");
        labelVolta2 = new JLabel("‎2ª volta: 00:00.000");
        labelTempoTotal = new JLabel("Tempo Total: 00:00.000");
        botaoInicio1 = new JButton("Iniciar 1ª volta");
        botaoInicio2 = new JButton("Iniciar 2ª volta");
        botaoFim1 = new JButton("Parar 1ª volta");
        botaoFim2 = new JButton("Parar 2ª volta");
        botaoFim1.setBackground(Color.red);
        botaoFim2.setBackground(Color.red);
        botaoInicio1.setBackground(Color.green);
        botaoInicio2.setBackground(Color.green);


        // ADICIONA OS BOTÕES E TEXTOS
        setLayout(new GridLayout(4, 2));
        add(labelVolta1);
        add(labelVolta2);
        add(botaoInicio1);
        add(botaoInicio2);
        add(botaoFim1);
        add(botaoFim2);
        add(labelTempoTotal);
        
      //BOTAO 1
        botaoInicio1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempoInicio1 = System.currentTimeMillis();
                labelVolta1.setText("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ 1ª volta: 00:00.000");
                botaoFim1.setEnabled(true);
                botaoInicio1.setEnabled(false);
                botaoInicio2.setEnabled(false);
                botaoFim2.setEnabled(false);
                
                emExecucao1 = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (emExecucao1) {
                            long tempo = System.currentTimeMillis() - tempoInicio1;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    labelVolta1.setText("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ 1ª volta: " + (tempo));
                                }
                            });
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
        System.out.println(botaoInicio1);

        botaoFim1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempoInicio1 == 0) {
                    return;
                }
                tempoFim1 = System.currentTimeMillis();
                botaoFim1.setEnabled(false);
                botaoInicio1.setEnabled(true);
                botaoInicio2.setEnabled(true);
                botaoFim2.setEnabled(true);
                emExecucao1 = false;
                labelVolta1.setText("‎1ª volta: " + formatTempo(tempoFim1 - tempoInicio1));
                if (tempoInicio2 != 0) {
                    long tempoTotal = tempoFim2 + tempoFim1 - tempoInicio2 - tempoInicio1;
                    labelTempoTotal.setText(formatTempo(tempoTotal));
                }
            }

            private String formatTempo(long l) {
                long minutos = TimeUnit.MILLISECONDS.toMinutes(l);
                long segundos = TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(minutos);
                long milissegundos = l - TimeUnit.SECONDS.toMillis(segundos) - TimeUnit.MINUTES.toMillis(minutos);
                return String.format("%02d:%02d.%03d", minutos, segundos, milissegundos);
            }
        });


        //BOTAO 2
        botaoInicio2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempoInicio2 = System.currentTimeMillis();
                labelVolta2.setText("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ 2ª volta: 00:00.000");
                botaoFim2.setEnabled(true);
                botaoInicio2.setEnabled(false);
                botaoInicio1.setEnabled(false);
                botaoFim1.setEnabled(false);
                emExecucao2 = true;
                new Thread(new Runnable() {
            @Override
            public void run() {
                while (emExecucao2) {
                    long tempo = System.currentTimeMillis() - tempoInicio2; 
                    SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                labelVolta2.setText("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ 2ª volta: " + formatTempo(tempo));
            }
            });
                try { Thread.sleep(10);
                } 
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                        }
                    }       
                }               
        }).start();
    }
});
botaoFim2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (tempoInicio2 == 0) {
            return; // Verifica botão 2
        }
        tempoFim2 = System.currentTimeMillis();
        botaoFim2.setEnabled(false);
        botaoInicio1.setEnabled(true);
        botaoInicio2.setEnabled(true);
        botaoFim1.setEnabled(true);
        emExecucao2 = false;
        labelVolta2.setText("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Volta 2: " + formatTempo(tempoFim2 - tempoInicio2));
        if (tempoInicio1 != 0) {
            // SOMA OS TEMPOS
            long tempoTotal = tempoFim2 + tempoFim1 - tempoInicio2 - tempoInicio1;
            labelTempoTotal.setText("Tempo Total: " + formatTempo(tempoTotal));
        }
    }

            private String formatTempo(long l) {
                long minutos = TimeUnit.MILLISECONDS.toMinutes(l);
                long segundos = TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(minutos);
                long milissegundos = l - TimeUnit.SECONDS.toMillis(segundos) - TimeUnit.MINUTES.toMillis(minutos);
                return String.format("%02d:%02d.%03d", minutos, segundos, milissegundos);
            }
        });

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 250);
    setLocationRelativeTo(null);
    setVisible(true);
    System.out.println(botaoInicio2);
    
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
    
    
     
private String formatTempo(long l) {
    Duration duracao = Duration.ofMillis(l);
    return String.format("%02d:%02d.%03d",
            duracao.toMinutes(), 
            duracao.toSecondsPart(), 
            duracao.toMillisPart());
}


public static void main(String[] args) throws IOException {
    new Cronometro();
}
}
