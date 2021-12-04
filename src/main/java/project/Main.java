package project;

import project.entities.Citizen;
import project.entities.Vaccine;

import javax.persistence.*;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class Main {

    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    public static void main(String[] args) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();


        try {
            transaction.begin();

//            Vaccine v3 = new Vaccine();
//            v3.setVaccineId(3);
//            v3.setVaccineName("em");
//            v3.setCompany("Elfein Medical");
//            entityManager.persist(v3);


            entityManager.createNativeQuery("select * from vaccine", Vaccine.class).getResultStream()
                    .forEach(System.out::println);

            entityManager.createQuery("select v.suppliesByVaccineId from Vaccine v where v.vaccineId = 1")
                    .getResultList().forEach(System.out::println);

            entityManager.createQuery("select v.vaccineId, v.vaccineName from Vaccine v")
                    .getResultList().forEach(o -> System.out.println(Arrays.toString((Object[]) o)));

            entityManager.createQuery("select v from Vaccination v")
                    .getResultList().forEach(System.out::println);


            entityManager.createQuery("select c from Citizen c")
                    .getResultList().forEach(System.out::println);


            System.out.println(entityManager.createNativeQuery("SELECT barcode\n" +
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
                                                                       "          WHERE w.worker_id = 111111111\n" +
                                                                       "            AND c.clinic_id = w.clinic_id) AS cid,\n" +
                                                                       "         supply,\n" +
                                                                       "         dose\n" +
                                                                       "    WHERE cid.clinic_id = supply.clinic_id\n" +
                                                                       "      AND dose.supply_id = supply.supply_id\n" +
                                                                       ")\n" +
                                                                       "LIMIT 1;").getResultList());

            Citizen citizen = entityManager.find(Citizen.class, 854089088);
            System.out.println(citizen.getAppointmentsByCitizenId());


//            TypedQuery<Employee> empByDeptQuery = entityManager.createNamedQuery("Employee.byDept", Employee.class);
//            empByDeptQuery.setParameter(1, "Java Advocacy");
//            for (Employee employee : empByDeptQuery.getResultList()) {
//                System.out.println(employee);
//            }
//
//            Query countEmpByDept = entityManager.createNativeQuery("SELECT COUNT(*) FROM Employee INNER JOIN Department D on Employee.department_id = D.id WHERE D.name=:deptName");
//            countEmpByDept.setParameter("deptName", "Java Advocacy");
//            System.out.println("There are " + countEmpByDept.getSingleResult() + " Java Advocates.");

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}