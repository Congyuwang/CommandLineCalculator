import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class HelpMenu {

    private static final JTextPane helpText = new JTextPane() {
        private static final long serialVersionUID = 8773823648370901685L;
        {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setContentType("text/html");
            try {
                setText(new String(Files.readAllBytes(Paths.get("resource/HelpMenu.html"))));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            setEditable(false);
        }
    };

    static JScrollPane createHelp() {
        JScrollPane help = new JScrollPane(helpText);
        help.setBackground(Color.WHITE);
        help.setForeground(Color.BLACK);
        return help;
    }
}
