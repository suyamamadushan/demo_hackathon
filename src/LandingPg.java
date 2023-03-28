import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPg {
    private JPanel landingPanel;
    private JButton button1;
    private JTextField selectedFile_txt;
    private JButton proceedButton;

    private static String selectedFilePath;

    public LandingPg() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(landingPanel);
                selectedFilePath = fc.getSelectedFile().getPath();
                selectedFile_txt.setText(selectedFilePath);
            }
        });
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(selectedFile_txt.getText() ==" ")
                    JOptionPane.showMessageDialog(landingPanel, "Select a valid Image",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                else{
                    JFrame frame = new JFrame("TummyFit");
                    frame.setContentPane(new HomePg(selectedFile_txt.getText()).getHomePanel());
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    landingPanel.getRootPane().setVisible(false);
                }


            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("TummyFit");
        frame.setContentPane(new LandingPg().landingPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static String getSelectedFilePath(){
        return selectedFilePath;
    }


}
