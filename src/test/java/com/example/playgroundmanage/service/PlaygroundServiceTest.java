/*
package com.example.playgroundmanage.service;


import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.location.vo.Playground;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlaygroundServiceTest {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private PlaygroundService playgroundService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    private Playground testPlayground;
    private User testUser;
    @BeforeEach
    void before() {
        testUser = User.builder()
                .username("test")
                .nickname("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
        testPlayground = Playground.builder()
                .name("test")
                .build();
        playgroundRepository.save(testPlayground);
    }


  */
/*  @Test
    void 현재_진행중인_경기_불러오기() {
        //given 운동장안에 9개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterOneMinute = LocalDateTime.now().plusMinutes(1);

        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(afterOneMinute.plusMinutes(i * 60L))
                        .runningTime(59)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(10, gameRepository.count());

        //when

        GameThumbnail game = playgroundService.getOngoingGame(testPlayground.getId(), afterOneMinute.plusMinutes(1));

        //then
        assertEquals(afterOneMinute.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), game.getGameStart());
    }

    @Test
    void 현재_진행중인_경기_불러오기_60분_후() {
        //given 운동장안에 9개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = now.plusMinutes(61);

        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(now.plusMinutes(1).plusMinutes(i * 60L))
                        .runningTime(59)
                        .host(testUser)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        for (Game g : gameList) {
            System.out.println(g.getGameStartDateTime());
        }

        assertEquals(10, gameRepository.count());

        //when

        GameThumbnail game = playgroundService.getOngoingGame(testPlayground.getId(), after.plusMinutes(1));

        //then
        assertEquals(now.plusMinutes(61).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), game.getGameStart());
    }

    @Test
    void 운동장_해당날짜의_타임라인() {
        //given 운동장안에 9개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusMinutes(1);

        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(after.plusMinutes(i * 60L))
                        .runningTime(59)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(10, gameRepository.count());

        //when
        List<OccupiedTime> occupiedTimes = playgroundService.getDailyGameTimelines(testPlayground.getId(), now);

        for (OccupiedTime occupiedTime : occupiedTimes) {
            System.out.println(occupiedTime.getStart() +" " + occupiedTime.getEnd());
        }

        //then
    }

    @Test
    void 가장빠른_3경기_가져오기() {
        //given 운동장안에 10개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusMinutes(1);
        //현재시간으로부터 1분뒤에 60분 마다 경기가 시작한다.
        //경기시간은 59분
        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(after.plusMinutes(i * 60L))
                        .runningTime(59)
                        .host(testUser)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(10, gameRepository.count());

        //when
        List<GameThumbnail> gameThumbnails = playgroundService.getUpcomingGames(testPlayground.getId());
        assertEquals(3, gameThumbnails.size());

        assertEquals("test", gameThumbnails.get(0).getHostName());
        assertEquals(59, gameThumbnails.get(1).getRunningTime());

        //then
    }

    @Test
    void 가장빠른_3경기_가져오기_2경기() {
        //given 운동장안에 10개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusMinutes(1);
        //현재시간으로부터 1분뒤에 60분 마다 경기가 시작한다.
        //경기시간은 59분
        List<Game> gameList = IntStream.range(0, 2)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(after.plusMinutes(i * 60L))
                        .runningTime(59)
                        .host(testUser)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(2, gameRepository.count());

        //when
        List<GameThumbnail> gameThumbnails = playgroundService.getUpcomingGames(testPlayground.getId());
        assertEquals(2, gameThumbnails.size());
        assertEquals("test", gameThumbnails.get(0).getHostName());
        assertEquals(59, gameThumbnails.get(1).getRunningTime());

        //then
    }

    @Test
    void 가장빠른_3경기_가져오기_경기가없음() {
        List<GameThumbnail> gameThumbnails = playgroundService.getUpcomingGames(testPlayground.getId());
        assertEquals(0, gameThumbnails.size());
    }*//*

}*/
