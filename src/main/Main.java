package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main extends JFrame{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputUsername;
	private JTextField inputPassword;
	private String filePath = new String("userList.txt");

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panelInputs = new JPanel();
		contentPane.add(panelInputs, BorderLayout.CENTER);
		panelInputs.setLayout(new GridLayout(4, 0, 0, 0));
		
		JLabel lblUsername = new JLabel("Usuário");
		panelInputs.add(lblUsername);
		
		inputUsername = new JTextField();
		panelInputs.add(inputUsername);
		inputUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Senha");
		panelInputs.add(lblPassword);
		
		inputPassword = new JTextField();
		panelInputs.add(inputPassword);
		inputPassword.setColumns(10);
		
		JPanel panelButtons = new JPanel();
		contentPane.add(panelButtons, BorderLayout.SOUTH);
		
		JButton btnRegister = new JButton("Cadastrar");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		panelButtons.add(btnRegister);
		
		JButton btnAuthenticate = new JButton("Autenticar");
		btnAuthenticate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authenticate();
			}
		});
		panelButtons.add(btnAuthenticate);
	}
	
	public String generateHash(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] byteHash = md.digest(input.getBytes("UTF-8"));
            String stringHash = new BigInteger(1, byteHash).toString(16);
            return stringHash;
        } catch (Exception e ){
            throw new RuntimeException();
        }
    }
	
	public boolean validateFields(String user, String password) {
		if(user.length() != 4 || password.length() != 4) {
			JOptionPane.showMessageDialog(null, "Usuário/senha deve ter 4 caracteres!", "ERRO", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		List<String> lines = Collections.emptyList();
	    try {
	      lines =
	       Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
	      
	      for(String line: lines) {
	    	  if(line.substring(9, 13).equals(user)) {
	    		  JOptionPane.showMessageDialog(null, "Usuário já cadastrado!", "ERRO", JOptionPane.ERROR_MESSAGE);
	    		  return false;
	    	  }
	      }
	      
	    } catch (IOException e) {
	     System.out.println(e.getMessage());
	    }
	    
		return true;
	}
	
    public  void register() {
    	String username = inputUsername.getText();
    	String password = inputPassword.getText();
  
    	if(!validateFields(username, password)) {
    		return;
    	}

        String hash = generateHash(password);
        
        
        try {
        	FileWriter writer = new FileWriter(filePath, true);
            writer.write("username=" + username + ", password=" + hash);
            writer.write("\r\n");  
            writer.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
    }

    public  void authenticate(){
    	String username = inputUsername.getText();
    	String password = inputPassword.getText();
    	String hash = generateHash(password);
    	
    	List<String> lines = Collections.emptyList();
	    try {
	      lines =
	       Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
	      
	      if(lines.contains("username=" + username + ", password=" + hash)) {
	    	  JOptionPane.showMessageDialog(null, "Usuário autenticado!");
	      }else {
	    	  JOptionPane.showMessageDialog(null, "Não foi possível autenticar", "ERRO", JOptionPane.ERROR_MESSAGE);
	      }
	      
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
    }

}
