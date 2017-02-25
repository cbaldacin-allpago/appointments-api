# appointments-api
A REST api for managing appointments of patients


## Technologies

- Java 8
- Spring Boot
- Spring Data
- MockMvc and JUnit
- MongoDB 
- Maven

Obs:. Assuming that Rest maturity model level 2 is ok for the purpose of the code exercise.


## How to run

1 - Make sure you have properly installed Java 8 and Maven.
2 - Open the file "src\main\resources\application.properties" and set your profile. 
2.1 - profile-dev is for embedded mongodb 
2.2 - profile-prod is for external mongodb which the configuration is at "src\main\resources\application-profile-prod.properties" (default)
3 - Open your command window, go to the project's root folder (/appointments-api) and run "mvn clean package"
4 - Once the build is run with success, jump ahead to the target folder and type "java -jar appointments-api-0.1.0.jar" and that's it. Tomcat is already embedded by the spring boot.


## API documentation and Solution description


#User story 1 = as a doctor I want to create my patients

1 - POST /api/v1/patients/ - Create a patient
request JSON example:
{
	"name":"Andrew",
	"surname":"Colins"
}

response JSON:
{
	"id":"58a075d93be8482fd8fec1f9",
	"name":"Andrew",
	"surname":"Colins"
}

#User story 2 = as a doctor I want to create appointments for a patient

2 - POST /api/v1/appointments/ - Create an appointment informing an already created patientId
request JSON example:
{
	"patientId":"58a078dd3be84836742a69dd",
	"start":"2017-02-12T15:01:49.667",
	"end":"2017-02-12T16:01:49.668"
}

response JSON:
{
	"id":"58a0789e3be8480b480a666c",
	"patientId":"58a078143be8480b480a666b",
	"start":"2017-02-12T14:58:28.724",
	"end":"2017-02-12T15:58:28.726",
	"rate":null
}

#User story 3 = as a doctor I want to see an overview of all appointments and their ratings

3 - GET /api/v1/appointments/ - Get all appointments with their rates. (null rates means the appointment hasn't been rated)
response JSON example:
[
	{"id":"58a07a123be84847bca88f90","patientId":"58a07a123be84847bca88f81","start":"2017-02-24T13:00:00","end":"2017-02-24T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8f","patientId":"58a07a123be84847bca88f80","start":"2017-02-23T13:00:00","end":"2017-02-23T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8e","patientId":"58a07a123be84847bca88f7f","start":"2017-02-22T13:00:00","end":"2017-02-22T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8d","patientId":"58a07a123be84847bca88f7e","start":"2017-02-21T13:00:00","end":"2017-02-21T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8c","patientId":"58a07a123be84847bca88f7d","start":"2017-02-20T13:00:00","end":"2017-02-20T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8b","patientId":"58a07a123be84847bca88f7c","start":"2017-02-17T13:00:00","end":"2017-02-17T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f8a","patientId":"58a07a123be84847bca88f7b","start":"2017-02-16T13:00:00","end":"2017-02-16T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f89","patientId":"58a07a123be84847bca88f7a","start":"2017-02-15T13:00:00","end":"2017-02-15T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f88","patientId":"58a07a123be84847bca88f79","start":"2017-02-14T13:00:00","end":"2017-02-14T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f87","patientId":"58a07a123be84847bca88f78","start":"2017-02-13T13:00:00","end":"2017-02-13T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f86","patientId":"58a07a123be84847bca88f77","start":"2017-02-10T13:00:00","end":"2017-02-10T14:00:00","rate":null},
	{"id":"58a07a123be84847bca88f85","patientId":"58a07a123be84847bca88f76","start":"2017-02-09T13:00:00","end":"2017-02-09T14:00:00","rate":null}
]

#User story 4 = as a doctor I want to see an overview of the next week’s appointments

4 - GET /api/v1/appointments/next-week - Get next week's appointments considering all patients. Also it was assumed that was not necessary to separate appointments by audiologist.
response JSON example:
[
	{"id":"58a07bb03be8483de087e525","patientId":"58a07bb03be8483de087e516","start":"2017-02-13T13:00:00","end":"2017-02-13T14:00:00","rate":null},
	{"id":"58a07bb03be8483de087e526","patientId":"58a07bb03be8483de087e517","start":"2017-02-14T13:00:00","end":"2017-02-14T14:00:00","rate":null},
	{"id":"58a07bb03be8483de087e527","patientId":"58a07bb03be8483de087e518","start":"2017-02-15T13:00:00","end":"2017-02-15T14:00:00","rate":null},
	{"id":"58a07bb03be8483de087e528","patientId":"58a07bb03be8483de087e519","start":"2017-02-16T13:00:00","end":"2017-02-16T14:00:00","rate":null},
	{"id":"58a07bb03be8483de087e529","patientId":"58a07bb03be8483de087e51a","start":"2017-02-17T13:00:00","end":"2017-02-17T14:00:00","rate":null}
]

#User story 5 = as a patient I want to see my next appointment

5 - GET /api/v1/patients/{patientId}/next-appointment - Get next appointment for the given patient.
response JSON example:
{
	"id":"58a07e223be8481aa4789dd1",
	"patientId":{patientId},
	"start":"2017-02-13T13:00:00",
	"end":"2017-02-13T14:00:00",
	"rate":null
}
	
	
#User story 6 = as a patient I want to rate my last appointment
	
6 - PUT /api/v1/patients/{patientId}/rate	
	
request JSON example:
{
	"rate":"8"
}	

response JSON:

{
	"id":"58a080073be8482b54514289",
	"patientId":{patientId},
	"start":"2017-02-10T13:00:00",
	"end":"2017-02-10T14:00:00",
	"rate":8
}
	
OBS:. This service has been designed to rate any appointment not only the last.
In order to rate a specific appointment provide the appointmentId in the JSON request.
If the appointmentId is absent then the last appointment is rated by default.

Example:
request JSON example:
{
	"appointmentId":{appointmentId},
	"rate":"8"
}	

response JSON:
{
	"id":{appointmentId},
	"patientId":{patientId},
	"start":"2017-02-10T13:00:00",
	"end":"2017-02-10T14:00:00",
	"rate":8
}