package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.login.dto.*;
import com.example.playgroundmanage.exception.TooManyRequestException;

import com.example.playgroundmanage.login.repository.UserGameRecordRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.login.vo.UserGameRecord;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.team.repository.TeamingRepository;

import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.vo.UploadFile;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final TeamingRepository teamingRepository;

    private final PasswordEncoder passwordEncoder;



    private final FileHandler fileHandler;

    private final UserGameRecordRepository userGameRecordRepository;

    private final UserDtoConverter userDtoConverter;

    @Transactional
    public void signup(UserSignupForm userSignupForm) {
        validateUser(userSignupForm.getUsername(), userSignupForm.getPassword());

        if (userRepository.existsByUsername(userSignupForm.getUsername())) {
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
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotExistException::new);
    }

    @Transactional
    public List<TeamDto.TeamResponseDto> getTeamsInfoUserBelongsTo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        List<Teaming> teams = user.getTeams();

        return teams.stream()
                .map(t -> TeamDto.TeamResponseDto.builder()
                        .teamId(t.getTeam().getId())
                        .sportsEvent(t.getTeam().getSportsEvent().getValue())
                        .teamName(t.getTeam().getTeamName())
                        .teamProfileImg(t.getTeam().getTeamProfileImg().getFileUrl())
                        .build())
                .toList();
    }


    @Transactional
    public void updateUserProfileImg(Long userId, MultipartFile img) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        if (user.getUserProfileImg() != null) {
            fileHandler.deleteFile(user.getUserProfileImg());
        }

        UploadFile uploadFile = fileHandler.storeFile(img);
        user.updateProfile(uploadFile);
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        return userDtoConverter.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto getUserInfoInTeam(Team team, User user) {
        String role = teamingRepository.findByTeamAndUser(team, user).getRole();

        return userDtoConverter.toUserInTeamResponseDto(user, role);
    }

    @Transactional
    public UserResponseDto changeNickname(UserRequestDto userRequestDto) {
        if (isValidUserNickname(userRequestDto.getUserNickname())) {
            throw new IllegalArgumentException("已经存在或不能使用的昵称");
        }

        User user = userRepository.findById(userRequestDto.getUserId())
                .orElseThrow(UserNotExistException::new);

        userRepository.save(user.update(UserEdit.builder()
                .userNickname(userRequestDto.getUserNickname())
                .build()));

        return userDtoConverter.toUserNicknameResponseDto(userRequestDto.getUserNickname());
    }

    public boolean isValidUserNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    @Transactional
    public UserGameRecord findUserGameRecordByUser(User user) {
        return userGameRecordRepository.findUserGameRecordByUser(user)
                .orElseGet(() -> userGameRecordRepository.save(
                        UserGameRecord.builder()
                                .win(0)
                                .lose(0)
                                .draw(0)
                                .user(user)
                                .lastUpdateTime(LocalDateTime.now().minusMinutes(15L))
                                .build()
                ));
    }

    @Transactional
    public UserRecordResponse getUserRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        UserGameRecord userGameRecord = findUserGameRecordByUser(user);

        return UserRecordResponse.builder()
                .win(userGameRecord.getWin())
                .lose(userGameRecord.getLose())
                .build();
    }

    //todo
    /*@Transactional
    public UserRecordResponse updateUserGameRecord(User user) {
        UserGameRecord userGameRecord = findUserGameRecordByUser(user);

        if (userGameRecord.getLastUpdateTime().isAfter(LocalDateTime.now().minusMinutes(10))) {
            throw new TooManyRequestException();
        }



        int win = gameParticipants.stream()
                .filter(gameParticipant -> gameParticipant.getGameRecord().equals(GameRecord.WIN))
                .toList().size();

        int lose = gameParticipants.stream()
                .filter(gameParticipant -> gameParticipant.getGameRecord().equals(GameRecord.LOSE))
                .toList().size();

        userGameRecordRepository.save(userGameRecord.update(win, lose));

        return UserRecordResponse.builder()
                .win(win)
                .lose(lose)
                .build();
    }*/


    @Transactional
    private UserGameRecord findUserGameRecord(User user) {
        return userGameRecordRepository.findUserGameRecordByUser(user)
                .orElse(userGameRecordRepository.save(UserGameRecord.builder()
                        .win(0)
                        .draw(0)
                        .lose(0)
                        .none(0)
                        .build()));
    }


    public List<UserRecordResponse> getUsersRanking() {
        List<UserGameRecord> userRankingDes = userGameRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(UserGameRecord::getWin).reversed()
                        .thenComparing(UserGameRecord::getLose))
                .toList();

        return IntStream.range(0, userRankingDes.size())
                .mapToObj(index -> UserRecordResponse.builder()
                        .ranking(index + 1)
                        .win(userRankingDes.get(index).getWin())
                        .lose(userRankingDes.get(index).getLose())
                        .userProfileImg(userRankingDes.get(index).getUser().getUserProfileImg().getFileUrl())
                        .userNickname(userRankingDes.get(index).getUser().getNickname())
                        .build()
                )
                .toList();
    }
}
