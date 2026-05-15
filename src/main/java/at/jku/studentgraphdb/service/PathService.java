package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.dto.Path;
import org.neo4j.driver.Record;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Use a neo4j Client here instead of Repositories,
 * because Paths do not map to a domain entity, and the repositories require that.
 */
@Service
public class PathService {

    private final Neo4jClient neo4jClient;

    public PathService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public List<Path> findPaths(String fromValue, String toValue) {
        if (fromValue == null || fromValue.isBlank() || toValue == null || toValue.isBlank()) {
            return Collections.emptyList();
        }

        // constrain the length of the paths to length of 6
        // to not execute very expensive queries

        // first, go through all possible properties to see if any match for A and B
        // then get a Path between them
        // finally return all distinct paths, with a nice -> between the text to get a nice output text
        String query = """
                MATCH (a), (b)
                WHERE elementId(a) <> elementId(b)
                  AND (
                        coalesce(a.name, '') = $fromValue
                     OR coalesce(a.id, '') = $fromValue
                     OR coalesce(a.matriculationNumber, '') = $fromValue
                     OR coalesce(a.employeeNumber, '') = $fromValue
                     OR coalesce(a.topic, '') = $fromValue
                     OR coalesce(a.room, '') = $fromValue
                     OR coalesce(a.date, '') = $fromValue
                  )
                  AND (
                        coalesce(b.name, '') = $toValue
                     OR coalesce(b.id, '') = $toValue
                     OR coalesce(b.matriculationNumber, '') = $toValue
                     OR coalesce(b.employeeNumber, '') = $toValue
                     OR coalesce(b.topic, '') = $toValue
                     OR coalesce(b.room, '') = $toValue
                     OR coalesce(b.date, '') = $toValue
                  )
                MATCH p = (a)-[*..6]-(b)
                RETURN DISTINCT
                  reduce(txt = '', n IN nodes(p) |
                    txt +
                    CASE WHEN txt = '' THEN '' ELSE ' -> ' END +
                    coalesce(n.name, n.id, n.matriculationNumber, n.employeeNumber, n.topic, n.room, n.date, 'node')
                  ) AS pathText,
                  length(p) AS pathLength
                ORDER BY pathLength ASC, pathText ASC
                """;

        // Map each return path to a Path Object using the mapping function
        // then return all of them
        return (List<Path>) neo4jClient.query(query)
                .bindAll(Map.of("fromValue", fromValue.trim(), "toValue", toValue.trim()))
                .fetchAs(Path.class)
                .mappedBy((typeSystem, record) -> mapPath(record))
                .all();
    }

    private Path mapPath(Record record) {
        return new Path(
                record.get("pathText").asString(),
                record.get("pathLength").asLong()
        );
    }
}