package org.network.gameframes;

import org.network.WindowConfig;
import org.network.gamecompnent.GameManager;
import org.network.panel.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OkOrNoFrame extends JLayeredPane {
    private JButton okButton;
    private ActionListener lastOkActionListener;
    private JLabel okPanelTitle;
    public OkOrNoFrame(){
        setSize(WindowConfig.WIDTH/3,WindowConfig.HEIGHT/3);
        setLocation(WindowConfig.WIDTH/3 - getWidth()/2,WindowConfig.HEIGHT/3 - getHeight()/2);

        BackgroundPanel backgroundPanel = new BackgroundPanel("ui/speechbox.png");
        backgroundPanel.setLocation(0,0);
        backgroundPanel.setSize(getSize());
        add(backgroundPanel,JLayeredPane.FRAME_CONTENT_LAYER);

        okPanelTitle = new JLabel(
                "<html>테스트 디버깅 용 텍스트 입니다." +
                        "<br>테스트 디버깅 용 텍스트 입니다.</html>"
        );
        okPanelTitle.setHorizontalAlignment(JLabel.CENTER);
        okPanelTitle.setBounds(30,0,getWidth() - 60,200);
        okPanelTitle.setFont(new Font("Default", Font.BOLD, 14));

        okButton = new JButton("확인");
        JButton noButton = new JButton("닫기");
        noButton.addActionListener(e -> {
            setVisible(false);
            okButton.removeActionListener(lastOkActionListener);
            GameManager.getInstance().isLocalPlayerMovable = true;
        });

        okButton.addActionListener(e -> {
            setVisible(false);
            GameManager.getInstance().isLocalPlayerMovable = true;
        });

        okButton.setBounds(120,150,100,30);
        noButton.setBounds(230,150,100,30);

        add(okPanelTitle,JLayeredPane.POPUP_LAYER);
        add(okButton,JLayeredPane.POPUP_LAYER);
        add(noButton,JLayeredPane.POPUP_LAYER);

        setVisible(false);
    }
    public void showOkNoPanel(String title,ActionListener okAction) {
        GameManager.getInstance().isLocalPlayerMovable = false;
        okPanelTitle.setText("<html>"+title+"</html>");
        lastOkActionListener = okAction;
        okButton.addActionListener(lastOkActionListener);
        setVisible(true);
    }
}
