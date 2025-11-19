/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;
import form.LoginForm;
/**
 *
 * @author syafiq
 */
public class Main {

    public static void main(String[] args) {
       try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new LoginForm().setVisible(true);
    }
}
