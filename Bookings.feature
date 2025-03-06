@Bookings
Feature: Verify the booking APIs of heroku app

  Scenario Outline: Create a new booking and verify the details
    Given user is able to create a booking request with details "<firstName>", "<additionalNeeds>", "<totalPrice>", "<depositPaid>", "<lastName>" and booking dates as "<checkIn>", "<checkOut>"
    Then user tries to fetch the booking details for "<inValidBookingID>"
    Then user fetches the booking details based on booking ID and verifies "<firstName>", "<additionalNeeds>", "<totalPrice>", "<depositPaid>", "<lastName>", "<checkIn>", "<checkOut>"

    Examples:
      | firstName | additionalNeeds | checkIn    | checkOut   | totalPrice | depositPaid | lastName | inValidBookingID |
      | Mohammed  | Beverages       | 2025-01-03 | 2025-02-04 | 1120       | true        | Saleem   | 000A             |