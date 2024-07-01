package com.example.playgroundmanage.location.api;

import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.althlectis.dto.request.AthleticsTimeInfo;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.location.dto.response.OccupiedTimeResponse;
import com.example.playgroundmanage.location.dto.PlaygroundRequestDto;
import com.example.playgroundmanage.location.dto.response.PlaygroundInfoResponse;
import com.example.playgroundmanage.location.service.PlaygroundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @PostMapping(value = "/playground/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void addPlayground(@RequestPart(value = "playgroundRequest") PlaygroundRequestDto playgroundRequestDto,  @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile) {
        playgroundRequestDto.setPlaygroundProfileImg(multipartFile);

        playgroundService.addPlayground(playgroundRequestDto);
    }

    //todo
   /* @GetMapping("/playground/{playgroundId}/current")
    public ResponseEntity<GameResponseDto> getOngoingGame(@PathVariable Long playgroundId) {
        return ResponseEntity.ok(playgroundService.getOngoingGame(playgroundId));
    }*/

    @GetMapping("/playground/{playgroundId}/upComing")
    public List<AthleticsResponse> getUpcomingGames(@PathVariable Long playgroundId) {
        return playgroundService.getUpcomingAthletics(playgroundId);
    }

    @GetMapping("/playground/{playgroundId}/info")
    public PlaygroundInfoResponse getPlaygroundInfo(@PathVariable Long playgroundId) {
        return playgroundService.getPlaygroundInfo(playgroundId);
    }

    @PostMapping("/playground/{playgroundId}/occupiedTime")
    public List<OccupiedTimeResponse> getOccupiedTimeLines(@PathVariable Long playgroundId, @RequestBody @Valid AthleticsTimeInfo athleticsTimeInfo) {
        GameTimeDto gameTimeDto = AthleticsTimeInfo.toGameTimeDto(athleticsTimeInfo);

        return playgroundService.getPlaygroundOccupiedTimeLines(playgroundId, gameTimeDto);
    }

    @PostMapping("/playground/{playgroundId}/valid-start")
    public boolean validateGameStartTime(@PathVariable Long playgroundId, @RequestBody @Valid AthleticsTimeInfo athleticsTimeInfo) {
        GameTimeDto gameTimeDto = AthleticsTimeInfo.toGameTimeDto(athleticsTimeInfo);

        return playgroundService.isValidGameStartTime(playgroundId, gameTimeDto);
    }


}
