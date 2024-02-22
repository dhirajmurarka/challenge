# Functionality for a transfer of money between accountsport services

## Further action points for the project

. Setter state of Account Balance to make thread safe. 
. Alternatively transferMoney service method to make @Transactional after adding spring-tx/transaction dependency.
. Enhanced Errors response can be returned from API in case of error. 
. Further more Tests can be added for various scenarios.  Also multithreading test cases for  join/interruption to add. 
. Logging can be enhanced. Thread/Marker can be added in log.
. For production use the map/values can be stored in the disk i.e. file System or DB , and save method to be enhanced accordingly.
. Service interface can be added. Service Implementation can be seperated. 
