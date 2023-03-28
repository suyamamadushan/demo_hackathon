import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class HomePg {
    private JPanel homePanel;
    private JComboBox gender_dd;
    private JTextField age_txt;
    private JComboBox activeness_dd;
    private JButton searchButton;
    private JTextField suggested_txt;

    private JButton submitButton;
    private JTextField remainingCalories_txt;
    private JTextField calories_txt;
    private JLabel imageLbl;
    private JPanel Jpanel11;
    private JComboBox feedback_dd;
    private JPanel selectedFood;
    private JLabel foodType;
    private JLabel selectedFoodImg;

    private String imagePath;

    public HomePg(String imagePath) {
        this.imagePath = imagePath;

        SearchFood searchFood = new SearchFood();
        boolean getFeedback = true;


        Random rand = new Random();
        calories_txt.setText(String.valueOf(rand.nextInt(300)+170));

        String foodName = "";//foodReg.sendRequest(imagePath);

        foodType.setText(foodName);


        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(200, 200,  Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);

        selectedFoodImg.setIcon(imageIcon);
        selectedFoodImg.validate();



        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!age_txt.getText().matches("\\d+")){
                    JOptionPane.showMessageDialog(homePanel, "Enter a valid Age",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if (Integer.parseInt(age_txt.getText())>90||Integer.parseInt(age_txt.getText())<1) {
                    JOptionPane.showMessageDialog(homePanel, "Enter a valid Age",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else if(!calories_txt.getText().matches("\\d+")){
                    JOptionPane.showMessageDialog(homePanel, "Enter a valid Calories",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else{
                    String foodName = "";
                    try {
                        foodName = searchFood.getFoodName(Integer.parseInt(calories_txt.getText()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(foodName.equalsIgnoreCase("Not enough data to suggest")){
                        suggested_txt.setText(foodName);
                        submitButton.setEnabled(false);
                        remainingCalories_txt.setText("");
                        imageLbl.setIcon(null);

                    }else{
                        //load image
                        suggested_txt.setText(foodName);
                        remainingCalories_txt.setText(searchFood.getRemainingCalories(gender_dd.getSelectedItem().toString(),
                                Integer.parseInt(age_txt.getText()),activeness_dd.getSelectedItem().toString())+"");
                        submitButton.setEnabled(true);



                        ImageIcon imageIcon = new ImageIcon(searchFood.getImagePath());//("./src/pic/1.png"); // load the image to a imageIcon
                        Image image = imageIcon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(200, 200,  Image.SCALE_SMOOTH); // scale it the smooth way
                        imageIcon = new ImageIcon(newimg);  // transform it back

                        imageLbl.setIcon(imageIcon);//new ImageIcon("./src/pic/1.jpg"));
                        imageLbl.validate();

                    }

                }


            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String food = suggested_txt.getText();

                if(!food.equalsIgnoreCase("Not enough data to suggest")&&food!="") {
                    try {
                        searchFood.setFeedback((feedback_dd.getSelectedItem().toString().equalsIgnoreCase("Like"))?true:false,
                                food);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public Container getHomePanel() {
        return homePanel;
    }
}
