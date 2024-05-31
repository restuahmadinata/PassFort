package passfort.models;

public class Admin extends User {

    public Admin(int id, String username, String password) {
        super(id, username, password, "Admin");
    }

    @Override
    public void displayUserRole() {
        System.out.println("Role: Admin");
    }
}
