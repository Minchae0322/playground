package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.response.PendingTeamResponse;
import com.example.playgroundmanage.dto.response.PendingUserResponse;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.login.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.type.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final TeamingService teamingService;

    private final PasswordEncoder passwordEncoder;

    private final GameRepository gameRepository;

    @Transactional
    public void signup(UserSignupForm userSignupForm) {
        validateUser(userSignupForm.getUsername(), userSignupForm.getPassword());
        if(userRepository.existsByUsername(userSignupForm.getUsername())) {
            throw new ExistUserException();
        }
        User user = User.builder()
                .username(userSignupForm.getUsername())
                .password(passwordEncoder.encode(userSignupForm.getPassword()))
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public List<Team> getTeamUserBelong(User user) {
        List<Teaming> userTeamRelations = teamingService.getTeamUserRelations(user);

        return userTeamRelations.stream()
                .map(Teaming::getTeam)
                .toList();
    }


    public List<PendingTeamResponse> getPendingTeamRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        List<Game> games = getHostCreatedGamesNotStarted(user);
        return getUnacceptedHomeSubTeams(games).stream()
                .map(g -> PendingTeamResponse.builder()
                        .teamName(g.getTeam().getTeamName())
                        .build())
                .toList();

    }

    public List<PendingUserResponse> getPendingUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        List<Game> games = getHostCreatedGamesNotStarted(user);
        List<SubTeam> subTeams = getUnacceptedHomeSubTeams(games);
        SubTeam subTeam = subTeams.stream()
                .filter(SubTeam::isNoneTeam)
                .findFirst().orElseThrow();
        subTeam.getMatchParticipants().stream().filter(p -> !p.isAccepted())
                .toList();
        return null;
    }

    public List<SubTeam> getUnacceptedHomeSubTeams(List<Game> games) {
        List<SubTeam> requestTeams = new ArrayList<>();
        for (Game game : games) {
            requestTeams.addAll(getUnacceptedSubTeams(game.getHomeTeam()));
            requestTeams.addAll(getUnacceptedSubTeams(game.getAwayTeam()));
        }
        return requestTeams;
    }

    public List<SubTeam> getUnacceptedSubTeams(CompetingTeam competingTeam) {
        return competingTeam.getSubTeams().stream()
                .filter(t -> !t.isAccept())
                .toList();
    }

    public List<Game> getHostCreatedGamesNotStarted(User user) {
        return gameRepository.findAllByHostAndGameStartDateTimeAfter(user, LocalDateTime.now());
    }





}
