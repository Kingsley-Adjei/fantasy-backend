package com.example.demo.fantasy.util;

import com.example.demo.fantasy.entity.Player;
import com.example.demo.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PlayerRepository playerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (playerRepository.count() == 0) {

            // --- GOALKEEPERS (GK) ---
            playerRepository.save(Player.builder().name("Pickford").position("GK").price(5.0).realClub("CS1").clubColor("#003399").nextFixture("IT1 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Nigel").position("GK").price(4.5).realClub("IT1").clubColor("#FFCC00").nextFixture("CS1 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("UglyBoy").position("GK").price(4.0).realClub("CS3").clubColor("#660000").nextFixture("IT2 (H)").pointsThisGw(0).totalPoints(0).build());

            // --- DEFENDERS (DEF) ---
            playerRepository.save(Player.builder().name("Saliba").position("DEF").price(6.0).realClub("CS1").clubColor("#003399").nextFixture("IT1 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Sadik").position("DEF").price(5.5).realClub("CS2").clubColor("#006633").nextFixture("CS4 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Justice").position("DEF").price(5.0).realClub("IT2").clubColor("#000000").nextFixture("CS3 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Brain").position("DEF").price(4.5).realClub("CS4").clubColor("#FF6600").nextFixture("CS2 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Arnold").position("DEF").price(5.5).realClub("CS1").clubColor("#003399").nextFixture("IT1 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Frequency").position("DEF").price(4.0).realClub("IT1").clubColor("#FFCC00").nextFixture("CS1 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Prince").position("DEF").price(4.0).realClub("CS2").clubColor("#006633").nextFixture("CS4 (H)").pointsThisGw(0).totalPoints(0).build());

            // --- MIDFIELDERS (MID) ---
            playerRepository.save(Player.builder().name("Saka").position("MID").price(10.0).realClub("CS1").clubColor("#003399").nextFixture("IT1 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Palmer").position("MID").price(9.5).realClub("IT1").clubColor("#FFCC00").nextFixture("CS1 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Salah").position("MID").price(12.5).realClub("CS3").clubColor("#660000").nextFixture("IT2 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Kingsley").position("MID").price(8.0).realClub("IT2").clubColor("#000000").nextFixture("CS3 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Dennis").position("MID").price(6.5).realClub("CS4").clubColor("#FF6600").nextFixture("CS2 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Blaise").position("MID").price(7.0).realClub("CS2").clubColor("#006633").nextFixture("CS4 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Don-Kay").position("MID").price(7.5).realClub("IT1").clubColor("#FFCC00").nextFixture("CS1 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Paul").position("MID").price(5.0).realClub("CS3").clubColor("#660000").nextFixture("IT2 (H)").pointsThisGw(0).totalPoints(0).build());

            // --- FORWARDS (FWD) ---
            playerRepository.save(Player.builder().name("Haaland").position("FWD").price(14.0).realClub("CS1").clubColor("#003399").nextFixture("IT1 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Charles").position("FWD").price(7.5).realClub("IT2").clubColor("#000000").nextFixture("CS3 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Osei").position("FWD").price(8.5).realClub("CS4").clubColor("#FF6600").nextFixture("CS2 (A)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Santos").position("FWD").price(7.0).realClub("CS2").clubColor("#006633").nextFixture("CS4 (H)").pointsThisGw(0).totalPoints(0).build());
            playerRepository.save(Player.builder().name("Banasco").position("FWD").price(6.5).realClub("IT1").clubColor("#FFCC00").nextFixture("CS1 (H)").pointsThisGw(0).totalPoints(0).build());

            System.out.println("✅ Player Market Populated with UI Styles!");
        }
    }
}