package passfort.models;

public class Regular extends User {

    public Regular(int id, String username, String password) {
        super(id, username, password, "User");
    }

    @Override
    public void displayUserRole() {
        System.out.println("Role: Regular User");
    }
}
