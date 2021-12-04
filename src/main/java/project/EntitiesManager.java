package project;

import project.entities.*;
import project.exceptions.DatabaseQueryException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * A class for managing the entities in the database through hibernate and / or native sql queries.
 */
public class EntitiesManager {

    public static final int ERROR_CODE = -1;

    private final EntityManagerFactory entityManagerFactory;

    public EntitiesManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    /*
     *******************************************
     User type - citizen
     ******************************************
     */

    void createAppointment(Citizen citizen, Clinic clinic, Timestamp timestamp) {
        Appointment appointment = Appointment.AppointmentBuilder
                .anAppointment()
                .withCitizenId(citizen.getCitizenId())
                .withClinicId(clinic.getClinicId())
                .withDate(timestamp)
                .build();

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
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

    void cancelAppointment(int appointmentId) {
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

    Collection<Appointment> getAppointmentListForCitizen(Citizen citizen) {
        return citizen.getAppointmentsByCitizenId();
    }


    /*
     *******************************************
     User type - worker
     ******************************************
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

    void logVaccination(Worker worker, Citizen citizen, Timestamp timestamp, int phase) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            int unusedDoseBarcode = worker.getClinicByClinicId().getSuppliesByClinicId().stream()
                    .map(Supply::getDosesBySupplyId)
                    .flatMap(Collection::stream)
                    .map(Dose::getBarcode)
                    .findFirst()
                    .orElse(ERROR_CODE);

//            List<?> unusedDoses = em
//                    .createNativeQuery(getUnusedDoseForWorker(worker.getWorkerId()))
//                    .getResultList();
//
//            if (unusedDoses.isEmpty()) throw new DatabaseQueryException("Not enough doses in the worker's clinic");
            if (unusedDoseBarcode == ERROR_CODE) throw new DatabaseQueryException("Not enough doses in the worker's clinic");

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

}
