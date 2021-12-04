package project.controller;

import project.model.EntitiesManager;
import project.model.entities.Citizen;
import project.model.entities.Clinic;
import project.model.entities.Worker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Scanner;

enum UserType {
    Citizen,
    Worker,
    ClinicManager,
    OperationManager
}

public class Controller {

    EntityManagerFactory entityManagerFactory;
    EntitiesManager entitiesManager;
    Citizen citizenUser;
    Worker workerUser;
    Clinic clinicManagerUser;
    UserType currentUserType;

    public Controller() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entitiesManager = new EntitiesManager(entityManagerFactory);

        Scanner scanner = new Scanner(System.in);
        initializeUser(scanner);
        switch (currentUserType) {
            case Citizen:
                citizenMenu(scanner);
                break;
            case Worker:
                workerMenu(scanner);
                break;
            case ClinicManager:
                clinicManagerMenu(scanner);
                break;
            case OperationManager:
                operationManagerMenu(scanner);
                break;
            default:
                break;
        }
    }

    void initializeUser(Scanner scanner) {
        while (currentUserType == null) try {
            System.out.println("> Choose your user type:");
            for (int i = 0; i < UserType.values().length; i++) {
                System.out.println((i + 1) + ") " + UserType.values()[i]);
            }

            currentUserType = UserType.values()[scanner.nextInt() - 1];
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
        }

        EntityManager em = entityManagerFactory.createEntityManager();

        int id = -1;
        boolean foundID = false;

        while (!foundID) try {
            switch (currentUserType) {
                case Citizen:
                    System.out.println("Enter citizen ID");
                    id = scanner.nextInt();
                    break;
                case Worker:
                    System.out.println("Enter worker ID");
                    id = scanner.nextInt();
                    break;
                case ClinicManager:
                    System.out.println("Enter clinic ID");
                    id = scanner.nextInt();
                    break;
                default:
                    break;
            }
            switch (currentUserType) {
                case Citizen:
                    citizenUser = em.find(Citizen.class, id);
                    foundID = citizenUser != null;
                    break;
                case Worker:
                    workerUser = em.find(Worker.class, id);
                    foundID = workerUser != null;
                    break;
                case ClinicManager:
                    clinicManagerUser = em.find(Clinic.class, id);
                    foundID = clinicManagerUser != null;
                    break;
                default:
                    foundID = true;
            }
            if (!foundID) System.out.println("User with such ID not found!");
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
        }

        em.close();
        System.out.println(currentUserType + " Logged in!");
    }

    void citizenMenu(Scanner scanner) {

    }

    void workerMenu(Scanner scanner) {

    }

    void clinicManagerMenu(Scanner scanner) {

    }

    void operationManagerMenu(Scanner scanner) {

    }

}
