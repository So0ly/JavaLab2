package com.lab;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class MainFrame {

	private JFrame frame;
	private Synchronizator synch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		initialize();
		synch = new Synchronizator();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		JTextArea textArea = new JTextArea();
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
		
		JButton btnStart = new JButton("Start");
		panel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("Start\n");
				synch.start();
			}
		});
		
		JButton btnStop = new JButton("Stop");
		panel.add(btnStop);
		
		btnStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("Stop\n");
				synch.stop();
			}
		});
	}

}
