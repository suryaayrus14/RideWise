# RideWise Class Model

## 1. Domain Model

### `Rider`
Represents a person requesting a ride.
- Key state: rider ID, name, pickup `Location`.
- Responsibility: hold rider data used by the ride workflow.

### `Driver`
Represents a driver who may be assigned rides.
- Key state: driver ID, name, current location, `VehicleType`, availability, active ride count.
- Responsibility: maintain driver state relevant to matching and ride assignment.

### `Ride`
Represents one ride request.
- Key state: ride ID, rider, destination, calculated distance, request timestamp, assigned driver, status, fare receipt.
- Responsibility: hold ride data and enforce valid ride-state transitions through its methods.

### `FareReceipt`
Represents the fare calculated for a ride.
- Key state: ride ID, amount, generation timestamp.
- Responsibility: preserve the fare result for display/history.

### `Location`
Represents a geographic coordinate using latitude and longitude.

### `RideStatus`
Enum representing `REQUESTED`, `ASSIGNED`, `COMPLETED`, and `CANCELLED`.

### `VehicleType`
Enum representing `BIKE`, `AUTO`, and `CAR`. In the current implementation, vehicle type is stored and displayed but does not affect matching or fare.

## 2. Services

### `RiderService`
Registers riders and retrieves riders by ID. It owns the in-memory rider collection.

### `DriverService`
Registers drivers, retrieves drivers, returns available drivers, and updates driver availability. It owns the in-memory driver collection.

### `RideService`
Coordinates the ride use case:
1. Resolve the rider.
2. Calculate distance from pickup to destination through `DistanceCalculator`.
3. Retrieve available drivers.
4. Ask the selected `RideMatchingStrategy` to choose a driver.
5. Create and assign a ride.
6. Calculate fare through `FareStrategy` and attach a `FareReceipt`.
7. Update driver availability and active ride count.
8. Track the ride and manage completion/cancellation.

### `RideConsoleView`
Formats available-driver and ride-history output for the console.

### `Main`
Creates and wires the services and strategies, collects console input, invokes service methods, and reports results. It should not contain the core matching or fare algorithms.

## 3. Strategy Interfaces and Implementations

### `RideMatchingStrategy`
Contract for choosing a driver from the available drivers.
- `NearestDriverStrategy`: uses distance from the rider's pickup location.
- `LeastActiveDriverStrategy`: chooses a driver with the lowest active ride count.

### `DistanceCalculator`
Contract for calculating distance between two locations.
- `HaversineDistanceCalculator`: computes straight-line distance in kilometres using the Haversine formula.

### `FareStrategy`
Contract for calculating a fare for a ride.
- `DefaultFareStrategy`: base fare plus a per-kilometre charge, according to the implementation.
- `PeakHourFareStrategy`: wraps/delegates to a base fare strategy and applies a multiplier during configured peak windows.

## 4. Supporting Classes

### `NoDriverAvailableException`
Signals that a ride cannot be assigned because no eligible available driver can be selected.

### `IdGenerator`
Generates unique-in-process identifiers for riders, drivers, and rides using counters.

## 5. Main Dependencies
- `Main` depends on service interfaces/classes and strategy implementations to wire the application.
- `RideService` depends on `RiderService`, `DriverService`, `DistanceCalculator`, `RideMatchingStrategy`, and `FareStrategy`.
- `Ride` refers to a `Rider`, optionally an assigned `Driver`, and a `FareReceipt`.
- Matching strategies depend on domain data and, for nearest-driver matching, a `DistanceCalculator`.

## 6. Design Note
This is a conceptual class model based on the project design. Keep names and method signatures in this document aligned with the source code if the implementation changes.
