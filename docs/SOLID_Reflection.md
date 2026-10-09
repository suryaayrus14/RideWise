# SOLID Principles Reflection — RideWise

This document describes how the current RideWise design relates to SOLID. These are design reflections, not a claim that every principle is perfectly satisfied.

## 1. Single Responsibility Principle (SRP)
Classes have focused responsibilities:
- `RiderService` manages rider registration and lookup.
- `DriverService` manages driver registration, availability, and lookup.
- `RideService` coordinates ride use cases and lifecycle operations.
- `RideConsoleView` formats console output.
- `HaversineDistanceCalculator` calculates geographic distance.
- Fare and matching strategies encapsulate their respective algorithms.

`Main` handles user interaction and dependency wiring rather than implementing fare or matching algorithms.

## 2. Open/Closed Principle (OCP)
`RideService` can work with different `RideMatchingStrategy` and `FareStrategy` implementations. A new matching or fare strategy can be introduced by implementing the relevant interface, with limited or no changes to the service orchestration.

For example, another fare strategy could be added without placing its pricing rules inside `RideService`.

## 3. Liskov Substitution Principle (LSP)
Implementations of `RideMatchingStrategy`, `FareStrategy`, and `DistanceCalculator` should honour their interface contracts. Any valid implementation should be usable wherever the corresponding interface is expected without breaking the ride workflow.

## 4. Interface Segregation Principle (ISP)
The strategy interfaces are narrow and purpose-specific:
- `RideMatchingStrategy` selects a driver.
- `FareStrategy` calculates a fare.
- `DistanceCalculator` calculates a distance.

Classes do not need to implement unrelated operations merely to participate in a strategy.

## 5. Dependency Inversion Principle (DIP)
`RideService` receives a `FareStrategy` and `DistanceCalculator` through its constructor and receives a matching strategy when a ride is requested. This keeps the service from having to instantiate every algorithm internally and supports substitution for tests or future implementations.

`Main` acts as the composition root by selecting concrete implementations.

## Additional Design Principles

### Composition over Inheritance
`PeakHourFareStrategy` composes/delegates to a base `FareStrategy`. `RideService` is configured with strategies rather than inheriting from strategy classes.

### Low Coupling and High Cohesion
The console, domain models, services, and algorithms are separated. Changes to fare calculation should primarily affect fare strategies; changes to distance calculation should primarily affect the distance calculator.

### Law of Demeter
Callers should avoid navigating deeply through unrelated objects when a direct method can express the required operation. For example, output code that repeatedly traverses `ride.getDriver().getVehicleType()` should be kept to the presentation boundary and not spread throughout business logic. Continue to review method chains as the project evolves.

## Limitations / Reflection
- `Main` still knows the concrete strategy classes because it wires the application. That is acceptable for a small console application.
- The in-memory collections are not persistent and are not designed for concurrent requests.
- Vehicle type is currently data on `Driver`; it is not yet a fare or eligibility policy.
- The design demonstrates the principles at a learning-project scale and should not be described as a production-ready architecture.
