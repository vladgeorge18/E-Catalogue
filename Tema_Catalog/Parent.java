public class Parent extends User implements Observer{
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
    }
    public void update(Notification notification){
        System.out.println(notification.toString());
    }

}
