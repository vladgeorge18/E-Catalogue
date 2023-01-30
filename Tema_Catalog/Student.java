public class Student extends User implements Comparable<Student>{
    private Parent mother;
    private Parent father;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Student() {
    }

    public void setMother(Parent mother){
        this.mother=mother;
    }
    public void setFather(Parent father){
        this.father=father;
    }

    public Parent getFather() {
        return father;
    }

    public Parent getMother() {
        return mother;
    }

    public int compareTo(Student other){
        String name1=this.getFirstName()+this.getLastName();
        String name2=other.getFirstName()+other.getLastName();
        return name1.compareTo(name2);
    }

   /* public boolean equals(Object o) {
        return super.equals(o);
    }

    public int hashCode() {
        return super.hashCode();
    }*/
}
