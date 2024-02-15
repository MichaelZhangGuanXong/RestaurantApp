package michael.zhang.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.rest.RestHandlerBuilder;
import lombok.Builder;
import lombok.Data;
import michael.zhang.restaurant.repository.ReservationRepository;
import org.apache.commons.lang3.ObjectUtils;

import javax.ws.rs.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static michael.zhang.restaurant.repository.ReservationRepository.maxReservationHours;

public class RestaurantApp {
    public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss", Locale.ENGLISH);

    public static void main(String[] args) throws Exception {

        Map<String, List<Reservation>> reservationMap = new HashMap<>();

        List<Reservation> dayReservations = new ArrayList<>();

        dayReservations.add(Reservation
                        .builder()
                        .customerName("Joe Parker")
                        .reservationHours(2)
                        .tableSize(2)
                        .bookDateTime(new Date())
                        .reservationDateTime(dateTimeFormatter.parse("2024-03-01 12:00:00"))
                        .build());

        reservationMap.put("2024-03-01", dayReservations);

        ReservationRepository repository = new ReservationRepository(reservationMap);

        Reservations reservations = new Reservations(repository);

        MuServer server = MuServerBuilder
                .httpServer()
                .withHttpsPort(8080)
                .addHandler(Method.GET, "/", (request, response, pathParams) -> response.write("Hello, world"))
                .addHandler(RestHandlerBuilder.restHandler(reservations)
                        .addCustomWriter(new JacksonJaxbJsonProvider())
                        .addCustomReader(new JacksonJaxbJsonProvider()))
                .start();

        System.out.println("Started server at " + server.uri());
    }

    // This class just like a RestController in Spring Web, for "GET" should allow all user to run
    // but for "POST", can be run by owner role only
    // The role authentication/Authorization can be implemented by Spring Security, for muServer, need
    // more time to understand how
    @Path("/reservations")
    public static class Reservations {

        private final ReservationRepository repository;

        public Reservations(ReservationRepository repository) {
            this.repository = repository;
        }

        /**
         * This method is used to look up all reservations by day
         * @param day - String like "yyyy-mm-dd"
         * @return - all reservations as a list
         */
        @GET
        @Path("/{day}")
        @Produces("application/json")
        public List<Reservation> lookupByDay(@PathParam("day") String day) {
            List<Reservation> reservationsByDay = repository.lookupReservationsByDay(day);

            if (ObjectUtils.isEmpty(reservationsByDay)) {
                throw new NotFoundException("No reservations for the day - " + day);
            }

            return reservationsByDay;
        }

        /**
         * This method is used to book a reservation, will save through repository
         * @param request - to see BookRequest class
         * @return - instance of reservation
         */
        @POST
        @Consumes("application/json")
        @Produces("application/json")
        public Reservation book(BookRequest request) {
            if (request.reservationHours > maxReservationHours)
                request.setReservationHours(maxReservationHours);

            // Here should include some validation logic to check reservation can be applied or not

            Reservation reservation = Reservation
                    .builder()
                    .customerName(request.customerName)
                    .tableSize(request.getTableSize())
                    .reservationDateTime(request.reservationDateTime)
                    .bookDateTime(new Date())
                    .reservationHours(request.getReservationHours())
                    .build();

            repository.save(reservation);

            return reservation;
        }

    }

    @Data
    public static class BookRequest implements Serializable {

        @JsonProperty("customerName")
        public String customerName;

        @JsonProperty("tableSize")
        public int tableSize;

        @JsonProperty("reservationDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Date reservationDateTime;

        @JsonProperty("reservationHours")
        public int reservationHours = 2;

    }

    @Builder
    @Data
    public static class Reservation implements Serializable {
        @JsonProperty("customerName")
        public String customerName;

        @JsonProperty("tableSize")
        public int tableSize;

        @JsonProperty("bookDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Date bookDateTime;

        @JsonProperty("reservationDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Date reservationDateTime;

        public int reservationHours;
    }

}