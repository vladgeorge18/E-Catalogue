import java.util.*;
public class Group extends TreeSet<Student> {
    String ID;
    Assistant assistant;
    Comparator<Student> comp;

    public Group(String ID, Assistant assistant, Comparator<Student> comp){
        this.ID=ID;
        this.assistant=assistant;
        this.comp=comp;
    }
    public Group(String ID, Assistant assistant) {
        this.ID=ID;
        this.assistant=assistant;
    }


}
