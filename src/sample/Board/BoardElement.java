package sample.Board;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class BoardElement {
    private double posX;
    private double posY;
    private boolean permeable;
    private double elementSize;

    private String imagePath;
    private ImageView imageView;


    public BoardElement(double iY, double iX, double elementSize, String imagePath, boolean permeable) {
        this.posX = iX*elementSize;
        this.posY = iY*elementSize;
        this.elementSize = elementSize;
        this.imagePath = imagePath;
        this.permeable = permeable;
        this.imageView = new ImageView(new Image(imagePath));
        imageView.relocate(posX, posY);
        imageView.setFitHeight(elementSize);
        imageView.setFitWidth(elementSize);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPermeable(boolean permeable){
        this.permeable = permeable;
    }

    public boolean isPermeable(){
        return  permeable;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void changeImageView(String imagePath){
        Platform.runLater(()-> imageView.setImage(new Image(imagePath)));
    }
}
