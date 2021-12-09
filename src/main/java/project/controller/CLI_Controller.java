package project.controller;

import project.model.EntitiesManager;
import project.model.EntitiesManager.UserType;
import project.model.exceptions.NamedException;
import project.model.util.Pair;
import project.model.entities.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * The controller for the Command Line Interface version of the program.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 * @see <a href="https://en.wikipedia.org/wiki/Command-line_interface">Command Line Interface - Wikipedia</a>
 */
public class CLI_Controller {

    private static EntitiesManager entitiesManager;
    private Citizen citizenUser;
    private Worker workerUser;
    private Clinic clinicManagerUser;
    private UserType currentUserType;

    private final static int EXIT_OPTION = 0;

    public CLI_Controller() {
        entitiesManager = EntitiesManager.instance();

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

        // close before exit
        EntitiesManager.close();
    }

    void initializeUser(Scanner scanner) {
        while (currentUserType == null) try {
            out.println("> Choose your user type:");
            for (int i = 0; i < UserType.values().length; i++) {
                out.println((i + 1) + ") " + UserType.values()[i]);
            }
            currentUserType = UserType.values()[scanner.nextInt() - 1];
        } catch (Exception e) {
            out.println("Invalid input!");
            scanner.nextLine();
        }

        boolean success = false;

        while (!success) try {
            switch (currentUserType) {
                case Citizen:
                    out.println("Enter citizen ID:");
                    citizenUser = entitiesManager.getCitizenByID(scanner.nextInt());
                    break;

                case Worker:
                    out.println("Enter worker ID:");
                    workerUser = entitiesManager.getWorkerByID(scanner.nextInt());
                    break;

                case ClinicManager:
                    out.println("Enter clinic ID:");
                    clinicManagerUser = entitiesManager.getClinicByID(scanner.nextInt());
                    break;

                default:
                    break;
            }
            success = true;
        } catch (NamedException e) {
            out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            out.println("Invalid input! (" + e.getClass().getSimpleName() + ")");
            scanner.nextLine();
        }

        out.println("Logged in as " + currentUserType);
    }

    void citizenMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            out.println("\n> Citizen Menu:");
            out.println("1) Create an appointment");
            out.println("2) Cancel an appointment");
            out.println("3) Show all appointments");
            out.println("4) Show administered vaccinations");
            out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    out.println("Goodbye!");
                    break;

                case 1:
                    out.println("> Select clinic:");
                    List<Clinic> clinics = entitiesManager.getAllClinics();
                    for (int i = 0; i < clinics.size(); i++) {
                        out.println((i + 1) + ") " + clinics.get(i));
                    }
                    Clinic chosenClinic = clinics.get(scanner.nextInt() - 1);
                    out.println("How many days from now? (1 - 14)");
                    entitiesManager.createAppointment(
                            citizenUser,
                            chosenClinic,
                            Appointment.createAppointmentDate(scanner.nextInt())
                    );
                    out.println("Appointment set!");
                    break;

                case 2:
                    out.println("> Select appointment:");
                    List<Appointment> pendingAppointments = entitiesManager.getPendingAppointmentForCitizen(citizenUser);
                    if (pendingAppointments.isEmpty()) out.println("No pending appointments!");
                    else {
                        for (int i = 0; i < pendingAppointments.size(); i++) {
                            out.println((i + 1) + ") " + pendingAppointments.get(i));
                        }
                        Appointment chosenAppointment = pendingAppointments.get(scanner.nextInt() - 1);
                        entitiesManager.cancelAppointment(chosenAppointment.getAppointmentId());
                        out.println("Appointment canceled!");
                    }
                    break;

                case 3:
                    List<Appointment> pending = entitiesManager.getPendingAppointmentForCitizen(citizenUser);
                    if (pending.isEmpty()) {
                        out.println("No pending appointments!");
                    } else {
                        pending.forEach(out::println);
                    }
                    break;

                case 4:
                    Collection<Vaccination> administered = entitiesManager.getVaccinationsForCitizen(citizenUser);
                    if (administered.isEmpty()) {
                        out.println("No vaccination records!");
                    } else {
                        administered.forEach(out::println);
                    }
                    break;

