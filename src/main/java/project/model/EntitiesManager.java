package project.model;

import project.model.entities.*;
import project.model.exceptions.DatabaseQueryException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        Citizen citizen = em.find(Citizen.class,id);
        if (citizen == null) throw new DatabaseQueryException("Citizen with this ID was not found!");
        return citizen;
    }

    public Worker getWorkerByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Worker worker = em.find(Worker.class,id);
        if (worker == null) throw new DatabaseQueryException("Worker with this ID was not found!");
        return worker;
    }

    public Clinic getClinicByID(int id) throws DatabaseQueryException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Clinic clinic = em.find(Clinic.class,id);
        if (clinic == null) throw new DatabaseQueryException("Clinic with this ID was not found!");
        return clinic;
    }


    /*
     *******************************************
     User type - citizen
     *******************************************
     */

    public void createAppointment(Citizen citizen, Clinic clinic, Timestamp timestamp) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

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
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public void cancelAppointment(int appointmentId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

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
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public List<Appointment> getPendingAppointmentForCitizen(Citizen citizen) {
        return citizen.getAppointmentsByCitizenId().stream()
                .filter(appointment -> appointment.getDate().after(Timestamp.valueOf(LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    public Collection<Vaccination> getVaccinationsForCitizen(Citizen citizen) {
        return citizen.getVaccinationsByCitizenId();
    }

    /*
     *******************************************
     User type - worker
     *******************************************
     */

    /**
     * A mySQL query to get all unused doses in the same clinic as a worker.
     *
     * @param workerId the worker id.
     * @return The query as a string.
     */
    private String getUnusedDoseForWorker(int workerId) {
        return "SELECT barcode\n" +
                "FROM dose\n" +
                "WHERE barcode NOT IN (\n" +
                "    SELECT dose_barcode\n" +
                "    FROM vaccination\n" +
                ")\n" +
                "  AND barcode IN (\n" +
                "    SELECT barcode\n" +
                "    FROM (SELECT c.clinic_id\n" +
                "          FROM clinic c,\n" +
                "               worker w\n" +
                "          WHERE w.worker_id = " + workerId + "\n" +
                "            AND c.clinic_id = w.clinic_id) AS cid,\n" +
                "         supply,\n" +
                "         dose\n" +
                "    WHERE cid.clinic_id = supply.clinic_id\n" +
                "      AND dose.supply_id = supply.supply_id\n" +
                ")\n" +
                "LIMIT 1;";
    }

    public void logVaccination(Worker worker, Citizen citizen, Timestamp timestamp, int phase) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            int unusedDoseBarcode = worker.getClinicByClinicId().getSuppliesByClinicId().stream()
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .filter(dose -> dose.getVaccinationsByBarcode().isEmpty())
                    .limit(1)
                    .map(Dose::getBarcode)
                    .findFirst()
                    .orElse(ERROR_CODE);

//            List<?> unusedDoses = em
//                    .createNativeQuery(getUnusedDoseForWorker(worker.getWorkerId()))
//                    .getResultList();
//
//            if (unusedDoses.isEmpty()) throw new DatabaseQueryException("Not enough doses in the worker's clinic");
            if (unusedDoseBarcode == ERROR_CODE)
                throw new DatabaseQueryException("Not enough doses in the worker's clinic");

            Vaccination vaccination = Vaccination.VaccinationBuilder
                    .aVaccination()
                    .withWorkerId(worker.getWorkerId())
                    .withCitizenId(citizen.getCitizenId())
                    .withPhase(phase)
                    .withDate(timestamp)
//                    .withDoseBarcode((int) unusedDoses.get(0))
                    .withDoseBarcode(unusedDoseBarcode)
                    .build();

            em.persist(vaccination);
            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public List<Appointment> getPendingAppointmentsForWorker(Worker worker) {
        return worker.getAppointmentsByWorkerId().stream()
                .filter(appointment -> appointment.getDate().after(Utils.now()))
                .collect(Collectors.toList());
    }

    /*
     *******************************************
     User type - clinic manager
     *******************************************
     */

    Collection<Appointment> getAppointmentsForClinic(Clinic clinic) {
        return clinic.getAppointmentsByClinicId();
    }

    Collection<Worker> getWorkersForClinic(Clinic clinic) {
        return clinic.getWorkersByClinicId();
    }

    void addSuppliesToClinic(Clinic clinic, int amount) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            List<Integer> vaccineTypes = em.createQuery("select Vaccine.vaccineId from Vaccine").getResultList();
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
        } finally {
            em.close(); // Close EntityManager
        }
    }

    void removeExpiredSupplies(Clinic clinic) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            clinic.getSuppliesByClinicId().stream()
                    .filter(supply -> supply.getExpiryDate().before(Utils.now()))
                    .forEach(em::remove);

            transaction.commit();
        } catch (Exception e) {
            // If there is an exception rollback changes
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }
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
    List<Object[]> getClinicsWithNotEnoughDoses() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        List<Object[]> lowSupplyClinics = Collections.emptyList();

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
            e.printStackTrace();
        } finally {
            em.close(); // Close EntityManager
        }

        return lowSupplyClinics;
    }

}

