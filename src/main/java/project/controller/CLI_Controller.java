package project.controller;

import project.model.EntitiesManager;
import project.model.util.Pair;
import project.model.entities.*;
import project.model.exceptions.DatabaseQueryException;
import project.model.exceptions.InvalidInputException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

enum UserType {
    Citizen,
    Worker,
    ClinicManager {
        @Override
        public String toString() {
            return "Clinic Manager";
        }
    },
    OperationManager {
        @Override
        public String toString() {
            return "Operation Manager";
        }
    }
}

/**
 * The controller for the Command Line Interface version of the program.
 *
 * @author Elazar Fine https://github.com/Elfein7Night
 * @see <a href="https://en.wikipedia.org/wiki/Command-line_interface">Command Line Interface - Wikipedia</a>
 */
public class CLI_Controller {

    EntityManagerFactory entityManagerFactory;
    EntitiesManager entitiesManager;
    Citizen citizenUser;
    Worker workerUser;
    Clinic clinicManagerUser;
    UserType currentUserType;

    private final static int EXIT_OPTION = 0;

    public CLI_Controller() {
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

        // exit
        entityManagerFactory.close();
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

        System.out.println("Logged in as " + currentUserType);
    }

    void citizenMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("\n> Citizen Menu:");
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
                    System.out.println("> Select clinic:");
                    List<Clinic> clinics = entitiesManager.getAllClinics();
                    for (int i = 0; i < clinics.size(); i++) {
                        System.out.println((i + 1) + ") " + clinics.get(i));
                    }
                    Clinic chosenClinic = clinics.get(scanner.nextInt() - 1);
                    System.out.println("How many days from now? (1 - 14)");
                    entitiesManager.createAppointment(
                            citizenUser,
                            chosenClinic,
                            Appointment.createAppointmentDate(scanner.nextInt())
                    );
                    System.out.println("Appointment set!");
                    break;

                case 2:
                    System.out.println("> Select appointment:");
                    List<Appointment> pendingAppointments = entitiesManager.getPendingAppointmentForCitizen(citizenUser);
                    if (pendingAppointments.isEmpty()) System.out.println("No pending appointments!");
                    else {
                        for (int i = 0; i < pendingAppointments.size(); i++) {
                            System.out.println((i + 1) + ") " + pendingAppointments.get(i));
                        }
                        Appointment chosenAppointment = pendingAppointments.get(scanner.nextInt() - 1);
                        entitiesManager.cancelAppointment(chosenAppointment.getAppointmentId());
                        System.out.println("Appointment canceled!");
                    }
                    break;

                case 3:
                    List<Appointment> pending = entitiesManager.getPendingAppointmentForCitizen(citizenUser);
                    if (pending.isEmpty()) {
                        System.out.println("No pending appointments!");
                    } else {
                        pending.forEach(System.out::println);
                    }
                    break;

