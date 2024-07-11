/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Icon;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author thanh
 */
public class XImage {

    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/pro1041/com/icon/icons8-son-goku-35.png");
        return new ImageIcon(url).getImage();

    }
}
