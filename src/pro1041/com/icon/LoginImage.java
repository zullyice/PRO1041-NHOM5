/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.icon;

import Icon.XImage;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Tom
 */
public class LoginImage {
    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/pro1041/com/icon/account.png");
        return new ImageIcon(url).getImage();

    }
}
