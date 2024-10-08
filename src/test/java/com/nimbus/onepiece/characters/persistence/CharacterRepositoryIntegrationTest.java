package com.nimbus.onepiece.characters.persistence;

 import com.nimbus.onepiece.characters.PersistenceTestData;
import com.nimbus.onepiece.characters.domain.Faction;
import com.nimbus.onepiece.characters.domain.Role;
import com.nimbus.onepiece.characters.persistence.records.CharacterRecord;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("integration")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CharacterRepositoryIntegrationTest {

    @Autowired
    CharacterRepository objectUnderTest;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Test
    @Order(1)
    void contextLoads() {
        assertTrue(postgres.isRunning());
    }

    @Test
    @Order(2)
    void findById() {
        //given
        UUID luffyId = PersistenceTestData.CHARACTER_LUFFY.id();
        //when
        CharacterRecord actual = objectUnderTest.findById(luffyId).block();
        //then
        assertNotNull(actual);
        assertEquals(luffyId, actual.id());
        assertEquals("Monkey D. Luffy", actual.name());
        assertEquals(Role.CAPTAIN.name(), actual.role());
        assertEquals(Faction.PIRATE.name(), actual.faction());
        assertNotNull(actual.crewId());
        assertNotNull(actual.devilFruitCode());
    }

    @Test
    @Order(3)
    void findById_notfound() {
        //given
        UUID unknownId = UUID.randomUUID();
        //when
        CharacterRecord actual = objectUnderTest.findById(unknownId).block();
        //then
        assertNull(actual);
    }

    @Test
    @Order(4)
    void findAllByCrewId() {
        //given
        UUID strawHatsId = PersistenceTestData.CREW_STRAW_HATS.id();
        //when
        Collection<CharacterRecord> actual = objectUnderTest.findAllByCrewId(strawHatsId).collectList().block();
        //then
        assertNotNull(actual);
        assertEquals(10, actual.size());
        assertEquals(10, actual.stream().filter(c -> c.crewId().equals(strawHatsId)).count());
    }

    @Test
    @Order(5)
    void findAllByCrewId_notfound() {
        //given
        UUID unknownId = UUID.randomUUID();
        //when
        Collection<CharacterRecord> actual = objectUnderTest.findAllByCrewId(unknownId).collectList().block();
        //then
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    @Order(6)
    void findAll() {
        //when
        Collection<CharacterRecord> actual = objectUnderTest.findAll().collectList().block();
        //then
        assertNotNull(actual);
        assertEquals(10, actual.size());
    }
}