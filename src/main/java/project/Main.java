package project;

import project.entities.Vaccine;

import javax.persistence.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
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