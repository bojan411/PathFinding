# Path following algorithm

Problem description [here](https://github.com/softwaresauna/code-challenge)

## Solution  

Program takes a map as an input, finds the position of start character and then follows the path and stops when end character is reached. 

Result of the program will be:
* Collected letters - without collecting same one twice
* Path as characters
* Error in case of any issues

## Implementation details

Current implementation traverses the map recursively until the end is found using index value matching logic. 
Values are evaluated using regex so pattern matching can be used in the CLI.
Current implementation aims to be robust and scalable by applying designs and principles like:
 - Separation of concerns
 - Single responsibility principle
 - Open-closed principle
 - Dependency Inversion Principle
 - High Cohesion

Also, current implementation uses method factories to instantiate particular interface implementations.

It should be possible to implement additional processors, finders, path resolvers, map parser and validators with no additional overhead other then implementing existing interfaces.



### Layers
* **Main app**. Entry point to application.
* **Service**. Entry point to adapter implementation.
* **Adapter**. Adapter specific logic implementation. Depends on Core.
* **Core**. Core application logic. 

Each layer has its own interfaces, implementation and related tests.
 

## Run tests

run `mvn test`

## Run application

Before running first package application by running `mvn clean package` 

Then run `java -jar target/pathFinderApp.jar`

### Configuring path characters and parameters

> **-n** [Represents empty[none] value in path.] [DEFAULT: " "] <br>
**-ud** [Represents value used to go up or down.] [DEFAULT: |] <br>
**-lr** [Represents value used to go left or right.] [DEFAULT: -] <br>
**-s** [Represents starting value.] [DEFAULT: @] <br>
**-e** [Represents ending value.] [DEFAULT: x] <br>
**-i** [Represents intersection value.] [DEFAULT: + or any letter] <br>
**-isc** [Represents intersection value on which to validate errors like fork and fake turn.] [DEFAULT: +] <br>
**-ipg** [Represents the maximum number of items are in each map list.] [DEFAULT: 2] <br>
**-f** [Represents where to find currentPath to traverse.] [DEFAULT: N/A] <br>
**-h** [Represents help menu with described arguments.] <br>

Example

`java -jar target/pathFinderApp.jar -f src/test/resources/basic.txt -s @ -e x`

File will be loaded from `src/test/resources/basic.txt` and START character will be set to @
and END character will be set to x.

Above HELP is also available when run with `java -jar target/pathFinderApp.jar -h`

## Author

Name: Bojan Ključević <br>
Contact: bkljuevi@yahoo.com


