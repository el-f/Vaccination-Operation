package project.model;

import project.model.entities.*;
import project.model.exceptions.DatabaseQueryException;
import project.model.exceptions.InvalidInputException;
import project.model.util.Pair;
import project.model.util.UtilMethods;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * A class for managing the entities in the database through hibernate and / or native sql queries.
 *
 * @author Elazar Fine  - github.com/Elfein7Night
 */
@SuppressWarnings("unchecked")
public class EntitiesManager {

    public static final String PERSISTENCE_UNIT_NAME = "default";

    private static EntityManagerFactory entityManagerFactory;
    private static EntitiesManager _instance;

    private EntitiesManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    /**
     * @return a singleton instance of the class.
     */
    public static EntitiesManager instance() {
        if (_instance == null) _instance = new EntitiesManager();
        return _instance;
    }

    /**
     * Before exiting the program it's best practice closing any connection to the database.
     */
    public static void close() {
        if (_instance != null) entityManagerFactory.close();
    }

    /* ********************************************************** */

    /**
     * Enum for easier handling of user types.
     */
    public enum UserType {
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

    /* ********************************************************** */

    /**
     * Get a {@link Citizen} entity from the database using its ID.
     *
     * @param id a citizen ID.
     * @return a {@link Citizen} entity instance.
     * @throws DatabaseQueryException if citizen not found.
     */
    public Citizen getCitizenByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Citizen citizen = em.find(Citizen.class, id);
        em.close();
        if (citizen == null) throw new DatabaseQueryException("Citizen with this ID was not found!");
        return citizen;
    }

