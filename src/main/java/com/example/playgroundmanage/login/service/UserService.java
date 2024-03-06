package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.dto.UserNicknameDto;
import com.example.playgroundmanage.dto.response.UserRecordResponse;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.location.repository.TeamingRepository;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.login.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final TeamingRepository teamingRepository;

    private final PasswordEncoder passwordEncoder;

    private final GameParticipantRepository gameParticipantRepository;

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
                .isEnable(true)
                .build();
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Transactional
    public List<TeamDto.TeamResponseDto> getTeamsInfoUserBelongsTo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        List<Teaming> teams = user.getTeams();
        return teams.stream()
                .map(t -> TeamDto.TeamResponseDto.builder()
                        .teamId(t.getTeam().getId())
                        .sportsEvent(t.getTeam().getSportsEvent().getValue())
                        .teamName(t.getTeam().getTeamName())
                        .teamProfileImg(getTeamProfileImg(t.getTeam().getTeamPic()))
                        .build())
                .toList();
    }

    public InMemoryMultipartFile getTeamProfileImg(UploadFile uploadFile) {
        if (uploadFile != null) {
            return fileHandler.extractFile(uploadFile);
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



    public UserInfoDto getUserProfile(User user) {
        return UserInfoDto.builder()
                .userNickname(user.getNickname())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public InMemoryMultipartFile getUserProfileImg(User user)  {
        return fileHandler.extractFile(user.getProfileImg());
    }

    @Transactional
    public UserInfoDto getUserInfo(User user)  {
        InMemoryMultipartFile userProfileImg = fileHandler.extractFile(user.getProfileImg());

        return UserInfoDto.builder()
                .userNickname(user.getNickname())
                .userId(user.getId())
                .userProfileImg(userProfileImg)
                .build();
    }

    @Transactional
    public UserInfoDto getUserInfoInTeam(Team team, User user)  {
        String role = teamingRepository.findByTeamAndUser(team, user).getRole();
        InMemoryMultipartFile userProfileImg = fileHandler.extractFile(user.getProfileImg());

        return UserInfoDto.builder()
                .userRole(role)
                .userNickname(user.getNickname())
                .userId(user.getId())
                .userProfileImg(userProfileImg)
                .build();
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
            throw new IllegalArgumentException("已经存在或不能使用的昵称");
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


    @Transactional
    public UserRecordResponse getUserRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        List<GameParticipant> gameParticipants = gameParticipantRepository.findAllByUser(user);
        int win = gameParticipants.stream()
                .filter(gameParticipant -> gameParticipant.getGameRecord().equals(GameRecord.WIN))
                .toList().size();

        int lose = gameParticipants.stream()
                .filter(gameParticipant -> gameParticipant.getGameRecord().equals(GameRecord.LOSE))
                .toList().size();


        return UserRecordResponse.builder()
                .win(win)
                .lose(lose)
                .build();
    }




}
