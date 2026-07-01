package com.promptLabs.asistencia.service;

import com.promptLabs.asistencia.dto.AttendanceRequestDto;
import com.promptLabs.asistencia.dto.AttendanceResponseDto;
import com.promptLabs.asistencia.entity.Attendance;
import com.promptLabs.asistencia.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public AttendanceResponseDto passAttendance(AttendanceRequestDto attendanceRequestDto, UUID studentId) {
        attendanceRepository.findByStudentIdAndAttendanceDate(studentId, attendanceRequestDto.attendanceDate())
                .ifPresent(existingAttendance -> {
                    throw new IllegalArgumentException("Attendance already exists");
                });

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(attendanceRequestDto.attendanceDate());
        attendance.setStatus(attendanceRequestDto.attendanceStatus());
        attendance.setAttendanceTime(LocalTime.now());
        Attendance saved = attendanceRepository.save(attendance);

        return new AttendanceResponseDto(saved.getAttendanceDate(), saved.getStatus(), saved.getStudentId(), saved.getId());

    }

    @Transactional
    public void removeAttendance(UUID attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new IllegalArgumentException("Attendance not found"));
        attendanceRepository.delete(attendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAttendances(UUID studentId) {
        List<Attendance> attendance = attendanceRepository.findAllByStudentId(studentId);
        return attendance.stream().map(this::toAttendanceResponseDto).toList();
    }
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> listAttendances() {
        return attendanceRepository.findAll().stream().map(this::toAttendanceResponseDto).toList();
    }

    @Transactional
    public AttendanceResponseDto updateAttendance(UUID Id, AttendanceRequestDto requestDto) {
        Attendance savedAttendance = attendanceRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found"));
        if (!savedAttendance.getAttendanceDate().equals(requestDto.attendanceDate())) {
            attendanceRepository.findByStudentIdAndAttendanceDate(savedAttendance.getStudentId(), requestDto.attendanceDate())
                    .ifPresent(existingAttendance -> {
                        throw new IllegalArgumentException("Attendance already exists");
                    });
        }


        savedAttendance.setAttendanceDate(requestDto.attendanceDate());
        savedAttendance.setStatus(requestDto.attendanceStatus());

        return new AttendanceResponseDto(savedAttendance.getAttendanceDate(), savedAttendance.getStatus(), savedAttendance.getStudentId(), savedAttendance.getId());
    }

    private AttendanceResponseDto toAttendanceResponseDto(Attendance attendance) {
        return new AttendanceResponseDto(
                attendance.getAttendanceDate(),
                attendance.getStatus(),
                attendance.getStudentId(),
                attendance.getId()
        );
    }
}