    /**
     * Get a {@link Worker} entity from the database using its ID.
     *
     * @param id a worker ID.
     * @return a {@link Worker} entity instance.
     * @throws DatabaseQueryException if worker not found.
     */
    public Worker getWorkerByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker worker = em.find(Worker.class, id);
        em.close();
        if (worker == null) throw new DatabaseQueryException("Worker with this ID was not found!");
        return worker;
    }

    /**
     * Get a {@link Clinic} entity from the database using its ID.
     *
     * @param id a clinic ID.
     * @return a {@link Clinic} entity instance.
     * @throws DatabaseQueryException if clinic not found.
     */
    public Clinic getClinicByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic clinic = em.find(Clinic.class, id);
        em.close();
        if (clinic == null) throw new DatabaseQueryException("Clinic with this ID was not found!");
        return clinic;
    }

    /**
     * @return A list of all {@link Clinic} entities in the DB.
     */
    public List<Clinic> getAllClinics() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Clinic> ret = em.createQuery("Select c from Clinic c").getResultList();
        em.close();
        return ret;
    }

    /**
     * @return A list of all {@link Supply} entities in the DB.
     */
    public List<Supply> getAllSupplies() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Supply> ret = em.createQuery("Select s from Supply s").getResultList();
        em.close();
        return ret;
    }

    /**
     * @return A list of all {@link Citizen} entities in the DB.
     */
    public List<Citizen> getAllCitizens() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Citizen> ret = em.createQuery("Select c from Citizen c").getResultList();
        em.close();
        return ret;
    }

    /**
     * @return A list of all {@link Worker} entities in the DB.
     */
    public List<Worker> getAllWorkers() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Worker> ret = em.createQuery("Select w from Worker w").getResultList();
        em.close();
        return ret;
    }

    /**
     * @return A list of all {@link Vaccination} entities in the DB.
     */
    public List<Vaccination> getAllVaccinations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Vaccination> ret = em.createQuery("Select v from Vaccination v").getResultList();
        em.close();
        return ret;
    }

    /**
     * @return A list of all {@link Appointment} entities in the DB.
     */
    public List<Appointment> getAllAppointments() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Appointment> ret = em.createQuery("Select a from Appointment a").getResultList();
        em.close();
        return ret;
    }


    /*
     *******************************************
     User type - citizen
     *******************************************
     */

    /**
     * Create an appointment and add it to the DB.
     *
     * @param citizen   a {@link Citizen} entity - the user.
     * @param clinic    a {@link Clinic} chosen by the user.
     * @param timestamp the appointment date chosen by the user.
     * @throws DatabaseQueryException if adding the appointment to the DB failed.
     */
    public void createAppointment(Citizen citizen, Clinic clinic, Timestamp timestamp) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Appointment appointment = Appointment.AppointmentBuilder
                    .anAppointment()
                    .withCitizenId(citizen.getCitizenId())
                    .withClinicId(clinic.getClinicId())
                    .withDate(timestamp)
                    .build();

            em.persist(appointment);
            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp.toString());
    }

    /**
     * Cancel an appointment chosen by the user.
     *
     * @param appointmentId the appointment id - chosen by the user.
     * @throws DatabaseQueryException if removing the appointment from the DB failed.
     */
    public void cancelAppointment(int appointmentId) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Appointment appointment = em.find(Appointment.class, appointmentId);
            em.remove(appointment);

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp.toString());
    }

    /**
     * @param citizen a {@link Citizen} entity instance.
     * @return a list of pending appointments.
     */
    public List<Appointment> getPendingAppointmentForCitizen(Citizen citizen) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Citizen attached = em.find(Citizen.class, citizen.getCitizenId());
        return attached.getAppointmentsByCitizenId().stream()
                .filter(appointment -> appointment.getDate().after(UtilMethods.now()))
                .collect(Collectors.toList());
    }

    /**
     * @param citizen a {@link Citizen} entity instance.
     * @return a collection of past vaccinations the citizen had.
     */
    public Collection<Vaccination> getVaccinationsForCitizen(Citizen citizen) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Citizen attached = em.find(Citizen.class, citizen.getCitizenId());
        return attached.getVaccinationsByCitizenId();
    }

    /*
     *******************************************
     User type - worker
     *******************************************
     */

    /**
     * Log a Vaccination to the DB for a specific worker user and the citizen he vaccinated.
     *
     * @param worker    a {@link Worker} entity instance - the user.
     * @param citizenID the citizen id of the citizen the worker vaccinated.
     * @throws DatabaseQueryException if adding the Vaccination to the DB failed.
     */
    public void logVaccination(Worker worker, int citizenID) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Citizen citizen = em.find(Citizen.class, citizenID);
            if (citizen == null) throw new DatabaseQueryException("No citizen with such ID!");
            int phase = citizen.getPhasesComplete() + 1;
            Timestamp timestamp = UtilMethods.now();

            Worker attached = em.find(Worker.class, worker.getWorkerId());

            int unusedDoseBarcode = attached.getClinicByClinicId().getSuppliesByClinicId().stream()
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(Dose::isUnused)
                    .limit(1)
                    .map(Dose::getBarcode)
                    .findFirst()
                    .orElseThrow(() -> new DatabaseQueryException("Not enough doses in the worker's clinic"));

            Vaccination vaccination = Vaccination.VaccinationBuilder
                    .aVaccination()
                    .withWorkerId(attached.getWorkerId())
                    .withCitizenId(citizen.getCitizenId())
                    .withPhase(phase)
                    .withDate(timestamp)
                    .withDoseBarcode(unusedDoseBarcode)
                    .build();

            em.persist(vaccination);
            transaction.commit();
        } catch (DatabaseQueryException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            em.close();
            throw e;
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
    }

    /**
     * @param worker a {@link Worker} entity instance.
     * @return a list of pending appointments which the worker is designated to.
     */
    public List<Appointment> getPendingAppointmentsForWorker(Worker worker) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker attached = em.find(Worker.class, worker.getWorkerId());
        return attached.getAppointmentsByWorkerId().stream()
                .filter(appointment -> appointment.getDate().after(UtilMethods.now()))
                .collect(Collectors.toList());
    }

    /**
     * @param worker a {@link Worker} entity instance.
     * @return a list of vaccinations the worker administered.
     */
    public Collection<Vaccination> getVaccinationsByWorker(Worker worker) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker attached = em.find(Worker.class, worker.getWorkerId());
        return attached.getVaccinationsByWorkerId();
    }

    /*
     *******************************************
     User type - clinic manager
     *******************************************
     */

    /**
     * @param clinic a {@link Clinic} instance.
     * @return the clinic's supplies held in a collection.
     */
    public Collection<Supply> getSuppliesForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getSuppliesByClinicId();
    }

    /**
     * @param clinic a {@link Clinic} instance.
     * @return the clinic's designated appointments held in a collection.
     */
    public Collection<Appointment> getAppointmentsForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getAppointmentsByClinicId();
    }

    /**
     * @param clinic a {@link Clinic} instance.
     * @return the clinic's designated workers held in a collection.
     */
    public Collection<Worker> getWorkersForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getWorkersByClinicId();
    }

    /**
     * Add a supply of each vaccine type to the clinic, the supply size is marked by {@code amount}.
     *
     * @param clinicID a clinic ID marking the clinic we add the supplies to.
     * @param amount   number of doses from each vaccine type to add to the clinic.
     * @throws DatabaseQueryException if adding the supplies to the DB failed.
     * @throws InvalidInputException  if the amount of supplies requested is invalid.
     */
    public void addSuppliesToClinic(int clinicID, int amount) throws DatabaseQueryException, InvalidInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic clinic = em.find(Clinic.class, clinicID);
        if (clinic == null) throw new DatabaseQueryException("No clinic with such ID!");
        addSuppliesToClinic(clinic, amount);
        em.close();
    }

    /**
     * Add a supply of each vaccine type to the clinic, the supply size is marked by {@code amount}.
     *
     * @param clinic a {@link Clinic} instance marking the clinic we add the supplies to.
     * @param amount number of doses from each vaccine type to add to the clinic.
     * @throws DatabaseQueryException if adding the supplies to the DB failed.
     * @throws InvalidInputException  if the amount of supplies requested is invalid.
     */
    public void addSuppliesToClinic(Clinic clinic, int amount) throws DatabaseQueryException, InvalidInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        if (amount <= 0 || amount > 1000) throw new InvalidInputException("Dose amount must be between 1 and 1000!");

        try {
            transaction.begin();

            addSuppliesToClinic(em, clinic, amount);

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
    }

    /**
     * Remove from the clinic any supplies which had their expiration date pass.
     *
     * @param clinic a {@link Clinic} instance - marking the clinic we remove the supplies from.
     * @return how many expired supplies were removed from the clinic.
     * @throws DatabaseQueryException if removing the supplies from the DB failed.
     */
    public long removeExpiredSuppliesFromClinic(Clinic clinic) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;
        AtomicLong expiredAmount = new AtomicLong();

        try {
            transaction.begin();

            Clinic attached = em.find(Clinic.class, clinic.getClinicId());

            attached.getSuppliesByClinicId().stream()
                    .filter(supply -> supply.getExpiryDate().before(UtilMethods.now()))
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(Dose::isUnused)
                    .forEach(dose -> {
                        em.remove(dose);
                        expiredAmount.incrementAndGet();
                    });

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
        return expiredAmount.get();
    }

    /**
     * Assign a worker to an appointment. If the appointment already had a worker assigned to it, it simply replaces them.
     *
     * @param clinicManagerUser a {@link Clinic} instance marking the clinic for the appointment.
     * @param workerID          a {@link Worker} ID marking the worker which will administer the vaccine.
     * @param appointmentID     an {@link Appointment} ID marking which appointment we replace the worker in.
     * @throws DatabaseQueryException if replacing the worker for the appointment in the DB failed.
     */
    public void assignWorkerToAppointment(Clinic clinicManagerUser, int workerID, int appointmentID) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Worker worker = em.find(Worker.class, workerID);
            if (worker == null) throw new DatabaseQueryException("No worker with such ID!");
            if (worker.getClinicId() != clinicManagerUser.getClinicId()) {
                throw new DatabaseQueryException("This worker is not assigned to this clinic!");
            }

            Appointment appointment = em.find(Appointment.class, appointmentID);
            if (appointment == null) throw new DatabaseQueryException("No appointment with such ID!");
            if (appointment.getClinicId() != clinicManagerUser.getClinicId()) {
                throw new DatabaseQueryException("This appointment is not for this clinic");
            }

            appointment.setWorkerId(workerID);
            em.persist(appointment);

            transaction.commit();
        } catch (DatabaseQueryException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            em.close();
            throw e;
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
    }


    /*
     *******************************************
     User type - operation manager
     *******************************************
     */

    /**
     * Get a map holding clinics with more appointments than vaccine doses:
     * <br><br/>
     * <pre>
     *  | key: Clinic | value: ( vaccines amount,  appointments amount ) |
     *  |-------------|--------------------------------------------------|
     *  |     ---     |                      ---                         |
     * </pre>
     *
     * @return a Map instance holding low supply clinics.
     */
    public Map<Clinic, Pair<Long, Long>> getLowSupplyClinics() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Clinic> allClinics = em.createQuery("select c from Clinic c").getResultList();
        Map<Clinic, Pair<Long, Long>> lowSupplyClinicsTable = new HashMap<>();

        allClinics.forEach(clinic -> {
            long vaccineAmount = clinic.getSuppliesByClinicId().stream()
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(Dose::isUnused)
                    .count();

            long appointmentsAmount = clinic.getAppointmentsByClinicId().size();

            if (vaccineAmount < appointmentsAmount) {
                lowSupplyClinicsTable.put(clinic, new Pair<>(vaccineAmount, appointmentsAmount));
            }
        });

        return lowSupplyClinicsTable;
    }

    /**
     * Add a supply of each vaccine type to all the operation's clinics, the supply size is marked by {@code amount}.
     *
     * @param amount number of doses from each vaccine type to add to each clinic.
     * @throws DatabaseQueryException if adding the supplies to the DB failed.
     * @throws InvalidInputException  if the amount of supplies requested is invalid.
     */
    public void addSuppliesToAllClinics(int amount) throws DatabaseQueryException, InvalidInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        if (amount <= 0 || amount > 1000) throw new InvalidInputException("Dose amount must be between 1 and 1000!");

        try {
            transaction.begin();

            List<Clinic> clinics = em.createQuery("select c from Clinic c").getResultList();
            clinics.forEach(clinic -> addSuppliesToClinic(em, clinic, amount));

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
    }

    /**
     * A helper function to add supplies to a clinic.
     *
     * @param em     entity manager we use to query the DB.
     * @param clinic the {@link Clinic} instance marking the clinic we add the supplies to.
     * @param amount amount of doses in each supply added.
     */
    private void addSuppliesToClinic(EntityManager em, Clinic clinic, int amount) {
        List<Integer> vaccineTypes = em.createQuery("select v.vaccineId from Vaccine v").getResultList();
        Date expiryDate = Supply.DEFAULT_EXPIRATION();
        vaccineTypes.forEach(vaccineID -> {
            Supply supply = Supply.SupplyBuilder
                    .aSupply()
                    .withVaccineId(vaccineID)
                    .withClinicId(clinic.getClinicId())
                    .withExpiryDate(expiryDate)
                    .build();

            em.persist(supply);
        });

        List<Integer> newSuppliesIDs = em.createQuery(
                        "select s.supplyId " +
                                "from Supply s " +
                                "where s.dosesBySupplyId is empty " +
                                "and s.clinicId = :cid " +
                                "and s.expiryDate = :exp"
                )
                .setParameter("cid", clinic.getClinicId())
                .setParameter("exp", expiryDate)
                .getResultList();

        newSuppliesIDs.forEach(supplyID -> {
            for (int i = 0; i < amount; i++) {
                Dose dose = new Dose();
                dose.setSupplyId(supplyID);
                em.persist(dose);
            }
        });
    }

    /**
     * Authenticate an Operation Manager before login.
     *
     * @param auth authentication string.
     * @throws DatabaseQueryException if not authenticated.
     */
    public void authenticateOperationManager(String auth) throws DatabaseQueryException {
        // just a theoretical example of course, this placeholder implementation can be easily changed with an database check!
        if (!auth.equals("1234")) throw new DatabaseQueryException("Invalid Operation Manager Authentication!");

    }

    /**
     * Return a count of how many available (unused) doses a supply has.
     * @param supply the supply to check.
     * @return amount of available doses.
     */
    public long getUnusedDosesAmount(Supply supply) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Supply attached = em.find(Supply.class, supply.getSupplyId());
        return attached.getDosesBySupplyId().stream()
                .filter(Dose::isUnused)
                .count();
    }

}

