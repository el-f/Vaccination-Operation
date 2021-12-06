# Vaccination-Operation
#### 'Human–computer interaction' Course Project

#### Description
The system oversees a vaccination operation in a city, managing data about its citizens, medical workers, and clinics and their vaccine supplies.
The purpose of the system is to facilitate vaccination appointments for citizens of varying risk groups, track the vaccination phase for each citizen, and make sure each clinic is adequately staffed and has enough vaccine supplies.

#### The system has three front ends:
- One for citizens - to make appointments and track their status through an app and a website.
- Second and Third for clinic managers to overlook their clinic and get relevant information to make informed logistical and managerial decisions.
- Third for operation managers - to overlook the operation and get relevant information to make informed logistical and managerial decisions.
- Forth for health care workers – to log vaccine administrations and to view their schedule.


### Progress:
| Component     	| Status        	|
|---------------	|---------------	|
| Database      	| done ✅        	|
| Back-end      	| done ✅        	|
| CLI front-end 	| done ✅        	|
| GUI front-end 	| in-progress ⏳ 	|

### Authors:
|  Author                                     	| Component                                           	|
|----------------------------------------------	|--------------------------------------------------	|
| [Elazar Fine](https://github.com/Elfein7Night)  | Database, Back-end, CLI front-end, GUI front-end 	|
| [Maor Ofek](https://github.com/maorofek)        | GUI front-end                                    	|
| [Ron Beraha](https://github.com/RonBless)       | GUI front-end                                    	|
| [Tal Benita](https://github.com/TalBenitaKoala) | GUI front-end                                    	|

### Technologies:
- Java 11 + OpenJFX 11
- MySQL
- Hibernate
- Maven

#### Design patterns worth mentioning:
- The system structure design is the `MVC` pattern.
- Complex entities are instantiated using the `Builder` pattern.
