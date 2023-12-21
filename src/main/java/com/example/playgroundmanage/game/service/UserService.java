package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.UserNicknameDto;
import com.example.playgroundmanage.dto.response.PendingTeamResponse;
import com.example.playgroundmanage.dto.response.PendingUserResponse;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.login.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.type.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final FileHandler fileHandler;

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

    @Transactional
    public List<TeamInfoResponse> getTeamsInfoUserBelongsTo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        List<Teaming> teams = user.getTeams();
        return teams.stream()
                .map(t -> TeamInfoResponse.builder()
                        .teamId(t.getId())
                        .teamName(t.getTeam().getTeamName())
                        .teamProfileImg(getTeamProfileImg(t.getTeam().getTeamPic()))
                        .build())
                .toList();
    }

    public InMemoryMultipartFile getTeamProfileImg(UploadFile uploadFile) {
        if (uploadFile != null) {
            try {
                return fileHandler.extractFile(uploadFile);
            } catch (IOException e) {
                throw new RuntimeException("사진을 가져 올 수 없습니다.");
            }
        }
        return null;
    }

    @Transactional
    public void updateProfileImg(Long userId, MultipartFile img) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        if (user.getProfileImg() != null) {
            fileHandler.deleteFile(user.getProfileImg());
        }
        UploadFile uploadFile = fileHandler.storeFile(img);
        user.updateProfile(uploadFile);
        userRepository.save(user);
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

    public UserInfoDto getUserProfile(User user) {
        return UserInfoDto.builder()
                .userNickname(user.getNickname())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public UserInfoDto getUserInfo(Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        InMemoryMultipartFile userProfileImg = fileHandler.extractFile(user.getProfileImg());
        return UserInfoDto.builder()
                .userNickname(user.getNickname())
                .userId(user.getId())
                .userProfileImg(userProfileImg)
                .build();
    }

    public List<PendingUserResponse> getPendingUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        List<Game> games = getHostCreatedGamesNotStarted(user);
        List<SubTeam> subTeams = getUnacceptedHomeSubTeams(games);
        SubTeam subTeam = subTeams.stream()
                .filter(SubTeam::isSoloTeam)
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

    @Transactional
    public UserNicknameDto changeNickname(Long userId, String newNickname) {
        if (isValidUserNickname(newNickname)) {
            throw new IllegalArgumentException("이미 존재하거나 사용 할 수 없는 닉네임입니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        userRepository.save(user.update(UserEdit.builder()
                .userNickname(newNickname)
                .build()));
        return UserNicknameDto.builder().userNickname(newNickname)
                .build();
    }

    public boolean isValidUserNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }





}
