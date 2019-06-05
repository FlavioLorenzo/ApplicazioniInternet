package com.internet_application.exercise_3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.internet_application.exercise_3.Deserializers.LineStopDeserializer;
import com.internet_application.exercise_3.Deserializers.ReservationDeserializer;
import com.internet_application.exercise_3.Deserializers.RideDeserializer;
import com.internet_application.exercise_3.Deserializers.UserDeserializer;
import com.internet_application.exercise_3.Entities.*;
import com.internet_application.exercise_3.Repositories.*;
import com.internet_application.exercise_3.Services.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@SuppressWarnings("Duplicates")
public class Exercise3Application {

    @Autowired
    private BusLineRepository busLineRepository;
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LineStopRepository lineStopRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public static void main(String[] args) {
        SpringApplication.run(Exercise3Application.class, args);
    }

    @Bean
    InitializingBean initDatabase() {
        return () -> {
            System.out.println("Initializing database...");
            loadBusLines();
            loadStops();
            loadLineStops();
            loadRoles();
            loadUsers();
            loadRides();
            loadReservations();
        };
    }

    private void loadBusLines() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<BusLineEntity>> busLineType = new TypeReference<List<BusLineEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/bus_lines.json");
        try {
            List<BusLineEntity> stateList = mapper.readValue(is, busLineType);
            busLineRepository.saveAll(stateList);
            System.out.println("Bus lines list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadStops() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<StopEntity>> stopType = new TypeReference<List<StopEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/stops.json");
        try {
            List<StopEntity> stateList = mapper.readValue(is, stopType);
            stopRepository.saveAll(stateList);
            System.out.println("Stops list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadLineStops() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LineStopEntity.class, new LineStopDeserializer());
        mapper.registerModule(module);
        TypeReference<List<LineStopEntity>> lineStopType = new TypeReference<List<LineStopEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/bus_line_stops.json");
        try {
            List<LineStopEntity> stateList = mapper.readValue(is, lineStopType);
            lineStopRepository.saveAll(stateList);
            System.out.println("Line stops list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadRoles() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<RoleEntity>> roleType = new TypeReference<List<RoleEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/roles.json");
        try {
            List<RoleEntity> stateList = mapper.readValue(is, roleType);
            roleRepository.saveAll(stateList);
            System.out.println("Roles saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadUsers() {
        ObjectMapper mapper = new ObjectMapper();
        /*SimpleModule module = new SimpleModule();
        module.addDeserializer(UserEntity.class, new UserDeserializer());
        mapper.registerModule(module);*/
        TypeReference<List<UserEntity>> userType = new TypeReference<List<UserEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/users.json");
        try {
            List<UserEntity> stateList = mapper.readValue(is, userType);
            userService.saveAll(stateList);
            System.out.println("Users saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadRides() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RideEntity.class, new RideDeserializer());
        mapper.registerModule(module);
        TypeReference<List<RideEntity>> rideType = new TypeReference<List<RideEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/rides.json");
        try {
            List<RideEntity> stateList = mapper.readValue(is, rideType);
            rideRepository.saveAll(stateList);
            System.out.println("Rides list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadReservations() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ReservationEntity.class, new ReservationDeserializer());
        mapper.registerModule(module);
        System.out.println("module loaded");
        TypeReference<List<ReservationEntity>> reservationType = new TypeReference<List<ReservationEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/reservations.json");
        System.out.println("trying to read...");
        try {
            List<ReservationEntity> stateList = mapper.readValue(is, reservationType);
            System.out.println("done reading, saving...");
            reservationRepository.saveAll(stateList);
            System.out.println("Reservation list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
