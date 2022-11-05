//시작화면 gif로 만들려고 일단 가져와서 코딩중


package org.network.packet;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class startFrame extends JFrame{
    JPanel contentpane;
    JLabel imageLabel=new JLabel();
    JLabel headerLabel=new JLabel();

    public void start(){
        try{
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            contentpane=(JPanel) getContentPane();
            contentpane.setLayout(new BorderLayout());
            setSize(new Dimension(400,300));
            setTitle("포켓몬");
            //상단 레이블
            headerLabel.setFont(new java.awt.Font("고딕",Font.BOLD,16));
            headerLabel.setText("포켓몬");
            contentpane.add(headerLabel, BorderLayout.NORTH);
            //이미지 레이블
            ImageIcon ii= new ImageIcon(this.getClass().getResource("org/network/pocket.gif"));
            imageLabel.setIcon(ii);
            contentpane.add(imageLabel,java.awt.BorderLayout.CENTER);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
