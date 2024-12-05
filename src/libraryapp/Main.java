package libraryapp;
import com.formdev.flatlaf.FlatLightLaf;

import view.LoginView; // Certifique-se de que o pacote está correto
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe principal do projeto.
 *
 * @author bruno
 */
public class Main {

    /**
     * Método principal do projeto.
     *
     * @param args os argumentos da linha de comando
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginView frame = new LoginView();
            frame.setVisible(true);
        });
    }
}
