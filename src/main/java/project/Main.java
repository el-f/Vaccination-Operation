package project;

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


            entityManager.createQuery("select v from Vaccination v")
                    .getResultList().forEach(System.out::println);


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