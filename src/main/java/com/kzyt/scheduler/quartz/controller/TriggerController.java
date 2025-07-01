package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.service.TriggerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("triggers")
@RequiredArgsConstructor
@Tag(name = "Trigger Management")
public class TriggerController {

    private final TriggerService triggerService;

    @Operation(summary = "Pause a trigger",
            description = "Pause a specific trigger by its name and group")
    @ApiResponse(responseCode = "200", description = "Trigger paused successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Trigger not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("pause")
    public ResponseEntity<String> pauseTrigger(
            @Validated @RequestBody JobIdentifier request
    ) {
        triggerService.pauseTrigger(request.name(), request.group());

        return ResponseEntity.ok("Trigger paused successfully.");
    }

    @Operation(summary = "Resume a trigger",
            description = "Resume a specific trigger by its name and group")
    @ApiResponse(responseCode = "200", description = "Trigger resume successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Trigger not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("resume")
    public ResponseEntity<String> resumeTrigger(
            @Validated @RequestBody JobIdentifier request
    ) {
        triggerService.resumeTrigger(request.name(), request.group());

        return ResponseEntity.ok("Trigger resume successfully.");
    }

    @Operation(summary = "Delete a trigger",
            description = "Delete a specific trigger by its name and group")
    @ApiResponse(responseCode = "200", description = "Trigger delete successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Trigger not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Failed to delete trigger or trigger due to an internal server error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @DeleteMapping("delete/{group}")
    public ResponseEntity<String> deleteTrigger(@PathVariable String group, @RequestParam String name) {
        triggerService.deleteTrigger(name, group);

        return ResponseEntity.ok("Trigger deleted successfully.");
    }



}
