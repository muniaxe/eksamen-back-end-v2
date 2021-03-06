package utils;


import com.google.common.base.Strings;
import dtos.DeveloperDTO;
import dtos.ProjectDTO;
import entities.Developer;
import entities.Project;
import facades.DeveloperFacade;
import facades.ProjectFacade;
import facades.UserFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Populate {
    private final EntityManagerFactory emf;


    public static void main(String[] args) {
        new Populate(EMF_Creator.createEntityManagerFactory()).populateAll();
    }

    public Populate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<String> populateAll() {
        List<String> populated = new ArrayList<>();
        if(populateUsers())
            populated.add("users");
        if(populateDevelopers())
            populated.add("developers");
        if(populateProjects())
            populated.add("projects");

        return populated;
    }

    /**
     *
     * @return Boolean regarding table being populated or not.
     *
     * */
    public boolean populateUsers() throws IllegalArgumentException {
        UserFacade userFacade = UserFacade.getInstance(this.emf);

        if (!userFacade.getAllPrivate().isEmpty()) return false;

        // NOTICE: Always set your password as environment variables.
        String password_admin = "test";
        String password_user = "test";

        boolean isDeployed = System.getenv("DEPLOYED") != null;
        if(isDeployed) {
            password_user = System.getenv("PASSWORD_DEFAULT_USER");
            password_admin = System.getenv("PASSWORD_DEFAULT_ADMIN");

            // Do not allow "empty" passwords in production.
            if(Strings.isNullOrEmpty(password_admin) || password_admin.trim().length() < 3 || Strings.isNullOrEmpty(password_user) || password_user.trim().length() < 3)
                throw new IllegalArgumentException("FAILED POPULATE OF USERS: Passwords were empty or less than 3 characters? Are environment variables: [PASSWORD_DEFAULT_USER, PASSWORD_DEFAULT_ADMIN] set?");
        }

        userFacade._create("user", password_user, new ArrayList<>());
        userFacade._create("admin", password_admin, Collections.singletonList("admin"));

        return true;
    }

    public boolean populateDevelopers() throws IllegalArgumentException {
        DeveloperFacade developerFacade = DeveloperFacade.getDeveloperFacade(this.emf);

        if(!developerFacade.getDevelopers().isEmpty()) return false;

        developerFacade.addDeveloper("Alpha Developer", "alpha@developer.test", "12345678", 180);
        developerFacade.addDeveloper("Beta Developer", "beta@developer.test", "87654321", 150);

        return true;
    }

    public boolean populateProjects() throws IllegalArgumentException{
        ProjectFacade projectFacade = ProjectFacade.getProjectFacade(this.emf);

        if(!projectFacade.getProjects().isEmpty()) return false;

        projectFacade.addProject("Big project", "This project is the big one");
        projectFacade.addProject("Small project", "This project is the small project");

        return true;
    }



}
