package project.controller;

import project.model.EntitiesManager;
import project.model.entities.Citizen;
import project.model.entities.Clinic;
import project.model.entities.Worker;
import project.model.exceptions.DatabaseQueryException;

import javax.persistence.EntityManagerFactory;
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

        boolean success = false;

        while (!success) try {
            switch (currentUserType) {
                case Citizen:
                    System.out.println("Enter citizen ID:");
                    citizenUser = entitiesManager.getCitizenByID(scanner.nextInt());
                    break;
                case Worker:
                    System.out.println("Enter worker ID:");
                    workerUser = entitiesManager.getWorkerByID(scanner.nextInt());
                    break;
                case ClinicManager:
                    System.out.println("Enter clinic ID:");
                    clinicManagerUser = entitiesManager.getClinicByID(scanner.nextInt());
                    break;
                default:
                    break;
            }
            success = true;
        } catch (DatabaseQueryException e) {
            System.out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();
        }

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
