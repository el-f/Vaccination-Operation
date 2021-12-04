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

    final static int EXIT_OPTION = 0;

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
            System.out.println("Invalid input! (" + e.getClass().getSimpleName() + ")");
            scanner.nextLine();
        }

        System.out.println(currentUserType + " Logged in!");
    }

    void citizenMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("> Citizen Menu:");
            System.out.println("1) Create an appointment");
            System.out.println("2) Cancel an appointment");
            System.out.println("3) Show all appointments");
            System.out.println("4) Show administered vaccinations");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;
                case 1:

                    break;
                case 2:

                    break;

                case 3:

                    break;

                case 4:
                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-3]");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void workerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("> Worker Menu:");
            System.out.println("1) Log a vaccination");
            System.out.println("2) Show all appointments");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;
                case 1:

                    break;
                case 2:

                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-2]");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void clinicManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("> Clinic Manager Menu:");
            System.out.println("1) Show all supplies");
            System.out.println("2) Show all workers");
            System.out.println("3) Show all appointments");
            System.out.println("4) Add supplies to clinic");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;
                case 1:

                    break;
                case 2:

                    break;

                case 3:

                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-3]");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void operationManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("> Operation Manager Menu:");
            System.out.println("1) ---");
            System.out.println("2) ---");
            System.out.println("3) ---");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;
                case 1:

                    break;
                case 2:

                    break;

                case 3:

                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-3]");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

}