                case 4:
                    Collection<Vaccination> administered = entitiesManager.getVaccinationsForCitizen(citizenUser);
                    if (administered.isEmpty()) {
                        System.out.println("No vaccination records!");
                    } else {
                        administered.forEach(System.out::println);
                    }
                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0 - 4]");
                    break;
            }
        } catch (DatabaseQueryException e) {
            System.out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void workerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("\n> Worker Menu:");
            System.out.println("1) Log a vaccination");
            System.out.println("2) Show all appointments");
            System.out.println("3) Show all administered vaccinations");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;

                case 1:
                    System.out.println("Enter vaccinated citizen ID:");
                    entitiesManager.logVaccination(workerUser, scanner.nextInt());
                    System.out.println("Vaccination logged!");
                    break;

                case 2:
                    List<Appointment> pending = entitiesManager.getPendingAppointmentsForWorker(workerUser);
                    if (pending.isEmpty()) {
                        System.out.println("No pending appointments!");
                    } else {
                        pending.forEach(System.out::println);
                    }
                    break;

                case 3:
                    Collection<Vaccination> vaccinations = entitiesManager.getVaccinationsByWorker(workerUser);
                    if (vaccinations.isEmpty()) {
                        System.out.println("No administered vaccinations for worker!");
                    } else {
                        vaccinations.forEach(System.out::println);
                    }
                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-3]");
                    break;
            }
        } catch (DatabaseQueryException e) {
            System.out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void clinicManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("\n> Clinic Manager Menu:");
            System.out.println("1) Show all clinic supplies");
            System.out.println("2) Show all clinic workers");
            System.out.println("3) Show all clinic appointments");
            System.out.println("4) Add supplies to clinic");
            System.out.println("5) Remove expired supplies from clinic");
            System.out.println("6) Add worker to appointment");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;

                case 1:
                    Collection<Supply> supplies = entitiesManager.getSuppliesForClinic(clinicManagerUser);
                    if (supplies.isEmpty()) {
                        System.out.println("No supplies for clinic!");
                    } else {
                        supplies.forEach(System.out::println);
                    }
                    break;

                case 2:
                    Collection<Worker> workers = entitiesManager.getWorkersForClinic(clinicManagerUser);
                    if (workers.isEmpty()) {
                        System.out.println("No workers in clinic!");
                    } else {
                        workers.forEach(System.out::println);
                    }
                    break;

                case 3:
                    Collection<Appointment> appointments = entitiesManager.getAppointmentsForClinic(clinicManagerUser);
                    if (appointments.isEmpty()) {
                        System.out.println("No appointments for clinic!");
                    } else {
                        appointments.forEach(System.out::println);
                    }
                    break;

                case 4:
                    System.out.println("Please enter how many vaccine doses from each vaccine type you wish to add:");
                    entitiesManager.addSuppliesToClinic(clinicManagerUser, scanner.nextInt());
                    System.out.println("Supplies added!");
                    break;

                case 5:
                    long expiredAmount = entitiesManager.removeExpiredSuppliesFromClinic(clinicManagerUser);
                    System.out.println("Removed " + expiredAmount + " expired doses from the clinic");
                    break;

                case 6:
                    int workerID, appointmentID;
                    System.out.println("Enter the worker ID:");
                    workerID = scanner.nextInt();
                    System.out.println("Enter the appointment ID:");
                    appointmentID = scanner.nextInt();
                    entitiesManager.replaceWorkerInAppointment(clinicManagerUser, workerID, appointmentID);
                    System.out.println("Worker assigned to appointment!");
                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-6]");
                    break;
            }
        } catch (DatabaseQueryException | InvalidInputException e) {
            System.out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void operationManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            System.out.println("\n> Operation Manager Menu:");
            System.out.println("1) Show all clinics");
            System.out.println("2) Show all supplies");
            System.out.println("3) Show all citizens");
            System.out.println("4) Show all workers");
            System.out.println("5) Show all vaccinations");
            System.out.println("6) Show all appointments");
            System.out.println("7) Show clinics with low supplies");
            System.out.println("8) Add supplies to a clinic");
            System.out.println("9) Add supplies to all clinics");
            System.out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    System.out.println("Goodbye!");
                    break;

                case 1:
                    entitiesManager.getAllClinics().forEach(System.out::println);
                    break;

                case 2:
                    entitiesManager.getAllSupplies().forEach(System.out::println);
                    break;

                case 3:
                    entitiesManager.getAllCitizens().forEach(System.out::println);
                    break;

                case 4:
                    entitiesManager.getAllWorkers().forEach(System.out::println);
                    break;

                case 5:
                    entitiesManager.getAllVaccinations().forEach(System.out::println);
                    break;

                case 6:
                    entitiesManager.getAllAppointments().forEach(System.out::println);
                    break;

                case 7:
                    System.out.println("Processing...");
                    Map<Clinic, Pair<Long, Long>> lowSupplyClinicsTable = entitiesManager.getLowSupplyClinics();
                    lowSupplyClinicsTable.forEach((clinic, vaccinesAppointmentsPair) -> System.out.println(
                            "clinic ID = " + clinic.getClinicId() +
                                    " | clinic Name = " + clinic.getClinicName() +
                                    " | vaccines Total = " + vaccinesAppointmentsPair.getFirst() +
                                    " | appointments = " + vaccinesAppointmentsPair.getSecond()
                    ));
                    break;

                case 8:
                    System.out.println("Enter clinic ID to add doses to:");
                    int clinicID = scanner.nextInt();
                    System.out.println("How many doses would you like to add from each vaccine type?");
                    int add = scanner.nextInt();
                    entitiesManager.addSuppliesToClinic(clinicID, add);
                    System.out.println("Supplies added!");
                    break;

                case 9:
                    System.out.println("How many doses would you like to add from each vaccine type?");
                    entitiesManager.addSuppliesToAllClinics(scanner.nextInt());
                    System.out.println("Supplies added!");
                    break;

                default:
                    System.out.println("Invalid choice For choice Range [0-9]");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

}
