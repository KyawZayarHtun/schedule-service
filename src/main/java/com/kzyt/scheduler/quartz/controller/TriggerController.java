package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.service.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("triggers")
@RequiredArgsConstructor
public class TriggerController {

    private final TriggerService triggerService;

    @PostMapping("pause")
    public ResponseEntity<String> pauseTrigger(
            @Validated @RequestBody JobIdentifier request
    ) {
        triggerService.pauseTrigger(request.name(), request.group());

        return ResponseEntity.ok("Trigger paused successfully.");
    }

    @PostMapping("resume")
    public ResponseEntity<String> resumeTrigger(
            @Validated @RequestBody JobIdentifier request
    ) {
        triggerService.resumeTrigger(request.name(), request.group());

        return ResponseEntity.ok("Trigger paused successfully.");
    }


    @DeleteMapping("delete/{group}")
    public ResponseEntity<String> deleteTrigger(@PathVariable String group, @RequestParam String name) {
        triggerService.deleteTrigger(name, group);

        return ResponseEntity.ok("Trigger deleted successfully.");
    }



}