                default:
                    out.println("Invalid choice For choice Range [0 - 4]");
                    break;
            }
        } catch (NamedException e) {
            out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void workerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            out.println("\n> Worker Menu:");
            out.println("1) Log a vaccination");
            out.println("2) Show all appointments");
            out.println("3) Show all administered vaccinations");
            out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    out.println("Goodbye!");
                    break;

                case 1:
                    out.println("Enter vaccinated citizen ID:");
                    entitiesManager.logVaccination(workerUser, scanner.nextInt());
                    out.println("Vaccination logged!");
                    break;

                case 2:
                    List<Appointment> pending = entitiesManager.getPendingAppointmentsForWorker(workerUser);
                    if (pending.isEmpty()) {
                        out.println("No pending appointments!");
                    } else {
                        pending.forEach(out::println);
                    }
                    break;

                case 3:
                    Collection<Vaccination> vaccinations = entitiesManager.getVaccinationsByWorker(workerUser);
                    if (vaccinations.isEmpty()) {
                        out.println("No administered vaccinations for worker!");
                    } else {
                        vaccinations.forEach(out::println);
                    }
                    break;

                default:
                    out.println("Invalid choice For choice Range [0 - 3]");
                    break;
            }
        } catch (NamedException e) {
            out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void clinicManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            out.println("\n> Clinic Manager Menu:");
            out.println("1) Show all clinic supplies");
            out.println("2) Show all clinic workers");
            out.println("3) Show all clinic appointments");
            out.println("4) Add supplies to clinic");
            out.println("5) Remove expired supplies from clinic");
            out.println("6) Replace worker in appointment");
            out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    out.println("Goodbye!");
                    break;

                case 1:
                    Collection<Supply> supplies = entitiesManager.getSuppliesForClinic(clinicManagerUser);
                    if (supplies.isEmpty()) {
                        out.println("No supplies for clinic!");
                    } else {
                        supplies.forEach(out::println);
                    }
                    break;

                case 2:
                    Collection<Worker> workers = entitiesManager.getWorkersForClinic(clinicManagerUser);
                    if (workers.isEmpty()) {
                        out.println("No workers in clinic!");
                    } else {
                        workers.forEach(out::println);
                    }
                    break;

                case 3:
                    Collection<Appointment> appointments = entitiesManager.getAppointmentsForClinic(clinicManagerUser);
                    if (appointments.isEmpty()) {
                        out.println("No appointments for clinic!");
                    } else {
                        appointments.forEach(out::println);
                    }
                    break;

                case 4:
                    out.println("Please enter how many vaccine doses from each vaccine type you wish to add:");
                    entitiesManager.addSuppliesToClinic(clinicManagerUser, scanner.nextInt());
                    out.println("Supplies added!");
                    break;

                case 5:
                    long expiredAmount = entitiesManager.removeExpiredSuppliesFromClinic(clinicManagerUser);
                    out.println("Removed " + expiredAmount + " expired doses from the clinic");
                    break;

                case 6:
                    int workerID, appointmentID;
                    out.println("Enter the worker ID:");
                    workerID = scanner.nextInt();
                    out.println("Enter the appointment ID:");
                    appointmentID = scanner.nextInt();
                    entitiesManager.assignWorkerToAppointment(clinicManagerUser, workerID, appointmentID);
                    out.println("Worker assigned to appointment!");
                    break;

                default:
                    out.println("Invalid choice For choice Range [0 - 6]");
                    break;
            }
        } catch (NamedException e) {
            out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

    void operationManagerMenu(Scanner scanner) {
        int choice = -1;
        while (choice != EXIT_OPTION) try {
            out.println("\n> Operation Manager Menu:");
            out.println("1) Show all clinics");
            out.println("2) Show all supplies");
            out.println("3) Show all citizens");
            out.println("4) Show all workers");
            out.println("5) Show all vaccinations");
            out.println("6) Show all appointments");
            out.println("7) Show clinics with low supplies");
            out.println("8) Add supplies to a clinic");
            out.println("9) Add supplies to all clinics");
            out.println("\n0) To exit");
            choice = scanner.nextInt();
            switch (choice) {
                case EXIT_OPTION:
                    out.println("Goodbye!");
                    break;

                case 1:
                    entitiesManager.getAllClinics().forEach(out::println);
                    break;

                case 2:
                    entitiesManager.getAllSupplies().forEach(out::println);
                    break;

                case 3:
                    entitiesManager.getAllCitizens().forEach(out::println);
                    break;

                case 4:
                    entitiesManager.getAllWorkers().forEach(out::println);
                    break;

                case 5:
                    entitiesManager.getAllVaccinations().forEach(out::println);
                    break;

                case 6:
                    entitiesManager.getAllAppointments().forEach(out::println);
                    break;

                case 7:
                    out.println("Processing...");
                    Map<Clinic, Pair<Long, Long>> lowSupplyClinicsTable = entitiesManager.getLowSupplyClinics();
                    if (lowSupplyClinicsTable.isEmpty()) {
                        out.println("All clinics are well supplied!");
                    } else {
                        out.println("Low supply clinics:");
                        lowSupplyClinicsTable.forEach((clinic, vaccinesAppointmentsPair) -> out.println(
                                "clinic ID = " + clinic.getClinicId() +
                                        " | clinic Name = " + clinic.getClinicName() +
                                        " | vaccines Total = " + vaccinesAppointmentsPair.getFirst() +
                                        " | appointments = " + vaccinesAppointmentsPair.getSecond()
                        ));
                    }
                    break;

                case 8:
                    out.println("Enter clinic ID to add doses to:");
                    int clinicID = scanner.nextInt();
                    out.println("How many doses would you like to add from each vaccine type? (1 - 1000)");
                    int add = scanner.nextInt();
                    entitiesManager.addSuppliesToClinic(clinicID, add);
                    out.println("Supplies added!");
                    break;

                case 9:
                    out.println("How many doses would you like to add from each vaccine type? (1 - 1000)");
                    entitiesManager.addSuppliesToAllClinics(scanner.nextInt());
                    out.println("Supplies added!");
                    break;

                default:
                    out.println("Invalid choice For choice Range [0 - 9]");
                    break;
            }
        } catch (NamedException e) {
            out.println(e.getFullMessage());
            scanner.nextLine();
        } catch (Exception e) {
            out.println("Error! " + e.getClass().getSimpleName());
            scanner.nextLine(); //clean buffer
        }
    }

}
