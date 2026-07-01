package com.promptLabs.asistencia.exception;

public class AttendanceConflict extends RuntimeException {
    public AttendanceConflict(String message) {
        super(message);
    }
}
