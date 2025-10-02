# COMP512 Programming Assignment 1

A Travel Reservation system, a component-based distributed information system, where customers can reserve flights, cars and rooms for their vacation.

To run the RMI resource managers as well as the middleware, a convenience script file run_servers.sh is provided:
```
cd Server/
./make_all  # Only required on first run
./run_servers.sh
```

Then start a Client to interact with the system. To run the RMI client:
```
cd Client
./run_client.sh [<middleware_hostname> [<middleware_name>]]
```

## Testing
To run the tests, in a running client session, send the command:
```
!, <file_path>
```
Where file_path are the path for the test files, which contains batched commands to be executed. Some test files are already provided under the Client/Client/Test folder.