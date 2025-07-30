-- Insert mock flights data
INSERT INTO FLIGHTS (flight_id, flight_code, origin, destination, departure_time, arrival_time, price) VALUES
    ('3fa85f64-5717-4562-b3fc-2c963f66afa6', 'ACME101', 'AKL', 'WLG', '2025-08-01T08:00:00+12:00', '2025-08-01T09:00:00+12:00', 89.99),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'ACME202', 'CHC', 'AKL', '2025-08-01T10:30:00+12:00', '2025-08-01T12:00:00+12:00', 120.00),
    ('1c6b1470-aabc-4cf5-8e3a-2df1b8eab8bb', 'ACME303', 'WLG', 'ZQN', '2025-08-02T14:15:00+12:00', '2025-08-02T16:00:00+12:00', 150.50),
    ('59be1395-c3b6-4e19-aea0-3c239c89a9e1', 'ACME404', 'DUD', 'CHC', '2025-08-03T07:45:00+12:00', '2025-08-03T08:45:00+12:00', 79.50),
    ('9e0f5c73-6c70-4ad4-83d0-8b5b8a2bbfa4', 'ACME505', 'NSN', 'AKL', '2025-08-03T09:30:00+12:00', '2025-08-03T11:10:00+12:00', 110.00),
    ('b5dc1104-7652-4b88-86d6-89b6d10e6e78', 'ACME606', 'WLG', 'TRG', '2025-08-04T13:00:00+12:00', '2025-08-04T14:30:00+12:00', 95.75),
    ('be8fcf2d-3ecf-4370-a65f-3a0bd16817ce', 'ACME707', 'ZQN', 'WLG', '2025-08-05T16:45:00+12:00', '2025-08-05T18:30:00+12:00', 135.25),
    ('6e2a1f14-6df9-4d30-98b2-1732dbf77b0d', 'ACME808', 'PMR', 'AKL', '2025-08-06T06:15:00+12:00', '2025-08-06T07:30:00+12:00', 89.00);

-- Insert mock bookings data
INSERT INTO BOOKINGS (booking_id, flight_id, passenger_name, passenger_email) VALUES
    ('7b907f2e-5742-4ad3-92e8-22f75e354ced', '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'Martin Smith', 'martin.smith@gmail.com'),
    ('bd6b8db7-83db-4953-9e99-5c6506f061af', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Sally Taylor', 'sally.taylor@hotmail.com'),
    ('c9d5a2a3-1ad3-4b95-8ad1-3baf71c643ac', 'b5dc1104-7652-4b88-86d6-89b6d10e6e78', 'Carol B Ginny', 'carol.b.ginny@test.com');
