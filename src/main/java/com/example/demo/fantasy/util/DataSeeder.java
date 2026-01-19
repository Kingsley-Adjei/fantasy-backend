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
            playerRepository.save(Player.builder().name("Haaland").position("FWD").realClub("Man City").price(15.0).build());
            playerRepository.save(Player.builder().name("Saka").position("MID").realClub("Arsenal").price(12.0).build());
            playerRepository.save(Player.builder().name("Palmer").position("MID").realClub("Chelsea").price(11.5).build());
            playerRepository.save(Player.builder().name("Salah").position("MID").realClub("Liverpool").price(13.5).build());
            playerRepository.save(Player.builder().name("Pickford").position("GK").realClub("Everton").price(5.0).build());
            playerRepository.save(Player.builder().name("Kingsley").position("MID").realClub("Man City").price(12.0).build());
            playerRepository.save(Player.builder().name("Sadik").position("DF").realClub("Man City").price(6.3).build());
            playerRepository.save(Player.builder().name("Dennis").position("MID").realClub("Man City").price(5.7).build());
            playerRepository.save(Player.builder().name("Blaise").position("MID").realClub("Man City").price(6.0).build());
            playerRepository.save(Player.builder().name("Justice").position("DF").realClub("Man City").price(5.5).build());
            playerRepository.save(Player.builder().name("Don-Kay").position("MID").realClub("Man City").price(6.5).build());
            playerRepository.save(Player.builder().name("Charles").position("FWD").realClub("Man City").price(7.0).build());
            playerRepository.save(Player.builder().name("Osei").position("FWD").realClub("Man City").price(10.0).build());
            playerRepository.save(Player.builder().name("Brain").position("DF").realClub("Man City").price(6.0).build());
            playerRepository.save(Player.builder().name("Nigel").position("GK").realClub("Man City").price(6.0).build());
            playerRepository.save(Player.builder().name("UglyBoy").position("GK").realClub("Man City").price(5.3).build());
            playerRepository.save(Player.builder().name("Santos").position("FWD").realClub("Man City").price(6.8).build());
            playerRepository.save(Player.builder().name("Arnold").position("DF").realClub("Man City").price(5.2).build());
            playerRepository.save(Player.builder().name("Banasco").position("FWD").realClub("Man City").price(6.0).build());
            playerRepository.save(Player.builder().name("Frequency").position("DF").realClub("Man City").price(5.0).build());
            playerRepository.save(Player.builder().name("Prince").position("DF").realClub("Man City").price(6.5).build());
            playerRepository.save(Player.builder().name("Paul").position("MID").realClub("Man City").price(5.8).build());

            System.out.println("✅ Player Market Populated!");
        }
    }
}