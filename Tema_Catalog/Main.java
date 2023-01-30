import java.util.*;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        Catalog Catalogul_Meu=Catalog.getInstance();
        Catalogul_Meu.parse();
        ScoreVisitor visitor = new ScoreVisitor();
        new ChoosePage("Choose");
    }
}
