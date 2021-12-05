package project.model;

import project.model.entities.*;
import project.model.exceptions.DatabaseQueryException;
import project.model.exceptions.InvalidInputException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * A class for managing the entities in the database through hibernate and / or native sql queries.
 */
@SuppressWarnings("unchecked")
public class EntitiesManager {

    public static final int ERROR_CODE = -1;

    private final EntityManagerFactory entityManagerFactory;

    public EntitiesManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Citizen getCitizenByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Citizen citizen = em.find(Citizen.class, id);
        em.close();
        if (citizen == null) throw new DatabaseQueryException("Citizen with this ID was not found!");
        return citizen;
    }

    public Worker getWorkerByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker worker = em.find(Worker.class, id);
        em.close();
        if (worker == null) throw new DatabaseQueryException("Worker with this ID was not found!");
        return worker;
    }

    public Clinic getClinicByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic clinic = em.find(Clinic.class, id);
        em.close();
        if (clinic == null) throw new DatabaseQueryException("Clinic with this ID was not found!");
        return clinic;
    }

    public List<Clinic> getAllClinics() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Clinic> ret = em.createQuery("Select c from Clinic c").getResultList();
        em.close();
        return ret;
    }

    /*
     *******************************************
     User type - citizen
     *******************************************
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

    public List<Appointment> getPendingAppointmentForCitizen(Citizen citizen) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Citizen attached = em.find(Citizen.class, citizen.getCitizenId());
        return attached.getAppointmentsByCitizenId().stream()
                .filter(appointment -> appointment.getDate().after(Timestamp.valueOf(LocalDateTime.now())))
                .collect(Collectors.toList());
    }

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

    public void logVaccination(Worker worker, int citizenID) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Citizen citizen = em.find(Citizen.class, citizenID);
            if (citizen == null) throw new DatabaseQueryException("No citizen with such ID!");
            int phase = citizen.getPhasesComplete() + 1;
            Timestamp timestamp = Utils.now();

            Worker attached = em.find(Worker.class, worker.getWorkerId());

            int unusedDoseBarcode = attached.getClinicByClinicId().getSuppliesByClinicId().stream()
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(dose -> dose.getVaccinationsByBarcode().isEmpty())
                    .limit(1)
                    .map(Dose::getBarcode)
                    .findFirst()
                    .orElse(ERROR_CODE);

            if (unusedDoseBarcode == ERROR_CODE)
                throw new DatabaseQueryException("Not enough doses in the worker's clinic");

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

    public List<Appointment> getPendingAppointmentsForWorker(Worker worker) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker attached = em.find(Worker.class, worker.getWorkerId());
        return attached.getAppointmentsByWorkerId().stream()
                .filter(appointment -> appointment.getDate().after(Utils.now()))
                .collect(Collectors.toList());
    }

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

    public Collection<Supply> getSuppliesForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getSuppliesByClinicId();
    }

    public Collection<Appointment> getAppointmentsForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getAppointmentsByClinicId();
    }

    public Collection<Worker> getWorkersForClinic(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic attached = em.find(Clinic.class, clinic.getClinicId());
        return attached.getWorkersByClinicId();
    }

    public void addSuppliesToClinic(Clinic clinic, int amount) throws DatabaseQueryException, InvalidInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        if (amount <= 0 || amount > 1000) throw new InvalidInputException("Dose amount must be between 1 and 1000!");

        try {
            transaction.begin();

            List<Integer> vaccineTypes = em.createQuery("select v.vaccineId from Vaccine v").getResultList();
            vaccineTypes.forEach(vaccineID -> {
                Supply supply = Supply.SupplyBuilder
                        .aSupply()
                        .withVaccineId(vaccineID)
                        .withClinicId(clinic.getClinicId())
                        .withExpiryDate(Supply.DEFAULT_EXPIRATION())
                        .build();

                em.persist(supply);
            });

            List<Integer> newSuppliesIDs = em.createQuery("select s.supplyId " +
                                                                  "from Supply s " +
                                                                  "where s.dosesBySupplyId is empty").getResultList();
            newSuppliesIDs.forEach(supplyID -> {
                for (int i = 0; i < amount; i++) {
                    Dose dose = new Dose();
                    dose.setSupplyId(supplyID);
                    em.persist(dose);
                }
            });

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            exp = e;
        } finally {
            em.close(); // Close EntityManager
        }
        if (exp != null) throw new DatabaseQueryException(exp);
    }

    public long removeExpiredSupplies(Clinic clinic) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;
        AtomicLong expiredAmount = new AtomicLong();

        try {
            transaction.begin();

            Clinic attached = em.find(Clinic.class, clinic.getClinicId());

            attached.getSuppliesByClinicId().stream()
                    .filter(supply -> supply.getExpiryDate().before(Utils.now()))
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(dose -> dose.getVaccinationsByBarcode().isEmpty())
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

    public void replaceWorkerToAppointment(Clinic clinicManagerUser, int workerID, int appointmentID) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Exception exp = null;

        try {
            transaction.begin();

            Worker worker = em.find(Worker.class, workerID);
            if (worker == null) throw new DatabaseQueryException("No worker such ID!");
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
     * Get a table showing clinics with more appointments than vaccine doses:
     * <br><br/>
     * <pre>
     *  | clinic_id | clinic_name | vaccines_total | appointments |
     *  |-----------|-------------|----------------|--------------|
     *  |    ---    |     ---     |       ---      |      ---     |
     * </pre>
     *
     * @return the result table shown above
     */
    public List<Object[]> getClinicsWithNotEnoughDoses() throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        List<Object[]> lowSupplyClinics = Collections.emptyList();
        Exception exp = null;

        try {
            transaction.begin();

            lowSupplyClinics = em.createNativeQuery(
                    "SELECT *\n" +
                            "FROM (SELECT clinic.clinic_id,\n" +
                            "             clinic.clinic_name,\n" +
                            "             (SELECT COUNT(barcode)\n" +
                            "              FROM dose\n" +
                            "              WHERE dose.supply_id = supply.supply_id) AS vaccines_total,\n" +
                            "             COUNT(appointment_id)                     AS appointments\n" +
                            "      FROM (clinic JOIN appointment ap ON clinic.clinic_id = ap.clinic_id)\n" +
                            "               JOIN supply ON supply.clinic_id = clinic.clinic_id\n" +
                            "      GROUP BY clinic.clinic_id) AS all_clinics\n" +
                            "WHERE appointments > vaccines_total\n" +
                            "UNION\n" +
                            "(SELECT clinic_id,\n" +
                            "        clinic_name,\n" +
                            "        (SELECT COUNT(clinic_id) FROM supply WHERE clinic.clinic_id = supply.clinic_id),\n" +
                            "        (SELECT COUNT(clinic_id) FROM appointment WHERE clinic.clinic_id = appointment.clinic_id)\n" +
                            " FROM clinic\n" +
                            " WHERE clinic_id NOT IN (SELECT clinic_id FROM supply)\n" +
                            "   AND clinic_id IN (SELECT clinic_id AS cid FROM appointment)\n" +
                            ");"
            ).getResultList();

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
        return lowSupplyClinics;
    }


}

