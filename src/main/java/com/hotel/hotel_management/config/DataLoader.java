package com.hotel.hotel_management.config;

import com.hotel.hotel_management.model.Room;
import com.hotel.hotel_management.model.User;
import com.hotel.hotel_management.repository.RoomRepository;
import com.hotel.hotel_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataLoader(UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {

        // ✅ ADMIN USER
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setEmail("admin@hotel.com");
            admin.setFullName("System Administrator");
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);
        }

        // ✅ GUEST USER
        if (userRepository.findByUsername("guest").isEmpty()) {
            User guest = new User();
            guest.setUsername("guest");
            guest.setPassword(encoder.encode("guest123"));
            guest.setEmail("guest@hotel.com");
            guest.setFullName("Test Guest");
            guest.setPhone("9876543210");
            guest.setCity("Delhi");
            guest.setState("Delhi");
            guest.setRole("ROLE_USER");

            userRepository.save(guest);
        }

        // ✅ DEMO USER
        if (userRepository.findByUsername("san").isEmpty()) {
            User san = new User();
            san.setUsername("san");
            san.setPassword(encoder.encode("san"));
            san.setEmail("san@hotel.com");
            san.setFullName("San Demo");
            san.setPhone("9123456780");
            san.setCity("Mumbai");
            san.setState("Maharashtra");
            san.setRole("ROLE_USER");

            userRepository.save(san);
        }

        // ✅ ROOMS
        if (roomRepository.count() == 0) {
            String[][] rooms = {
                {"S101","SINGLE","2500","1","Cozy single room with city view"},
                {"S102","SINGLE","2500","1","Cozy single room with garden view"},
                {"D201","DELUXE","4000","2","Spacious deluxe room with balcony"},
                {"D202","DELUXE","4000","2","Spacious deluxe, lake-facing"},
                {"D203","DELUXE","4500","2","Premium deluxe with king bed"},
                {"DB301","DOUBLE_BED","4500","2","Double bed, family-friendly"},
                {"DB302","DOUBLE_BED","4500","2","Double bed with extra sofa"},
                {"SU401","SUITE","7000","4","Executive suite with living area"},
                {"SU402","SUITE","7500","4","Presidential suite, top floor"},
                {"TW501","TWIN","3500","2","Twin bed, great for friends"},
                {"FM601","FAMILY","6000","4","Family room, kids welcome"},
            };

            for (String[] r : rooms) {
                Room room = new Room();
                room.setRoomNumber(r[0]);
                room.setRoomType(r[1]);
                room.setPricePerNight(new BigDecimal(r[2]));
                room.setMaxGuests(Integer.parseInt(r[3]));
                room.setDescription(r[4]);
                room.setHotelName("Grand Hotel");
                room.setStatus("AVAILABLE");

                roomRepository.save(room);
            }
        }
    }
}