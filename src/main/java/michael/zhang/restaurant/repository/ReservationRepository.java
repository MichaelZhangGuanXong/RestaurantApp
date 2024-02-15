package michael.zhang.restaurant.repository;

import michael.zhang.restaurant.RestaurantApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// This repository should connect to DB through JPA, but here is just use hardcode to simulate
public class ReservationRepository {
    public final static int maxReservationHours = 2;
    public final static SimpleDateFormat dayOnlyFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private final Map<String, List<RestaurantApp.Reservation>> reservations;

    public ReservationRepository(Map<String, List<RestaurantApp.Reservation>> reservations) {
        this.reservations = reservations;
    }

    // In real project, this call should save reservation into DB
    public RestaurantApp.Reservation save(RestaurantApp.Reservation reservation) {
        if (reservation.reservationHours > maxReservationHours)
            reservation.setReservationHours(maxReservationHours);

        String day = dayOnlyFormatter.format(reservation.reservationDateTime);

        if (!reservations.containsKey(day)) {
            List<RestaurantApp.Reservation> dayReservations = new ArrayList<>();
            dayReservations.add(reservation);

            reservations.put(day, dayReservations);
        } else
            reservations.get(day).add(reservation);

        return reservation;
    }

    // In real project, this call should run SQL query to lookup reservations by day
    public List<RestaurantApp.Reservation> lookupReservationsByDay(String day) {
        return reservations.get(day);
    }

}
