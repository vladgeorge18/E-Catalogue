public abstract class User {
    private String firstName, lastName;
    public User(String firstName, String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User() {
    }

    public String toString(){
        return firstName +" "+ lastName;
    }

    public boolean equals(Object o) {
        User s = (User) o;
        if (this.getFirstName().equals(s.getFirstName()) && this.getLastName().equals(s.getLastName())) {
            return true;
        } else {
            return false;
        }
    }

   /* public int hashCode() {
        if (this.getFirstName() == null ^ this.getLastName() == null) {
            return 0;
        } else {
            return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
        }
    }*/
}
