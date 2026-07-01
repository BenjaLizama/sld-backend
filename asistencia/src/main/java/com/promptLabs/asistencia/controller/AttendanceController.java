package com.promptLabs.asistencia.controller;

import com.promptLabs.asistencia.dto.AttendanceRequestDto;
import com.promptLabs.asistencia.dto.AttendanceResponseDto;
import com.promptLabs.asistencia.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/asistencias")
@AllArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/pass-attendance/{id}")
    public ResponseEntity<AttendanceResponseDto> createAttendance(@RequestBody @Valid AttendanceRequestDto requestDto , @PathVariable UUID id) {
        AttendanceResponseDto response = attendanceService.passAttendance(requestDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<List<AttendanceResponseDto>> getAsistencia(@PathVariable UUID id) {
        List<AttendanceResponseDto> response = attendanceService.getAttendances(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> listAttendances() {
        List<AttendanceResponseDto> response = attendanceService.listAttendances();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<AttendanceResponseDto>  updateAttendance(@RequestBody @Valid AttendanceRequestDto requestDto, @PathVariable UUID id ) {
        AttendanceResponseDto response = attendanceService.updateAttendance(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID id) {
        attendanceService.removeAttendance(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
