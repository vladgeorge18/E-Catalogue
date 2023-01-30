public class Teacher extends User implements Element{
    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    /*public int hashCode() {
        return super.hashCode();
    }*/
}
