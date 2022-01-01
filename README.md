# Vaccination-Operation
#### 'Human–computer interaction' Course Project

#### Description
The system oversees a vaccination operation in a city, managing data about its citizens, medical workers, and clinics and their vaccine supplies.
The purpose of the system is to facilitate vaccination appointments for citizens of varying risk groups, track the vaccination phase for each citizen, and make sure each clinic is adequately staffed and has enough vaccine supplies.

#### The system has 4 front ends:
1) for citizens - to make appointments and track their status through an app and a website.
2) for clinic managers - to overlook their clinic and get relevant information to make informed logistical and managerial decisions.
3) for operation managers - to overlook the operation and get relevant information to make informed logistical and managerial decisions.
4) for health care workers - to log vaccine administrations and to view their schedule.


### Progress:
| Component     	 | Status        	 |
|-----------------|-----------------|
| Database      	 | done ✅        	 |
| Back-end      	 | done ✅        	 |
| CLI front-end 	 | done ✅        	 |
| GUI front-end 	 | in-progress ⏳ 	 |

### Technologies:
- Java 11 + OpenJFX 11
- MySQL
- Hibernate
- Maven

#### Design patterns worth mentioning:
- The system structure design is the `MVC` pattern.
- Complex entities are instantiated using the `Builder` pattern.
- The primary model class `EntitiesManager` is implemented as a `Singleton`.
