package com.internet_application.backend;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.internet_application.backend.Deserializers.*;
import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Repositories.*;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.ChildService;
import com.internet_application.backend.Services.UserService;
import com.internet_application.backend.Utils.DateUtils;
import com.internet_application.backend.Utils.MiscUtils;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@SuppressWarnings("Duplicates")
public class BackendApplication {

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
    private ChildService childService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Value("classpath:/data/lines/*.json")
    private Resource[] lineFiles;


    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    InitializingBean initDatabase() {
        return () -> {
            System.out.println("Initializing database...");
            loadRoles();
            loadUsers();
            loadChildren();
            // loadBusLines();
            loadStops();
            // loadLineStops();
            loadLines();
            loadRides();
            loadReservations();
        };
    }

    private void loadBusLines() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BusLineEntity.class, new BusLineDeserializer());
        mapper.registerModule(module);
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

    private void loadLines() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BusLineEntity.class, new BusLineInitDeserializer());
        mapper.registerModule(module);
        TypeReference<BusLineEntity> busLineType = new TypeReference<BusLineEntity>() {};

        // PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            // Resource[] resources = resolver.getResources("file:/data/lines/*.json");

            // MiscUtils.getResourceFolderFiles("/data/lines/")
            for (final Resource lineResource : lineFiles) {
                InputStream curFile = lineResource.getInputStream();

                BusLineEntity line = mapper.readValue(curFile, busLineType);
                // line.setName(curFile.getName().replaceFirst("[.][^.]+$", ""));

                BusLineEntity savedLine =busLineRepository.save(line);

                for(LineStopEntity ls: line.getOutwordStops()) {
                    ls.setLine(savedLine);
                    ls.setDirection(false);
                    lineStopRepository.save(ls);
                }

                for(LineStopEntity ls: line.getReturnStops()) {
                    ls.setLine(savedLine);
                    ls.setDirection(true);
                    lineStopRepository.save(ls);
                }
            }

            System.out.println("Bus lines list saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadStops() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(StopEntity.class, new StopDeserializer());
        mapper.registerModule(module);
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

    private void loadChildren() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ChildEntity.class, new ChildDeserializer());
        mapper.registerModule(module);
        TypeReference<List<ChildEntity>> childType = new TypeReference<List<ChildEntity>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/data/children.json");
        try {
            List<ChildEntity> stateList = mapper.readValue(is, childType);
            childService.saveAll(stateList);
            System.out.println("Children saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadRides() {
        // Retrieve all the lines available
        List<BusLineEntity> lines =  busLineRepository.findAll();

        // Select all the dates that are not holidays
        List<Date> dateList = new ArrayList<Date>();
        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusMonths(2)); date = date.plusDays(1)) {
            if(!DateUtils.isHoliday(date)) {
                dateList.add(DateUtils.convertToDateViaSqlDate(date));
            }
        }

        // For each line and each day which is not a holiday, create all the rides for the next two months
        List<RideEntity> rideList = new ArrayList<RideEntity>();
        for(BusLineEntity line: lines) {
            for(Date date: dateList) {
                RideEntity toSchoolRide = new RideEntity();
                toSchoolRide.setDate(date);
                toSchoolRide.setDirection(false);
                toSchoolRide.setLine(line);
                toSchoolRide.setRideBookingStatus(RideBookingStatus.NOT_STARTED);
                toSchoolRide.setLocked(false);
                toSchoolRide.setLatestStop(null);
                toSchoolRide.setLatestStopTime(null);
                rideList.add(toSchoolRide);

                RideEntity fromSchoolRide = new RideEntity();
                fromSchoolRide.setDate(date);
                fromSchoolRide.setDirection(true);
                fromSchoolRide.setLine(line);
                fromSchoolRide.setRideBookingStatus(RideBookingStatus.NOT_STARTED);
                fromSchoolRide.setLocked(false);
                fromSchoolRide.setLatestStop(null);
                fromSchoolRide.setLatestStopTime(null);
                rideList.add(fromSchoolRide);
            } // Marco is beautiful or Beautiful is Marco?
        }

        System.out.println("Rides list created successfully");
        rideRepository.saveAll(rideList);
        System.out.println("Rides list saved successfully");
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