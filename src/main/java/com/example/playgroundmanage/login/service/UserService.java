package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.althlectis.repo.AthleticsParticipantRepository;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.login.dto.*;

import com.example.playgroundmanage.login.repository.UserAthleticsRecordRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.login.vo.UserAthleticsRecord;
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
import java.util.Optional;
import java.util.stream.IntStream;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final TeamingRepository teamingRepository;

    private final PasswordEncoder passwordEncoder;

    private final AthleticsParticipantRepository athleticsParticipantRepository;

    private final FileHandler fileHandler;

    private final UserAthleticsRecordRepository userAthleticsRecordRepository;

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
    public UserRecordResponse getUserRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        UserAthleticsRecord userAthleticsRecord = userAthleticsRecordRepository.findByUser(user)
                .orElseThrow();

        return UserRecordResponse.from(userAthleticsRecord);
    }



    public List<UserRecordResponse> getUsersRanking() {
        List<UserAthleticsRecord> userRankingDes = userAthleticsRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(UserAthleticsRecord::getWin).reversed()
                        .thenComparing(UserAthleticsRecord::getLose))
                .toList();

        return IntStream.range(0, userRankingDes.size())
                .mapToObj(index -> UserRecordResponse.from(userRankingDes.get(index), index)
                )
                .toList();
    }

}
