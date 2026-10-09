# RideWise Requirements

## 1. Purpose
RideWise is a simplified console-based ride-sharing system intended to demonstrate low-level design, object-oriented programming, and clean separation of responsibilities. It is not intended to be a complete commercial application.

## 2. Functional Requirements

### FR-1: Rider Registration
The system shall allow a user to register a rider with a name and pickup location. The system generates a rider ID.

### FR-2: Driver Registration
The system shall allow a user to register a driver with a name and current location. The system generates a driver ID. The implementation also records a `VehicleType` (`BIKE`, `AUTO`, or `CAR`).

### FR-3: View Available Drivers
The system shall display drivers whose availability state is available. Driver details include identity and, in the current implementation, vehicle type and active ride count.

### FR-4: Request a Ride
The user shall provide a registered rider ID and destination coordinates. The system calculates the trip's straight-line distance from the rider's pickup location to the destination. The user chooses a ride-matching strategy.

### FR-5: Driver Matching
The system shall support interchangeable driver-matching strategies:
- `NearestDriverStrategy`: chooses the available driver closest to the rider's pickup location.
- `LeastActiveDriverStrategy`: chooses an available driver with the lowest active ride count.

If no driver can be selected, the system shall report an appropriate error.

### FR-6: Fare Calculation
The system shall support interchangeable fare strategies:
- `DefaultFareStrategy`: calculates a fare from the configured base amount and per-kilometre rate.
- `PeakHourFareStrategy`: delegates to the default fare strategy and applies a multiplier during configured peak periods.

### FR-7: Ride Lifecycle
The ride lifecycle uses these statuses:
- `REQUESTED`
- `ASSIGNED`
- `COMPLETED`
- `CANCELLED`

A successful ride request is assigned to a driver. A ride may be completed after assignment. A ride may be cancelled according to the state-transition rules implemented in `Ride`.

### FR-8: Fare Receipt
A fare receipt is generated for a successfully requested ride and is associated with that ride.

### FR-9: Console Menu
The console shall support rider registration, driver registration, viewing available drivers, requesting rides, completing rides, cancelling rides, viewing rides, and exiting.

### FR-10: Vehicle Type
The current implementation stores and displays each driver's vehicle type. Vehicle type does not currently determine fare or restrict driver matching.

## 3. Non-Functional / Design Requirements
- Use interfaces for interchangeable strategies.
- Keep console interaction separate from business operations.
- Prefer composition over inheritance.
- Aim for low coupling and high cohesion.
- Keep business logic in service/model/strategy classes rather than in `Main`.
- Apply SOLID principles where appropriate.
- Use meaningful validation and exceptions for invalid operations.

## 4. Assumptions and Constraints
- Data is stored in memory and is lost when the application exits.
- Locations are represented by latitude and longitude.
- Haversine distance is a straight-line distance, not a road-route distance.
- The application is a console-based learning project; no database, REST API, authentication, map provider, or production deployment is in scope.
- Exact fare rates and peak-hour windows are defined in the strategy implementation and should be kept consistent with the code.

## 5. Acceptance Checklist
- [ ] Rider registration returns a generated ID.
- [ ] Driver registration returns a generated ID and records a vehicle type.
- [ ] Available-driver listing excludes unavailable drivers.
- [ ] Ride requests calculate distance from pickup to destination.
- [ ] Both matching strategies can be selected.
- [ ] Fare receipt is generated for a successful ride request.
- [ ] Completion changes status to `COMPLETED` and releases the driver.
- [ ] Cancellation changes status to `CANCELLED` and releases the driver when allowed.
- [ ] A request fails clearly when no driver is available.
- [ ] Ride history displays ride status and relevant ride details.
- [ ] The project compiles and the manual test runner passes.
