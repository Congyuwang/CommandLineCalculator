import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class HelpMenu {

    private static final JTextPane helpText = new JTextPane() {
        private static final long serialVersionUID = 8773823648370901685L;
        {
            Scanner scanner = new Scanner(getClass().getResourceAsStream("resource/HelpMenu.html"));
            StringBuilder stringBuilder = new StringBuilder();
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setContentType("text/html");
            while(scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }
            setText(stringBuilder.toString());
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
