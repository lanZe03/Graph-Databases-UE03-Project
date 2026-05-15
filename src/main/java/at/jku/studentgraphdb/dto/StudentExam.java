package at.jku.studentgraphdb.dto;

public record StudentExam(
        String lectureId,
        String lectureTopic,
        String examDate,
        String examRoom,
        Long currentGrade,
        String examKey
) {
}
