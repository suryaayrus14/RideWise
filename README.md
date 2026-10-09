# RideWise — Modular Ride-Sharing System

## Overview
RideWise is a simplified, console-based ride-sharing application built in Java. It demonstrates object-oriented design and low-level design (LLD) concepts through rider and driver registration, ride matching, fare calculation, and ride lifecycle management.

This is an in-memory console project, not a production ride-sharing platform. Data is lost when the application exits.

## Features
- Register riders with pickup coordinates.
- Register drivers with location and vehicle type (`BIKE`, `AUTO`, or `CAR`).
- View available drivers.
- Request a ride by entering destination coordinates.
- Calculate straight-line trip distance with the Haversine formula.
- Choose a driver-matching strategy:
    - Nearest available driver.
    - Least active rides.
- Calculate fares using interchangeable strategies:
    - Default fare strategy.
    - Peak-hour fare strategy.
- Complete or cancel rides.
- View ride history, including status, distance, fare, and vehicle type.

## Technology
- Java
- Java Collections Framework
- `java.time` for request timestamps
- Console input/output

No database, web API, or Spring Boot is required.

## Project Structure
```text
src/
└── com/airtribe/ridewise/
    ├── Main.java
    ├── RideConsoleView.java
    ├── model/
    ├── service/
    ├── strategy/
    ├── exception/
    ├── util/
    └── test/
        └── RideWiseTest.java
docs/
    ├── Requirements.md
    ├── Class_Model.md
    ├── SOLID_Reflection.md
    └── Object_Relationships.md
```

The exact location of `RideWiseTest.java` may differ depending on how the project is configured in the IDE.

## How to Run
1. Open the project in IntelliJ IDEA or another Java IDE.
2. Ensure the package declarations match the directory structure.
3. Run `com.airtribe.ridewise.Main`.
4. Use the console menu to register riders and drivers, request rides, complete/cancel rides, and view ride history.

To run the lightweight test runner, run `RideWiseTest.main()` from the IDE. It is a plain Java test runner and does not require JUnit. Confirm that the test source/package configuration in your IDE is correct.

## Typical Workflow
1. Register a rider and record the generated rider ID.
2. Register one or more drivers, selecting a vehicle type for each.
3. View available drivers.
4. Request a ride using the registered rider ID and destination coordinates.
5. Select a matching strategy.
6. Review the assigned driver, vehicle type, calculated distance, fare, and status.
7. Complete or cancel the ride using its generated ride ID.
8. View all rides to inspect their final statuses.

## Fare and Distance Notes
- Distance is calculated between the rider's pickup coordinates and the entered destination coordinates.
- The Haversine calculation produces straight-line distance in kilometres, not road distance.
- The default fare strategy uses the project's configured base fare and per-kilometre rate.
- The peak-hour strategy applies its configured multiplier during its defined peak windows.
- Fare amounts displayed are estimates based on these simplified rules.

## Design Highlights
- **Strategy Pattern:** driver matching and fare calculation can be changed without embedding all variants in `RideService`.
- **Separation of concerns:** `Main` handles console interaction, services coordinate use cases, models represent domain state, and strategies encapsulate algorithms.
- **Composition over inheritance:** `RideService` is configured with strategy implementations instead of inheriting from them.
- **In-memory state:** services hold data in collections for this assignment's scope.



## Documentation
See the `docs/` directory:
- `Requirements.md` — functional scope and assumptions.
- `Class_Model.md` — responsibilities and key class design.
- `SOLID_Reflection.md` — how the design applies SOLID principles.
- `Object_Relationships.md` — associations, composition, and dependencies.
