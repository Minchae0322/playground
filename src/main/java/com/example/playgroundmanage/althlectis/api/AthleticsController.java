package com.example.playgroundmanage.althlectis.api;

import com.example.playgroundmanage.althlectis.dto.request.AthleticsDetailsRequest;
import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;
import com.example.playgroundmanage.althlectis.dto.request.GameResultRequest;
import com.example.playgroundmanage.althlectis.dto.response.AthleticsDetailsResponse;
import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.althlectis.service.AthleticsGenerator;
import com.example.playgroundmanage.althlectis.service.AthleticsResultService;
import com.example.playgroundmanage.althlectis.service.AthleticsService;
import com.example.playgroundmanage.althlectis.service.factory.AthleticsGeneratorFactory;


import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.login.vo.MyUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.example.playgroundmanage.util.GameValidation.validateStartTimeAfterPresent;

@RestController
@RequiredArgsConstructor
public class AthleticsController {

    private final AthleticsService athleticsService;
    
    private final AthleticsGeneratorFactory athleticsGeneratorFactory;

    private final AthleticsResultService athleticsResultService;


    @GetMapping("/game/{gameId}/Friendly/NONE")
    public AthleticsDetailsResponse getFriendlyAthleticsTeam(@PathVariable Long gameId) {
        return athleticsService.getFriendlyAthleticsDetails(new AthleticsDetailsRequest(gameId, "NONE"));
    }

    @GetMapping("/game/{gameId}/Competition/{gameTeamSide}")
    public AthleticsDetailsResponse getRankAthleticsTeam(@PathVariable Long gameId, @PathVariable String gameTeamSide) {
        return athleticsService.getRankAthleticsDetails(new AthleticsDetailsRequest(gameId, gameTeamSide));
    }

    @GetMapping("/game/{gameId}/info")
    public AthleticsResponse getAthleticsInfo(@PathVariable Long gameId) {
        return athleticsService.getGameInfo(gameId);
    }

    @PostMapping("/game/generate")
    public ResponseEntity<Long> generateGame(@RequestBody @Valid GameGenerationRequest gameRegistration, @AuthenticationPrincipal MyUserDetails userDetails) {
        validateStartTimeAfterPresent(gameRegistration.toGameTimeDto());
        AthleticsGenerator gameGenerator = athleticsGeneratorFactory.find(gameRegistration.gameType());

        return ResponseEntity.ok(gameGenerator.generate(userDetails.getUser().getId(), gameRegistration));
    }

    @PreAuthorize("hasPermission(#gameId,'summit_game','CREATE')")
    @PostMapping("/game/result/{gameId}")
    public ResponseEntity<String> summitGameResult(@RequestBody @Valid GameResultRequest gameResultRequest,
                                                   @PathVariable Long gameId) {
        athleticsResultService.updateAthleticsResult(gameResultRequest);

        return ResponseEntity.ok("success");
    }

    //todo delete
}
